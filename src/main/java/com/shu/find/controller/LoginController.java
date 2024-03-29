package com.shu.find.controller;

import com.shu.find.dto.PaginationDTO;
import com.shu.find.enums.ContentTypeEnum;
import com.shu.find.model.User;
import com.shu.find.service.ContentService;
import com.shu.find.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.UUID;

/**
 * @Author ShiQi
 * @Date 2020/4/23 20:27
 */
@Slf4j
@Controller
public class LoginController {
    @Autowired
    private ContentService contentService;
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public String doLogin(@RequestParam(name = "account") String account,
                          @RequestParam(name = "password") String password,
                          Model model,
                          //写了HttpServletRequest后，spring会自动将request放入上下文以供使用
                          HttpServletResponse response,
                          HttpServletRequest request) {
        User user = new User();
        user.setAccount(account);
        //首页展示内容：
        PaginationDTO pagination = contentService.list(null, null);
        model.addAttribute("pagination", pagination);
        if (!userService.isExist(account)) {
            //找不到该用户
            model.addAttribute("error", "该用户不存在！");
            return "index";
        }
        User dbUser = userService.getByAccount(account);
        if (dbUser.getPassword().equals(password)) {
            //登录成功，写cookie和session
            String token = UUID.randomUUID().toString();//javaJDK提供的一个自动生成主键的方法:
            user.setToken(token);
            userService.update(user);
            // 写cookie和session
            request.getSession().setAttribute("user", user);
            response.addCookie(new Cookie("token", token));
            log.info("用户 "+dbUser.getName()+" 登录, "+new Date());
            return "redirect:/index";
        } else {
            //登录失败
            model.addAttribute("error", "用户名或密码错误");
            return "index";
        }
    }
    //为方便测试，注册后进入以下接口
    @GetMapping("/login")
    public String login(@RequestParam(name = "account") String account,
                        Model model,
                        HttpServletResponse response,
                        HttpServletRequest request){
        User user = new User();
        user.setAccount(account);
        //首页展示内容：
        PaginationDTO pagination = contentService.list(null, null);
        model.addAttribute("pagination", pagination);
        User dbUser = userService.getByAccount(account);
        String token = UUID.randomUUID().toString();//javaJDK提供的一个自动生成主键的方法:
        dbUser.setToken(token);
        userService.update(dbUser);
        request.getSession().setAttribute("user", user);
        response.addCookie(new Cookie("token", token));
        return "redirect:/index";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response,
                         Model model) {
        //清除缓存数据
    //    更换tomcat后失效
        request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        //        deleteNewCookie.setPath("/");设置本cookie的存放路径（本项目不需要）
        response.addCookie(cookie);
        PaginationDTO pagination = contentService.list(null,null);
        model.addAttribute("pagination", pagination);
        return "index";
    }
}