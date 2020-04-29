package com.shu.find.controller;

import com.shu.find.dto.CommentCreateDTO;
import com.shu.find.dto.ResultDTO;
import com.shu.find.exception.CustomizeErrorCode;
import com.shu.find.model.Collection;
import com.shu.find.model.Comment;
import com.shu.find.model.User;
import com.shu.find.service.CollectionService;
import com.shu.find.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CollectionController {
    @Resource
    private CollectionService collectionService;

    //  @ResponseBody：将对象自动序列化成json发送到前端
    @ResponseBody
    @RequestMapping(value = "/collection", method = RequestMethod.GET)
//  @RequestBody:自动生成json
    public Object post(@RequestBody Integer questionId,
                       HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        Collection collection = new Collection();
        collection.setQuestionId(questionId);
        collection.setUserId(user.getId());
        if (collectionService.isInCollection(user.getId(),questionId)) {
            collectionService.delete(collection);
        } else {
            collectionService.insert(collection);
        }

        return ResultDTO.okOf();
    }
}
