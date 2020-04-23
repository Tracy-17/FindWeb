package com.shu.find.enums;

/**
 * Author:ShiQi
 * Date:2019/12/18-21:51
 */
public enum NotificationStatusEnum {
    UNREAD(0), READ(1);
    private int status;

    public int getStatus() {
        return status;
    }

    NotificationStatusEnum(int status) {
        this.status = status;
    }
}
