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
    private UserExtMapper userExtMapper;
    @Autowired
    private CommentExtMapper commentExtMapper;
    @Autowired
    private NotificationMapper notificationMapper;

    Long now = System.currentTimeMillis();

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
            createNotify(dbComment.getCommentator(), comment.getCommentator(), NotificationTypeEnum.REPLY_COMMENT.getType(), commentator.getName(), content.getId(), content.getTitle());
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
            createNotify(content.getCreator(), comment.getCommentator(), NotificationTypeEnum.REPLY_QUESTION.getType(), commentator.getName(), content.getId(), content.getTitle());
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

    //更改评论状态(被选为优质回答)
    @Transactional
    public void updateChose(CommentChoseDTO commentChoseDTO) {
        Comment comment = commentMapper.selectByPrimaryKey(commentChoseDTO.getCommentId());
        Content content = contentMapper.selectByPrimaryKey(commentChoseDTO.getContentId());
        User user = new User();
        user.setId(comment.getCommentator());
        if (comment.getType() == CommentTypeEnum.CONTENT.getType()) {
            //设为优质回答
            comment.setType(CommentTypeEnum.ANSWER.getType());
            //更新评论者的被优选次数
            user.setChoseCount(1);
            //给评论者创建通知
            //接收者id;创建者id;通知类型;创建者昵称（缓存）;相关内容id;相关内容标题（缓存）
            createNotify(comment.getCommentator(), commentChoseDTO.getCreatorId(), NotificationTypeEnum.CHOSE.getType(), commentChoseDTO.getCreatorName(), content.getId(), content.getTitle());
        } else if (comment.getType() == CommentTypeEnum.ANSWER.getType()) {
            //取消优质回答（设为普通回答）
            comment.setType(CommentTypeEnum.CONTENT.getType());
            //更新评论者的被优选次数
            user.setChoseCount(-1);
            //删除此条通知
            NotificationExample notificationExample = new NotificationExample();
            notificationExample.createCriteria().andNotifierEqualTo(content.getCreator())
                    .andReceiverEqualTo(comment.getCommentator()).
                    andTypeEqualTo(NotificationTypeEnum.CHOSE.getType());
            notificationMapper.deleteByExample(notificationExample);
        }
        userExtMapper.changeChoseCount(user);
        commentMapper.updateByPrimaryKey(comment);
    }

    /**
     * 创建通知
     * @param receiver         ：接收者id
     * @param notifier         ：创建者id
     * @param notificationType ：通知类型
     * @param notifierName     ：创建者昵称（缓存）
     * @param outerId          ：相关内容id
     * @param outerTitle       ：相关内容标题（缓存）
     */
    private void createNotify(Integer receiver, Integer notifier, Integer notificationType, String notifierName, Integer outerId, String outerTitle) {
        //如果接收者是自己，不创建通知
        if (receiver == notifier) {
            return;
        }
        Notification notification = new Notification();
        notification.setGmtCreate(now);
        notification.setReceiver(receiver);
        notification.setNotifier(notifier);
        notification.setType(notificationType);
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setNotifierName(notifierName);
        notification.setOuterId(outerId);
        notification.setOuterTitle(outerTitle);
        notificationMapper.insert(notification);
    }
}
