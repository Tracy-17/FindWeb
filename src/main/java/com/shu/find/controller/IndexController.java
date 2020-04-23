package com.shu.find.controller;

import com.shu.find.dto.PaginationDTO;
import com.shu.find.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author ShiQi
 * @Date 2020/4/23 17:55
 * 首页：
 * 按时间倒序显示提问
 * 有搜索功能
 */
@Controller
public class IndexController {
    @Autowired
    private QuestionService questionService;

    @GetMapping("/index")
    //这里只写/ 火狐测试ok，chrome需要在所有地方补全/index
    public String index(Model model,
                        @RequestParam(name = "search", required = false) String search
    ) {

        //获取question数据
        PaginationDTO pagination = questionService.list(search);
        model.addAttribute("pagination", pagination);
        model.addAttribute("search", search);
        return "index";
    }
}
