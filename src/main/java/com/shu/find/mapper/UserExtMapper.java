package com.shu.find.mapper;

import com.shu.find.model.User;


/**
 * @Author ShiQi
 * @Date 2020/4/23 18:23
 */
public interface UserExtMapper {
    int changeFollow(User record);
    int changeFans(User record);
    int changeChoseCount(User record);
    int changeLikeCount(User record);
}
