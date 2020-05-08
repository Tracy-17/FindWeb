package com.shu.find.controller;

import com.shu.find.dto.NotificationDTO;
import com.shu.find.enums.NotificationTypeEnum;
import com.shu.find.model.User;
import com.shu.find.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

/**
 * Author:ShiQi
 * Date:2020/5/7-14:08
 */
@Controller
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping("notification/{id}")//访问profile+动态页面时调到此处
    public String profile(HttpServletRequest request,
                          @PathVariable(name = "id") Integer id) {
        //验证登录：
        User user = (User) request.getSession().getAttribute("user");
        //未登录跳转：
        if (user == null) {
            return "redirect:/index";
        }

        NotificationDTO notificationDTO = notificationService.read(id, user);
        if (NotificationTypeEnum.REPLY_COMMENT.getType() == notificationDTO.getType()
                || NotificationTypeEnum.REPLY_QUESTION.getType() == notificationDTO.getType()
                || NotificationTypeEnum.CHOSE.getType() == notificationDTO.getType()) {
            return "redirect:/content/" + notificationDTO.getOuterId();
        } else {
            return "redirect:/index";
        }

    }
}
