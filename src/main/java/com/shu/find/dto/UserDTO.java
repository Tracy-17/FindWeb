package com.shu.find.dto;

import com.shu.find.model.User;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

/**
 * Author:ShiQi
 * Date:2020/5/10-12:14
 */
@Data
public class UserDTO implements Comparable{
    private Integer id;
    private String account;
    private String password;
    private String token;
    private String name;
    private String avatar;
    private String bio;
    private Integer followCount;
    private Integer fansCount;
    private Long gmtCreate;
    private Long gmtModify;
    private Integer likeCount;
    private Integer choseCount;

    private Integer priority;
    //是否被当前用户关注
    private Boolean isFollowed;
    @Override
    public int compareTo(@NotNull Object o) {
       /* 插入的最新元素>=当前元素时，替换掉当前元素的位置；
        不是>=时，寻找其父级元素进行比较替换*/
        return this.getPriority()-((UserDTO)o).getPriority();
    }
}
