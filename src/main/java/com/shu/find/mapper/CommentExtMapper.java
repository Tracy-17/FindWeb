package com.shu.find.mapper;

import com.shu.find.model.Comment;

/**
 * @Author ShiQi
 * @Date 2020/4/23 18:24
 */
public interface CommentExtMapper {
    int incComment(Comment comment);

    int incLike(Comment comment);
}

