package com.shu.find.dto;

import com.shu.find.enums.LikeTypeEnum;
import com.shu.find.service.LikeService;
import lombok.Data;
import com.shu.find.model.User;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Author:ShiQi
 * Date:2019/12/14-2:23
 */
@Data
public class CommentDTO {
    @Autowired
    LikeService likeService;

    private Integer id;
    private Integer parentId;
    private Integer type;
    private Integer commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer commentCount;
    private Integer likeCount;
    private String content;
    //新增：
    private User user;
    /*
    * 前端传的dataType为json，参数值true或false。后端，注意是重点！变量类型如果是boolean，那转换后的值永远为false。
改为Boolean问题解决。*/
    private Boolean isInLike;
    private Boolean isChose;

}
