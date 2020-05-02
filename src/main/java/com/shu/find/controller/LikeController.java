package com.shu.find.controller;

import com.shu.find.dto.PaginationDTO;
import com.shu.find.dto.ResultDTO;
import com.shu.find.enums.LikeTypeEnum;
import com.shu.find.exception.CustomizeErrorCode;
import com.shu.find.model.Mylike;
import com.shu.find.model.User;
import com.shu.find.service.LikeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Author:ShiQi
 * Date:2020/5/2-19:17
 */
@Controller
public class LikeController {
    @Resource
    private LikeService likeService;

    //测试：
    @ResponseBody
    @RequestMapping(value = "/like/{action}", method = RequestMethod.POST)
    public Object like(@RequestBody Mylike mylike,
                       @PathVariable(name = "action") String action,
                       HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        mylike.setUserId(user.getId());
        if("Content".equals(action)){
            mylike.setType(LikeTypeEnum.CONTENT.getType());
        }else if("Comment".equals(action)){
            mylike.setType(LikeTypeEnum.COMMENT.getType());
        }

        if (likeService.isInLike(user.getId(),mylike.getType(),mylike.getContentId())){
            likeService.delete(mylike);
        }else{
            likeService.insert(mylike);
        }
        return ResultDTO.okOf();
    }

   /* @ResponseBody
    @RequestMapping(value = "/likeContent", method = RequestMethod.POST)
    public Object likeContent(@RequestBody Mylike mylike,
                       HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        mylike.setUserId(user.getId());
        mylike.setType(LikeTypeEnum.CONTENT.getType());
        if (likeService.isInLike(user.getId(),mylike.getType(),mylike.getContentId())){
            likeService.delete(mylike);
        }else{
            likeService.insert(mylike);
        }
        return ResultDTO.okOf();
    }
    @ResponseBody
    @RequestMapping(value = "/likeComment", method = RequestMethod.POST)
    public Object likeComment(@RequestBody Mylike mylike,
                       HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        mylike.setUserId(user.getId());
        mylike.setType(LikeTypeEnum.COMMENT.getType());
        if (likeService.isInLike(user.getId(),mylike.getType(),mylike.getContentId())){
            likeService.delete(mylike);
        }else{
            likeService.insert(mylike);
        }
        return ResultDTO.okOf();
    }*/
}
