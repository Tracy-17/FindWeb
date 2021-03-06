package com.shu.find.enums;

/**
 * Author:ShiQi
 * Date:2019/12/13-19:52
 */
public enum CommentTypeEnum {
    CONTENT(1),
    COMMENT(2),
    ANSWER(3);/*精选回答*/

    private Integer type;

    public static boolean isExist(Integer type) {
        for (CommentTypeEnum commentTypeEnum : CommentTypeEnum.values()) {
            if (commentTypeEnum.getType() == type) {
                return true;
            }
        }
        return false;
    }

    public Integer getType() {
        return type;
    }

    CommentTypeEnum(Integer type) {
        this.type = type;
    }
}
