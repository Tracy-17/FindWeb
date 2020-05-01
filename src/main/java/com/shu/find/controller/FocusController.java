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
 * Author:ShiQi
 * Date:2020/5/1-19:56
 */
@Controller
public class FocusController {
    @Autowired
    private ContentService contentService;
    @Autowired
    private UserService userService;

    @GetMapping("/focus")
    //这里只写/ 火狐测试ok，chrome需要在所有地方补全/index
    public String index(Model model,
                        @RequestParam(name = "search", required = false) String search
    ) {

        //获取question数据
        PaginationDTO pagination = contentService.list(search,0);
        model.addAttribute("pagination", pagination);
        model.addAttribute("search", search);

        return "focus";
    }
}
