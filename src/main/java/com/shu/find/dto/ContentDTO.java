package com.shu.find.dto;

import com.shu.find.model.Content;
import com.shu.find.model.User;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

/**
 * Author:ShiQi
 * Date:2019/12/8-18:14
 * 展示给前端专用
 */
@Data
public class ContentDTO implements Comparable{
    private Integer id;
    private String title;
    private String description;
    private String tag;
    private Integer type;
    private Long gmtCreate;
    private Long gmtModify;
    private Integer creator;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
    private Integer collCount;
    //需要展示给前端用户信息，但不存储进数据库
    private User user;
    //收藏时间：
    private Long collTime;

    //热门问题展示
    private Integer priority;
    @Override
    public int compareTo(@NotNull Object o) {
       /* 插入的最新元素>=当前元素时，替换掉当前元素的位置；
        不是>=时，寻找其父级元素进行比较替换*/
        return this.getPriority()-((ContentDTO)o).getPriority();
    }
}
