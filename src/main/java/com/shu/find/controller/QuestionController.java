package com.shu.find.controller;

import com.shu.find.dto.CommentDTO;
import com.shu.find.dto.QuestionDTO;
import com.shu.find.enums.CommentTypeEnum;
import com.shu.find.model.User;
import com.shu.find.service.CollectionService;
import com.shu.find.service.CommentService;
import com.shu.find.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author ShiQi
 * @Date 2020/4/23 20:01
 */
@Controller
public class QuestionController {
    @Resource
    private QuestionService questionService;
    @Resource
    private CommentService commentService;
    @Autowired
    private CollectionService collectionService;

    @GetMapping("question/{id}")
    public String question(@PathVariable(name = "id") Integer id,
                           Model model, HttpServletRequest request) {

        QuestionDTO questionDTO = questionService.getById(id);
        System.out.println(questionDTO.getUser().getName());
        List<QuestionDTO> relatedQuestions = questionService.selectRelated(questionDTO);
        List<CommentDTO> comments = commentService.listByTargetId(id, CommentTypeEnum.QUESTION);
        Boolean is=false;
        User user = (User) request.getSession().getAttribute("user");
        if(user!=null){
            is=collectionService.isInCollection(user.getId(),id);
        }
        //累加阅读数
        questionService.incView(id);
        model.addAttribute("question", questionDTO);
        model.addAttribute("comments", comments);
        model.addAttribute("relatedQuestions", relatedQuestions);
        model.addAttribute("isInCollection",is);
        return "question";
    }
}
