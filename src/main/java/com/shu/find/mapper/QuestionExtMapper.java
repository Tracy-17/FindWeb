package com.shu.find.mapper;

import com.shu.find.model.Question;

import java.util.List;

/**
 * @Author ShiQi
 * @Date 2020/4/23 18:23
 */
public interface QuestionExtMapper {
    int incView(Question record);

    int incComment(Question record);

    int incCollection(Question record);

    List<Question> selectRelated(Question question);

    Integer countBySearch(String search);

    List<Question> selectBySearch(String search);
}
