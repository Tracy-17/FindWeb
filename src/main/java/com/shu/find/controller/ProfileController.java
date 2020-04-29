package com.shu.find.controller;

import com.shu.find.dto.PaginationDTO;
import com.shu.find.model.User;
import com.shu.find.service.NotificationService;
import com.shu.find.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

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
    private QuestionService questionService;
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/profile/{action}")//访问profile+动态页面时调到此处
    public String profile(HttpServletRequest request,
                          @PathVariable(name = "action") String action,
                          Model model) {
        //验证登录：
        User user = (User) request.getSession().getAttribute("user");
        //未登录跳转：
        if (user == null) {
            return "redirect:/index";
        }

        if ("questions".equals(action)) {
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的问题");
            PaginationDTO paginationDTO = questionService.list(user.getId());
            model.addAttribute("pagination", paginationDTO);
        } else if ("replies".equals(action)) {
            PaginationDTO paginationDTO = notificationService.list(user.getId());
            model.addAttribute("section", "replies");
            model.addAttribute("pagination", paginationDTO);
            model.addAttribute("sectionName", "最新回复");
        }

        return "profile";
    }
    @GetMapping("/info")
    public String info(HttpServletRequest request,Model model){
        //获取当前页面用户信息
        //验证登录：
        User user = (User) request.getSession().getAttribute("user");
        //未登录跳转：
        if (user == null) {
            return "redirect:/index";
        }
        model.addAttribute("user",user);
        return "info";
    }
}
