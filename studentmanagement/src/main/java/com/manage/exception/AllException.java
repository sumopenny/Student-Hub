package com.manage.exception;

import com.manage.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class AllException {
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        String message = e.getMessage();

        log.error("出错了！{}", message);
        //优化异常提示信息
        return Result.error(message != null ? message : "服务器异常");
    }
}
