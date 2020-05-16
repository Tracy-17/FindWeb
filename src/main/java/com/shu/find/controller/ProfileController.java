package com.shu.find.controller;

import com.shu.find.dto.ContentDTO;
import com.shu.find.dto.PaginationDTO;
import com.shu.find.dto.UserDTO;
import com.shu.find.enums.MyRelationTypeEnum;
import com.shu.find.model.Content;
import com.shu.find.model.Follow;
import com.shu.find.model.User;
import com.shu.find.service.FollowService;
import com.shu.find.service.NotificationService;
import com.shu.find.service.ContentService;
import com.shu.find.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author ShiQi
 * @Date 2020/4/23 18:09
 * 我的
 * 通知
 * 我的提问
 * 我的评论
 * 点过的赞
 * 我的收藏
 */
@Controller
public class ProfileController {
    @Autowired
    private ContentService contentService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserService userService;
    @Autowired
    private FollowService followService;

    private User user;
    private List<ContentDTO> contents = null;

    @GetMapping("/profile/{action}")//访问profile+动态页面时调到此处
    public String profile(HttpServletRequest request,
                          @PathVariable(name = "action") String action,
                          Model model) {
        //验证登录：
        user = (User) request.getSession().getAttribute("user");
        //未登录跳转：
        if (user == null) {
            return "redirect:/index";
        }
        if ("contents".equals(action)) {
            model.addAttribute("section", "contents");
            model.addAttribute("sectionName", "我的发布");
            contents = contentService.listByCreator(user);
            model.addAttribute("contents", contents);
        } else if ("replies".equals(action)) {
            PaginationDTO paginationDTO = notificationService.list(user.getId());
            model.addAttribute("section", "replies");
            model.addAttribute("pagination", paginationDTO);
            model.addAttribute("sectionName", "我的消息");
        } else if ("collections".equals(action)) {
            model.addAttribute("section", "collections");
            model.addAttribute("sectionName", "我的收藏");
            contents = contentService.listMyCollection(user.getId());
            model.addAttribute("contents", contents);
        }
        return "profile";
    }

    @GetMapping("/info/{id}/{action}")
    public String info(HttpServletRequest request,
                       @PathVariable(name = "id") Integer id,
                       @PathVariable(name = "action") String action,
                       Model model) {
        //获取当前页面用户信息
        User infoUser = userService.getById(id);
        model.addAttribute("name", infoUser.getName());
        //验证登录：
        user = (User) request.getSession().getAttribute("user");
        //未登录跳转：
        if (user == null) {
            return "redirect:/index";
        }
        UserDTO infoUserDTO = new UserDTO();
        BeanUtils.copyProperties(infoUser, infoUserDTO);
        infoUserDTO.setIsFollowed(followService.isFollowed(user.getId(), id));
        if ("my".equals(action)) {
            model.addAttribute("section", "my");
            model.addAttribute("sectionName", "个人资料");
            model.addAttribute("user", infoUserDTO);
        } else if ("followers".equals(action)) {
            model.addAttribute("section", "followers");
            model.addAttribute("sectionName", "关注列表");
            List<User> followers = followService.myRelation(infoUser.getId(), MyRelationTypeEnum.FOLLOW.getType());
            model.addAttribute("followers", followers);
        } else if ("fans".equals(action)) {
            model.addAttribute("section", "fans");
            model.addAttribute("sectionName", "粉丝列表");
            List<User> fans = followService.myRelation(infoUser.getId(), MyRelationTypeEnum.FAN.getType());
            model.addAttribute("fans", fans);
        }
        return "info";
    }

    @PostMapping("/changeBio")
    public String changeBio(@RequestParam(name = "bio") String bio,
                            HttpServletRequest request) {
        user = (User) request.getSession().getAttribute("user");
        //未登录跳转：
        if (user == null) {
            return "redirect:/index";
        }
        user.setBio(bio);
        userService.update(user);
        return "redirect:/info/"+user.getId()+"/my";
    }
}
