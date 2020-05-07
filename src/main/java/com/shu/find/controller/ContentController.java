package com.shu.find.controller;

import com.shu.find.dto.CommentDTO;
import com.shu.find.dto.ContentDTO;
import com.shu.find.enums.CommentTypeEnum;
import com.shu.find.enums.LikeTypeEnum;
import com.shu.find.model.Comment;
import com.shu.find.model.User;
import com.shu.find.service.*;
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
public class ContentController {
    @Resource
    private ContentService contentService;
    @Resource
    private CommentService commentService;
    @Autowired
    private CollectionService collectionService;
    @Autowired
    private FollowService followService;
    @Autowired
    private LikeService likeService;

    @GetMapping("content/{id}")
    public String content(@PathVariable(name = "id") Integer id,
                           Model model, HttpServletRequest request) {
        ContentDTO contentDTO = contentService.getById(id);
        System.out.println(contentDTO.getUser().getName());
        List<ContentDTO> relatedQuestions = contentService.selectRelated(contentDTO);
        List<CommentDTO> comments = commentService.listByTargetId(id, CommentTypeEnum.CONTENT);
        Boolean isInCollection=false;
        Boolean isFollowed=false;
        Boolean isInLikeContent=false;
        //同时显示优质评论
        List<CommentDTO> choseComment=commentService.listByTargetId(id,CommentTypeEnum.ANSWER);
        if(choseComment!=null){
            comments.addAll(0,choseComment);
        }
        for(CommentDTO commentDTO:comments){
            commentDTO.setIsInLike(false);
        }
        User user = (User) request.getSession().getAttribute("user");
        if(user!=null){
            isInCollection=collectionService.isInCollection(user.getId(),id);
            isFollowed=followService.isFollowed(user.getId(),contentDTO.getCreator());
            isInLikeContent=likeService.isInLike(user.getId(), LikeTypeEnum.CONTENT.getType(),contentDTO.getId());
            //查询每一条评论的点赞状态
            for(CommentDTO commentDTO:comments){
                commentDTO.setIsInLike(likeService.isInLike(user.getId(),LikeTypeEnum.COMMENT.getType(),commentDTO.getId()));
                commentDTO.setIsChose(commentDTO.getType()==CommentTypeEnum.ANSWER.getType()?true:false);
            }
        }
        //累加阅读数
        contentService.incView(id);
        model.addAttribute("content", contentDTO);
        model.addAttribute("comments", comments);
        model.addAttribute("relatedQuestions", relatedQuestions);
        model.addAttribute("isInCollection",isInCollection);
        model.addAttribute("isFollowed",isFollowed);
        model.addAttribute("isInLikeContent",isInLikeContent);
        return "content";
    }
}
