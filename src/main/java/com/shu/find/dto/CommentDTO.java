package com.shu.find.dto;

import lombok.Data;
import com.shu.find.model.User;

/**
 * Author:ShiQi
 * Date:2019/12/14-2:23
 */
@Data
public class CommentDTO {
    private Long id;
    private Long parentId;
    private Integer type;
    private Long commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer likeCount;
    private String content;
    //新增：
    private User user;
    private Integer commentCount;
}
