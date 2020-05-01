package com.shu.find.dto;

import lombok.Data;
import com.shu.find.model.User;

/**
 * Author:ShiQi
 * Date:2019/12/14-2:23
 */
@Data
public class CommentDTO {
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
    private boolean status;
}
