package com.shu.find.enums;

/**
 * Author:ShiQi
 * Date:2020/5/1-20:48
 */
public enum ContentTypeEnum {
    QUESTION(0),
    ARTICLE(1),
    FINISH(2);/*已被解答的问题*/
    private Integer type;

    public static boolean isExist(Integer type) {
        for (ContentTypeEnum contentTypeEnum : ContentTypeEnum.values()) {
            if (contentTypeEnum.getType() == type) {
                return true;
            }
        }
        return false;
    }

    public Integer getType() {
        return type;
    }

    ContentTypeEnum(Integer type) {
        this.type = type;
    }
}
