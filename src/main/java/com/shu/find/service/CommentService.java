package com.shu.find.service;

import com.shu.find.dto.CommentDTO;
import com.shu.find.enums.CommentTypeEnum;
import com.shu.find.enums.NotificationStatusEnum;
import com.shu.find.enums.NotificationTypeEnum;
import com.shu.find.exception.CustomizeErrorCode;
import com.shu.find.exception.CustomizeException;
import com.shu.find.mapper.*;
import com.shu.find.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private ContentMapper contentMapper;
    @Resource
    private ContentExtMapper contentExtMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private CommentExtMapper commentExtMapper;
    @Resource
    private NotificationMapper notificationMapper;

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
            createNotify(comment, dbComment.getCommentator(), commentator.getName(), content.getTitle(), NotificationTypeEnum.REPLY_COMMENT, content.getId());
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
            createNotify(comment, content.getCreator(), commentator.getName(), content.getTitle(), NotificationTypeEnum.REPLY_QUESTION, content.getId());
        }
    }

    private void createNotify(Comment comment, Integer receiver, String notifierName, String outerTitle, NotificationTypeEnum notificationType, Integer outerId) {
        //接收者是自己，不创建通知
        if (receiver == comment.getCommentator()) {
            return;
        }
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(notificationType.getType());
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());

        notification.setOuterId(outerId);
        notification.setNotifier(comment.getCommentator());
        notification.setReceiver(receiver);
        //状态：0：未读，1：已读
        notification.setStatus(0);
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(outerTitle);
        notificationMapper.insert(notification);
    }

    public List<CommentDTO> listByTargetId(Integer id, CommentTypeEnum type) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(id)
                //为问题回复
                //ctrl+alt+P：Extracting parameter to listByQuestionId(Integer)抽参数;ctrl+F6修改参数类型
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
}
