package com.shu.find.exception;

/**
 * Author:ShiQi
 * Date:2019/12/13-2:39
 */
public enum CustomizeErrorCode implements ICustomizeErrorCode {

    QUESTION_NOT_FOUND(2001, "寻找的问题不在啦QAQ"),
    TARGET_PARAM_NOT_FOUND(2002, "未选中任何问题或评论进行回复"),
    NO_LOGIN(2003, "当前操作需要登录，请登录后重试"),
    SYSTEM_ERROR(2004, "服务器溜了"),
    TYPE_PARAM_WRONG(2005, "评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006, "回复的评论不存在"),
    CONTENT_IS_EMPTY(2007, "输入内容不能为空"),
    READ_NOTIFICATION_FAIL(2008, "非法操作"),
    NOTIFICATION_NOT_FOUND(2009, "消息不见啦~~~"),
    FOLLOW_DONT_EXIST(2010,"这条关注不存在");


    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    private Integer code;
    private String message;

    CustomizeErrorCode(Integer code, String message) {
        this.message = message;
        this.code = code;
    }
}
