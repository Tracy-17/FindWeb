package com.shu.find.enums;

/**
 * Author:ShiQi
 * Date:2020/5/2-20:25
 */
public enum LikeTypeEnum {
    CONTENT(1),
    COMMENT(2);
    /*已被解答的问题*/
    private Integer type;

    public static boolean isExist(Integer type) {
        for (LikeTypeEnum likeTypeEnum : LikeTypeEnum.values()) {
            if (likeTypeEnum.getType() == type) {
                return true;
            }
        }
        return false;
    }

    public Integer getType() {
        return type;
    }

    LikeTypeEnum(Integer type) {
        this.type = type;
    }
}
