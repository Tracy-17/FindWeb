package com.shu.find.exception;

import com.alibaba.fastjson.JSON;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import com.shu.find.dto.ResultDTO;
import com.shu.find.exception.CustomizeErrorCode;
import com.shu.find.exception.CustomizeException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Author:ShiQi
 * Date:2020/5/17-16:23
 */
@ControllerAdvice
public class CustomizeExceptionHandler {
    @ExceptionHandler(Exception.class)
    ModelAndView handle(Throwable e, Model model, HttpServletRequest request, HttpServletResponse response){
        String contentType = request.getContentType();
        if("qpplication/json".equals(contentType)){
            ResultDTO resultDTO;
            //返回json
            if(e instanceof CustomizeException){
                resultDTO= ResultDTO.errorOf((CustomizeException)e);
            }else {
                resultDTO= ResultDTO.errorOf(CustomizeErrorCode.SYSTEM_ERROR);
            }
            try {
                response.setContentType("application/json");
                response.setStatus(200);
                response.setCharacterEncoding("utf-8");
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(resultDTO));
                writer.close();
            } catch (IOException ioe) {
                //之后会统一捕获异常
//                ex.printStackTrace();
            }
            return null;
        }else{
            //错误页面跳转
            if(e instanceof CustomizeException){
                model.addAttribute("message",e.getMessage());
            }else {
                model.addAttribute("message", CustomizeErrorCode.SYSTEM_ERROR.getMessage());
            }
            return new ModelAndView("error");
        }
    }
}