package com.shu.find.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FileController {

    //访问路径为：http://localhost:8080/file

    @RequestMapping("/file")

    public String file(){
        System.out.print("================请求路径===跳转file页面====="+"\n");
        return "/file";

    }

//    @RequestMapping("/shangchuan")
//
//    public String shangchuan(){
//        System.out.print("================请求路径===跳转index页面====="+"\n");
//        return "/index";
//
//    }
}