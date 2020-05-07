package com.shu.find.enums;

/**
 * Author:ShiQi
 * Date:2019/12/18-21:43
 */
public enum NotificationTypeEnum {
    REPLY_QUESTION(1, "评论了你的内容"),
    REPLY_COMMENT(2, "回复了你的评论"),
    FOLLOWED(3,"关注了你");
    private int type;
    private String name;

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    NotificationTypeEnum(int status, String name) {
        this.type = status;
        this.name = name;
    }

    //根据type返回其类型？
    public static String nameOfType(int type) {
        for (NotificationTypeEnum notificationTypeEnum : NotificationTypeEnum.values()) {
            if (notificationTypeEnum.getType() == type) {
                return notificationTypeEnum.getName();
            }
        }
        return "";
    }
}
