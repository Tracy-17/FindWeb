package com.shu.find.controller;

import com.shu.find.dto.ResultDTO;
import com.shu.find.exception.CustomizeErrorCode;
import com.shu.find.model.Collection;
import com.shu.find.model.Follow;
import com.shu.find.model.User;
import com.shu.find.service.CollectionService;
import com.shu.find.service.FollowService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Author:ShiQi
 * Date:2020/5/1-16:10
 */
@Controller
public class FollowController {
    @Resource
    private FollowService followService;

    @ResponseBody
    @RequestMapping(value = "/follow", method = RequestMethod.POST)
    public Object coll(@RequestBody Follow foll,
                       HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        Follow follow=new Follow();
        follow.setFollower(foll.getFollower());
        follow.setUserId(user.getId());
        if (followService.isFollowed(user.getId(),foll.getFollower())) {
            followService.delete(follow);
        } else {
            followService.insert(follow);
        }
        return ResultDTO.okOf();
    }
}
