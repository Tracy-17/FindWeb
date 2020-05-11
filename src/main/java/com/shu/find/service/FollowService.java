package com.shu.find.service;

import com.shu.find.enums.MyRelationTypeEnum;
import com.shu.find.enums.NotificationStatusEnum;
import com.shu.find.enums.NotificationTypeEnum;
import com.shu.find.mapper.FollowMapper;
import com.shu.find.mapper.NotificationMapper;
import com.shu.find.mapper.UserExtMapper;
import com.shu.find.mapper.UserMapper;
import com.shu.find.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:ShiQi
 * Date:2020/5/1-17:43
 */
@Service
public class FollowService {
    @Autowired
    private FollowMapper followMapper;
    @Autowired
    private UserExtMapper userExtMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private NotificationMapper notificationMapper;

    //查找我的关系人列表
    public List<User> myRelation(Integer userId, Integer type) {
        FollowExample followExample = new FollowExample();
        followExample.setOrderByClause("gmt_create desc");
        List<User> people = new ArrayList<>();
        List<Follow> relations;
        if(type==MyRelationTypeEnum.FOLLOW.getType()) {
            //查找此人的所有关注
            followExample.createCriteria()
                    .andUserIdEqualTo(userId);
            relations= followMapper.selectByExample(followExample);
            for (Follow follow : relations) {
                people.add(userMapper.selectByPrimaryKey(follow.getFollower()));
            }
        }else if(type==MyRelationTypeEnum.FAN.getType()){
            followExample.createCriteria()
                    .andFollowerEqualTo(userId);
            relations= followMapper.selectByExample(followExample);
            for (Follow fan : relations) {
                people.add(userMapper.selectByPrimaryKey(fan.getUserId()));
            }
        }
        return people;
    }
    //查询是否已被关注
    public boolean isFollowed(Integer userId, Integer follower) {
        List<User> followers=myRelation(userId,MyRelationTypeEnum.FOLLOW.getType());
        //遍历
        for (User f : followers) {
            if (follower == f.getId()) {
                return true;
            }
        }
        return false;
    }

    //关注
    @Transactional
    public void insert(Follow follow) {
        follow.setGmtCreate(System.currentTimeMillis());
        followMapper.insert(follow);
        //增加我的关注数
        User my = new User();
        my.setId(follow.getUserId());
        my.setFollowCount(1);
        userExtMapper.changeFollow(my);
        //增加被关注用户的粉丝数
        User follower = new User();
        follower.setId(follow.getFollower());
        follower.setFansCount(1);
        userExtMapper.changeFans(follower);
        //创建通知
        String name = userMapper.selectByPrimaryKey(my.getId()).getName();
        createNotify(my.getId(), name, follower.getId());
    }

    //创建通知
    private void createNotify(Integer notifier, String notifierName, Integer receiver) {
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(NotificationTypeEnum.FOLLOWED.getType());
        //关注消息不给提示，只在列表显示
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notification.setNotifier(notifier);
        notification.setReceiver(receiver);
        notification.setNotifierName(notifierName);
        notificationMapper.insert(notification);
    }

    //取消关注
    @Transactional
    public void delete(Follow follow) {
        FollowExample followExample = new FollowExample();
        //不知道为什么一直超时
        followExample.createCriteria()
                .andUserIdEqualTo(follow.getUserId())
                .andFollowerEqualTo(follow.getFollower());
        followMapper.deleteByExample(followExample);
        //减少我的关注数
        User my = new User();
        my.setId(follow.getUserId());
        my.setFollowCount(-1);
        userExtMapper.changeFollow(my);
        //减少被关注用户的粉丝数
        User follower = new User();
        follower.setId(follow.getFollower());
        follower.setFansCount(-1);
        userExtMapper.changeFans(follower);
        //删除此条通知
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andNotifierEqualTo(follow.getUserId())
                .andReceiverEqualTo(follow.getFollower());
        notificationMapper.deleteByExample(notificationExample);
    }
}
