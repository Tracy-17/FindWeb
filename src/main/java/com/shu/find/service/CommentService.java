package com.shu.find.service;

import com.shu.find.dto.CommentChoseDTO;
import com.shu.find.dto.CommentDTO;
import com.shu.find.enums.CommentTypeEnum;
import com.shu.find.enums.NotificationStatusEnum;
import com.shu.find.enums.NotificationTypeEnum;
import com.shu.find.exception.CustomizeErrorCode;
import com.shu.find.exception.CustomizeException;
import com.shu.find.mapper.*;
import com.shu.find.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author ShiQi
 * @Date 2020/4/23 18:05
 */
@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private ContentMapper contentMapper;
    @Autowired
    private ContentExtMapper contentExtMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CommentExtMapper commentExtMapper;
    @Autowired
    private NotificationMapper notificationMapper;

    //添加评论
    //    Transactional:将整个方法体设为同一个事物
    @Transactional
    public void insert(Comment comment, User commentator) {
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }

        if (comment.getType() == CommentTypeEnum.COMMENT.getType()) {
            //回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            //查询当前问题题目
            Content content = contentMapper.selectByPrimaryKey(dbComment.getParentId());
            if (content == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            //增加父评论的评论数
            Comment parentComment = new Comment();
            parentComment.setId(comment.getParentId());
            parentComment.setCommentCount(1);
            commentExtMapper.incComment(parentComment);
            //创建通知
            createNotify(comment, dbComment.getCommentator(), commentator.getName(), content.getTitle(), NotificationTypeEnum.REPLY_COMMENT.getType(), content.getId(),0);
        } else {
            //回复问题
            Content content = contentMapper.selectByPrimaryKey(comment.getParentId());
            if (content == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            content.setCommentCount(1);
            contentExtMapper.incComment(content);
            //创建通知
            createNotify(comment, content.getCreator(), commentator.getName(), content.getTitle(), NotificationTypeEnum.REPLY_QUESTION.getType(), content.getId(),0);
        }
    }

    //展示在内容页的评论
    public List<CommentDTO> listByTargetId(Integer id, CommentTypeEnum type) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(id)
                //ctrl+alt+P：Extracting parameter to listByQuestionId(Integer)抽参数;ctrl+F6修改参数类型
                //为问题回复
                .andTypeEqualTo(type.getType());
        //按时间倒序显示回复：
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        if (comments.size() == 0) {
            return new ArrayList<>();
        }
        // λ ？获取去重的评论者
        //新语法：map（）遍历以下得到结果集，collect得到结果集，toSet转换为Set
        Set<Integer> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Integer> userIds = new ArrayList<>();
        userIds.addAll(commentators);
        //获取评论者并转换为map
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        //comments与users匹配：
//        效率过低：for(Comment comment:comments){for(User user:users){}}

        //同上，将user做成map
        Map<Integer, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

        //转换Comment为CommentDTO
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            //两类之间copy：BeanUtils.copyProperties（source,target）;
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());

        return commentDTOS;
    }

    //更改评论状态
    @Transactional
    public void updateChose(CommentChoseDTO commentChoseDTO) {
        Comment comment = commentMapper.selectByPrimaryKey(commentChoseDTO.getCommentId());
        Content content = contentMapper.selectByPrimaryKey(commentChoseDTO.getContentId());
        if(comment.getType()==CommentTypeEnum.CONTENT.getType()) {
            comment.setType(CommentTypeEnum.ANSWER.getType());
            //给评论者的通知
            createNotify(comment, comment.getCommentator(), commentChoseDTO.getCreatorName(), content.getTitle(), NotificationTypeEnum.CHOSE.getType(), content.getId(),commentChoseDTO.getCreatorId());
        }else if(comment.getType()==CommentTypeEnum.ANSWER.getType()){
            comment.setType(CommentTypeEnum.CONTENT.getType());
        }
        commentMapper.updateByPrimaryKey(comment);
    }

    //创建通知
    private void createNotify(Comment comment, Integer receiver, String notifierName, String outerTitle, Integer notificationType, Integer outerId,Integer contentCreatorId) {
        //type1、2接收者是自己，不创建通知
        if (receiver == comment.getCommentator()&&contentCreatorId==0) {
            return;
        }
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(notificationType);
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());

        notification.setOuterId(outerId);
        if(contentCreatorId==0){
            notification.setNotifier(comment.getCommentator());
        }else{
            notification.setNotifier(contentCreatorId);
        }
        notification.setReceiver(receiver);
        //状态：0：未读，1：已读
        notification.setStatus(0);
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(outerTitle);
        notificationMapper.insert(notification);
    }
}
