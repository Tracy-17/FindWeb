package com.shu.find.service;

import com.shu.find.enums.LikeTypeEnum;
import com.shu.find.exception.CustomizeErrorCode;
import com.shu.find.exception.CustomizeException;
import com.shu.find.mapper.*;
import com.shu.find.model.*;
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
public class LikeService {
    @Resource
    private MylikeMapper mylikeMapper;
    @Resource
    private ContentMapper contentMapper;
    @Resource
    private ContentExtMapper contentExtMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private CommentExtMapper commentExtMapper;


    //查询是否已被点赞
    public boolean isInLike(Integer userId, Integer type, Integer id) {
        MylikeExample mylikeExample = new MylikeExample();
        //查找此人的此类的所有赞
        mylikeExample.createCriteria()
                .andUserIdEqualTo(userId)
                .andTypeEqualTo(type)
                .andContentIdEqualTo(id);
        List<Mylike> mylikes = mylikeMapper.selectByExample(mylikeExample);
        if (mylikes.size()==1) {
            return true;
        }
        return false;
    }
    //点赞
    @Transactional
    public void insert(Mylike mylike) {
        mylike.setGmtCreate(System.currentTimeMillis());
        mylikeMapper.insert(mylike);
        if(mylike.getType()==LikeTypeEnum.CONTENT.getType()){
            //增加问题的点赞数
            Content content = contentMapper.selectByPrimaryKey(mylike.getContentId());
            if (content == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            content.setLikeCount(1);
            contentExtMapper.incLike(content);
        }else if(mylike.getType()==LikeTypeEnum.COMMENT.getType()){
            //增加评论的点赞数
            Comment comment =commentMapper.selectByPrimaryKey(mylike.getContentId());
            if(comment==null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            comment.setLikeCount(1);
            commentExtMapper.incLike(comment);
        }
    }
    //取消赞
    @Transactional
    public void delete(Mylike mylike) {
        MylikeExample mylikeExample = new MylikeExample();
        mylikeExample.createCriteria()
                .andUserIdEqualTo(mylike.getUserId())
                .andTypeEqualTo(mylike.getType())
                .andContentIdEqualTo(mylike.getContentId());
        mylikeMapper.deleteByExample(mylikeExample);
        if(mylike.getType()==LikeTypeEnum.CONTENT.getType()){
            //减少问题的点赞数
            Content content = contentMapper.selectByPrimaryKey(mylike.getContentId());
            if (content == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            content.setLikeCount(-1);
            contentExtMapper.incLike(content);
        }else if(mylike.getType()==LikeTypeEnum.COMMENT.getType()){
            //减少评论的点赞数
            Comment comment =commentMapper.selectByPrimaryKey(mylike.getContentId());
            if(comment==null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            comment.setLikeCount(-1);
            commentExtMapper.incLike(comment);
        }
    }
}
