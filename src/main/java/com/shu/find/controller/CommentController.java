package com.shu.find.controller;

import com.shu.find.dto.CommentChoseDTO;
import com.shu.find.dto.CommentCreateDTO;
import com.shu.find.dto.CommentDTO;
import com.shu.find.dto.ResultDTO;
import com.shu.find.enums.CommentTypeEnum;
import com.shu.find.exception.CustomizeErrorCode;
import com.shu.find.model.Comment;
import com.shu.find.model.User;
import com.shu.find.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author ShiQi
 * @Date 2020/4/23 19:58
 */
@Controller
public class CommentController {
    @Resource
    private CommentService commentService;

    //发布评论
    //  @ResponseBody：将对象自动序列化成json发送到前端
    @ResponseBody
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
//  @RequestBody:自动生成json
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        if (commentCreateDTO == null || StringUtils.isBlank(commentCreateDTO.getContent())) {
            return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }

        Comment comment = new Comment();

        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());

        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModify(comment.getGmtCreate());
        comment.setCommentCount(0);
        comment.setLikeCount(0);

        comment.setCommentator(user.getId());
        commentService.insert(comment, user);

        return ResultDTO.okOf();
    }

    //更改评论状态
    @ResponseBody
    @RequestMapping(value = "/chose", method = RequestMethod.POST)
    public Object coll(@RequestBody CommentChoseDTO commentChoseDTO,
                       HttpServletRequest request) {
        //登录验证：
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        commentChoseDTO.setCreatorName(user.getName());
        commentChoseDTO.setCreatorId(user.getId());
        commentService.updateChose(commentChoseDTO);
        return ResultDTO.okOf();
    }

    @ResponseBody
    @RequestMapping(value = "/comment/{id}", method = RequestMethod.GET)
    public ResultDTO<List<CommentDTO>> comments(@PathVariable(name = "id") Integer id) {
        List<CommentDTO> commentDTOS = commentService.listByTargetId(id, CommentTypeEnum.COMMENT);
        return ResultDTO.okOf(commentDTOS);
    }
}

