package com.shu.find.enums;

/**
 * Author:ShiQi
 * Date:2020/5/12-0:14
 */
public enum MyRelationTypeEnum {
    FOLLOW(1),
    FAN(2);
    private Integer type;

    public static boolean isExist(Integer type) {
        for (MyRelationTypeEnum myRelationTypeEnum : MyRelationTypeEnum.values()) {
            if (myRelationTypeEnum.getType() == type) {
                return true;
            }
        }
        return false;
    }

    public Integer getType() {
        return type;
    }

    MyRelationTypeEnum(Integer type) {
        this.type = type;
    }
}
