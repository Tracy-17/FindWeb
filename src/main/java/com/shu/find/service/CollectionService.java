package com.shu.find.service;

import com.shu.find.exception.CustomizeErrorCode;
import com.shu.find.exception.CustomizeException;
import com.shu.find.mapper.CollectionMapper;
import com.shu.find.mapper.QuestionExtMapper;
import com.shu.find.mapper.QuestionMapper;
import com.shu.find.model.Collection;
import com.shu.find.model.CollectionExample;
import com.shu.find.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author ShiQi
 * @Date 2020/4/23 18:12
 */
@Service
public class CollectionService {
    @Autowired
    private CollectionMapper collectionMapper;
    @Resource
    private QuestionMapper questionMapper;
    @Resource
    private QuestionExtMapper questionExtMapper;


    //查询是否已被收藏
    public boolean isInCollection(Integer userId,Integer questionId) {
        CollectionExample collectionExample = new CollectionExample();
        //查找此人的所有收藏
        collectionExample.createCriteria()
                .andUserIdEqualTo(userId);
        List<Collection> collections = collectionMapper.selectByExample(collectionExample);
        //遍历
        for (Collection col : collections) {
            if (questionId == col.getQuestionId()) {
                return true;
            }
        }
        return false;
    }
    //收藏
    @Transactional
    public void insert(Collection collection) {
        collection.setGmtCreate(System.currentTimeMillis());
        collectionMapper.insert(collection);
        //增加问题的收藏数
        Question question = questionMapper.selectByPrimaryKey(collection.getQuestionId());
        if (question == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        question.setCollCount(1);
        questionExtMapper.incCollection(question);
    }
    //取消收藏
    @Transactional
    public void delete(Collection collection) {
        CollectionExample collectionExample = new CollectionExample();
        collectionExample.createCriteria()
                .andUserIdEqualTo(collection.getUserId())
                .andQuestionIdEqualTo(collection.getQuestionId());
        collectionMapper.deleteByExample(collectionExample);
    }
}
