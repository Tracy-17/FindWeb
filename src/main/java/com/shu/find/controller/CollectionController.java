package com.shu.find.controller;

import com.shu.find.dto.ResultDTO;
import com.shu.find.exception.CustomizeErrorCode;
import com.shu.find.model.Collection;
import com.shu.find.model.User;
import com.shu.find.service.CollectionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CollectionController {
    @Resource
    private CollectionService collectionService;

    //  @ResponseBody：将对象自动序列化成json发送到前端
//  @RequestBody:自动生成json
    @ResponseBody
    @RequestMapping(value = "/collection", method = RequestMethod.POST)
    public Object coll(@RequestBody Collection coll,
                       HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        Collection collection = new Collection();
        collection.setQuestionId(coll.getQuestionId());
        collection.setUserId(user.getId());
        if (collectionService.isInCollection(user.getId(),coll.getQuestionId())) {
            collectionService.delete(collection);
        } else {
            collectionService.insert(collection);
        }
        return ResultDTO.okOf();
    }
}
