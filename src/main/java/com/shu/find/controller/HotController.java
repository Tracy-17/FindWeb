package com.shu.find.controller;

import com.shu.find.service.CommentService;
import com.shu.find.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @Author ShiQi
 * @Date 2020/4/23 18:01
 * 热门
 * 显示：
 * 热门话题
 * 浏览+点赞*10+评论*100+收藏*1000，以此排序，文章or问题
 * 浏览最多
 * 点赞最对
 * 评论最多
 * 热点回答（单条评论点赞最多）
 */
@Controller
public class HotController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private CommentService commentService;
}
