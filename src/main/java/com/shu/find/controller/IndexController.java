package com.shu.find.controller;

import com.shu.find.dto.PaginationDTO;
import com.shu.find.service.ContentService;
import com.shu.find.service.UserService;
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
    private ContentService contentService;
    @Autowired
    private UserService userService;

    @GetMapping("/index")
    public String index(Model model,
                        @RequestParam(name = "search", required = false) String search
    ) {

        //首页按照时间倒序展示问题列表
        PaginationDTO pagination = contentService.list(search,0);
        model.addAttribute("pagination", pagination);
        model.addAttribute("search", search);

        return "index";
    }

    @GetMapping("/article")
    public String article(Model model,
                        @RequestParam(name = "search", required = false) String search
    ) {
        PaginationDTO pagination = contentService.list(search,1);
        model.addAttribute("pagination", pagination);
        model.addAttribute("search", search);

        return "article";
    }
}
