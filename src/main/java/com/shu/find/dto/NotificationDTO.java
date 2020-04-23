package com.shu.find.dto;

import lombok.Data;

/**
 * Author:ShiQi
 * Date:2019/12/19-0:57
 */
@Data
public class NotificationDTO {
    private Long id;
    private Long gmtCreate;
    private Integer type;
    private Integer status;
    private Long notifier;
    private String notifierName;
    //展示在提醒页面的元素
    private String outerTitle;
    private Long outerId;
    private String typeName;
}