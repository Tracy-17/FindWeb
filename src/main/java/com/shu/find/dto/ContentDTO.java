package com.shu.find.dto;

import com.shu.find.model.User;
import lombok.Data;

/**
 * Author:ShiQi
 * Date:2019/12/8-18:14
 * 展示给前端专用
 */
@Data
public class ContentDTO {
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
}
