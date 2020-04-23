package com.shu.find.exception;

/**
 * Author:ShiQi
 * Date:2019/12/13-2:37
 * 准备封装异常处理，避免每次都重写同样的错误信息
 */
public interface ICustomizeErrorCode {
    String getMessage();

    Integer getCode();
}
