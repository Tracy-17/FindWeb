package com.shu.find.service;

import com.shu.find.mapper.FollowMapper;
import com.shu.find.mapper.UserExtMapper;
import com.shu.find.model.Follow;
import com.shu.find.model.FollowExample;
import com.shu.find.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    //查询是否已被关注
    public boolean isFollowed(Integer userId,Integer follower) {
        FollowExample followExample=new FollowExample();
        //查找此人的所有关注
        followExample.createCriteria()
                .andUserIdEqualTo(userId);
        List<Follow> follows = followMapper.selectByExample(followExample);
        //遍历
        for (Follow follow : follows) {
            if (follower == follow.getFollower()) {
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
        User my=new User();
        my.setId(follow.getUserId());
        my.setFollowCount(1);
        userExtMapper.changeFollow(my);
        //增加被关注用户的粉丝数
        User follower=new User();
        follower.setId(follow.getFollower());
        follower.setFansCount(1);
        userExtMapper.changeFans(follower);

        //创建通知
//        createNotify(comment, dbComment.getCommentator(), commentator.getName(), question.getTitle(), NotificationTypeEnum.REPLY_COMMENT, question.getId());
    }
    //取消关注
    @Transactional
    public void delete(Follow follow) {
        follow.setGmtCreate(System.currentTimeMillis());
        followMapper.insert(follow);
        //减少我的关注数
        User my=new User();
        my.setId(follow.getUserId());
        my.setFollowCount(-1);
        userExtMapper.changeFollow(my);
        //减少被关注用户的粉丝数
        User follower=new User();
        follower.setId(follow.getFollower());
        follower.setFansCount(-1);
        userExtMapper.changeFans(follower);
    }
}
