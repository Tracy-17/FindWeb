package com.shu.find.controller;

import com.shu.find.model.User;
import com.shu.find.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author ShiQi
 * @Date 2020/4/23 20:31
 */
@Controller
public class SignUpController {
    @Autowired
    private UserService userService;

    //get方法：渲染页面
    @GetMapping("/signUp")
    public String signUp(Model model) {
        return "/signUp";
    }

    @PostMapping("/signUp")
    public String doSignUp(@RequestParam(name = "account") String account,
                           @RequestParam(name = "password") String password,
                           @RequestParam(name = "name") String name,
                           @RequestParam(name = "avatar") String avatar,
                           @RequestParam(name = "bio") String bio,
                           Model model) {
        //查询一下是否存在此用户
        if (userService.isExist(account)) {
            //此用户名已存在
            model.addAttribute("error", "此用户名已存在！");
            return "/signUp";
        } else {
            User user = new User();
            user.setAccount(account);
            user.setPassword(password);
            user.setName(name);
            user.setAvatar(avatar);
            user.setBio(bio);
            userService.create(user);
            return "redirect:/index";
        }
    }
}