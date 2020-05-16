package com.shu.find.service;

import com.shu.find.exception.CustomizeErrorCode;
import com.shu.find.exception.CustomizeException;
import com.shu.find.mapper.UserMapper;
import com.shu.find.model.User;
import com.shu.find.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author ShiQi
 * @Date 2020/4/23 18:11
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    //根据account查找id
    public Integer findIdByAccount(String account) {
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andAccountEqualTo(account);
        List<User> users = userMapper.selectByExample(userExample);
        return users.get(0).getId();
    }

    //查询此用户名是否存在
    public boolean isExist(String account) {
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andAccountEqualTo(account);
        List<User> users = userMapper.selectByExample(userExample);
        if (users.size() == 0) {
            return false;
        } else {
            return true;
        }
    }

    //登录验证
    public User getByAccount(String account) {
        User user = userMapper.selectByPrimaryKey(findIdByAccount(account));
        return user;
    }

    Long nowTime = System.currentTimeMillis();

    //注册
    public void create(User user) {
        user.setFansCount(0);
        user.setFollowCount(0);
        user.setChoseCount(0);
        user.setLikeCount(0);
        user.setGmtCreate(nowTime);
        user.setGmtModify(nowTime);
        userMapper.insert(user);
    }

    //重新登录时
    public void update(User user) {
        User dbUser = userMapper.selectByPrimaryKey(findIdByAccount(user.getAccount()));
        dbUser.setGmtModify(nowTime);
        UserExample example = new UserExample();
        example.createCriteria()
                .andAccountEqualTo(dbUser.getAccount());
        userMapper.updateByExampleSelective(user, example);
    }

    public void Delete(User user) {
        userMapper.deleteByPrimaryKey(findIdByAccount(user.getAccount()));
    }

    /**
     * 通过token获取User
     */
    public User getByToken(String token) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andTokenEqualTo(token);
        List<User> results = userMapper.selectByExample(userExample);
        if (results.size() == 0) {
            return null;
        }
        return results.get(0);
    }

    public User getById(Integer id) {
        User user = userMapper.selectByPrimaryKey(id);
        //异常处理
        if (user == null) {
            throw new CustomizeException(CustomizeErrorCode.USER_NOT_FOUND);
        }
        //获取user对象
        return user;
    }
}