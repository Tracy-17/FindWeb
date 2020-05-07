package com.shu.find.dto;

import com.shu.find.model.Comment;
import com.shu.find.model.Content;
import lombok.Data;

/**
 * Author:ShiQi
 * Date:2020/5/7-23:19
 */
@Data
public class CommentChoseDTO {
    private Integer commentId;
    private Integer contentId;
    private String creatorName;
    public Integer creatorId;
}
