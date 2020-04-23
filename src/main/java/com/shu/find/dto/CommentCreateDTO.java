package com.shu.find.dto;

import lombok.Data;

/**
 * Author:ShiQi
 * Date:2019/12/13-19:12
 * 创建评论时页面传递来的DTO
 */
@Data
public class CommentCreateDTO {
    private Integer parentId;
    private String content;
    private Integer type;
}
