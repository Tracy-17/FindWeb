package com.shu.find.controller;

import com.shu.find.cache.HotCatch;
import com.shu.find.dto.PaginationDTO;
import com.shu.find.enums.ContentTypeEnum;
import com.shu.find.model.Content;
import com.shu.find.service.ContentService;
import com.shu.find.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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
    @Autowired
    private HotCatch hotCatch;

    //主页（首页）
    @GetMapping("/index")
    public String index(Model model,
                        @RequestParam(name = "search", required = false) String search,
                        @RequestParam(name = "tag", required = false) String tag
    ) {
        //首页按照时间倒序展示问题列表
        PaginationDTO pagination = contentService.list(search,tag,ContentTypeEnum.QUESTION.getType());
        model.addAttribute("pagination", pagination);
        model.addAttribute("search", search);
        model.addAttribute("tag",tag);
        return "index";
    }

    //文章
    @GetMapping("/article")
    public String article(Model model) {
        PaginationDTO pagination = contentService.list(null,null, ContentTypeEnum.ARTICLE.getType());
        model.addAttribute("pagination", pagination);
        return "article";
    }

    //热门
    @GetMapping("/hot")
    public String hot(Model model){
        List<Content> contents=hotCatch.getHotContents();
        model.addAttribute("contents", contents);
        List<String> tags= hotCatch.getHotTags();
        model.addAttribute("tags",tags);
        return "hot";
    }

    //关注
    @GetMapping("/focus")
    public String focus(){
        return "focus";
    }
}
