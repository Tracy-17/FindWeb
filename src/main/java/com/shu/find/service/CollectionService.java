package com.shu.find.service;

import com.shu.find.exception.CustomizeErrorCode;
import com.shu.find.exception.CustomizeException;
import com.shu.find.mapper.CollectionMapper;
import com.shu.find.mapper.ContentExtMapper;
import com.shu.find.mapper.ContentMapper;
import com.shu.find.model.Collection;
import com.shu.find.model.CollectionExample;
import com.shu.find.model.Content;
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
    @Resource
    private CollectionMapper collectionMapper;
    @Resource
    private ContentMapper contentMapper;
    @Resource
    private ContentExtMapper questionExtMapper;


    //查询是否已被收藏
    public boolean isInCollection(Integer userId,Integer contentId) {
        CollectionExample collectionExample = new CollectionExample();
        //查找此人的所有收藏
        collectionExample.createCriteria()
                .andUserIdEqualTo(userId);
        List<Collection> collections = collectionMapper.selectByExample(collectionExample);
        //遍历
        for (Collection col : collections) {
            if (contentId == col.getQuestionId()) {
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
        Content content = contentMapper.selectByPrimaryKey(collection.getQuestionId());
        if (content == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        content.setCollCount(1);
        questionExtMapper.incCollection(content);
    }
    //取消收藏
    @Transactional
    public void delete(Collection collection) {
        CollectionExample collectionExample = new CollectionExample();
        collectionExample.createCriteria()
                .andUserIdEqualTo(collection.getUserId())
                .andQuestionIdEqualTo(collection.getQuestionId());
        collectionMapper.deleteByExample(collectionExample);
        //减少问题的收藏数
        Content content = contentMapper.selectByPrimaryKey(collection.getQuestionId());
        if (content == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        content.setCollCount(-1);
        questionExtMapper.incCollection(content);
    }
}
