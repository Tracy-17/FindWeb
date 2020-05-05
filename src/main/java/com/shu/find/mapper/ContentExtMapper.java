package com.shu.find.mapper;

import com.shu.find.model.Content;

import java.util.List;

/**
 * @Author ShiQi
 * @Date 2020/4/23 18:23
 */
public interface ContentExtMapper {
    int incView(Content record);

    int incComment(Content record);

    int incCollection(Content record);

    int incLike(Content record);

    List<Content> selectRelated(Content content);

    Integer countBySearch(String search);

    List<Content> selectBySearch(String search,String tag);

}
