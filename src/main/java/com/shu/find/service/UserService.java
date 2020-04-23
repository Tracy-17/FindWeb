package com.shu.find.service;

import com.shu.find.mapper.UserMapper;
import com.shu.find.model.User;
import com.shu.find.model.UserExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author ShiQi
 * @Date 2020/4/23 18:11
 */
@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    //查询此用户名是否存在
    public boolean isExist(String account) {
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andNameEqualTo(account);
        List<User> users = userMapper.selectByExample(userExample);
        if (users.size() == 0) {
            return false;
        } else {
            return true;
        }
    }

    //登录验证
    public User getByAccount(String account) {
        User user = userMapper.selectByPrimaryKey(account);
        return user;
    }

    Long nowTime = System.currentTimeMillis();
    //注册
    public void create(User user) {
        user.setGmtCreate(nowTime);
        user.setGmtModify(nowTime);
        userMapper.insert(user);
    }

    //重新登录时
    public void Update(User user) {
        User dbUser=userMapper.selectByPrimaryKey(user.getName());
        user.setGmtModify(nowTime);
        UserExample example = new UserExample();
        example.createCriteria()
                .andNameEqualTo(dbUser.getName());
        userMapper.updateByExampleSelective(user, example);
    }
    public void Delete(User user){
        userMapper.deleteByPrimaryKey(user.getName());
    }
    /**
     * 通过token获取User
     * @param token （缓存）登录时凭证
     * @return
     */
    public User getByToken(String token){
        UserExample userExample=new UserExample();
        userExample.createCriteria().andTokenEqualTo(token);
        List<User> results=userMapper.selectByExample(userExample);
        if(results.size()==0){
            return null;
        }
        return results.get(0);
    }
}