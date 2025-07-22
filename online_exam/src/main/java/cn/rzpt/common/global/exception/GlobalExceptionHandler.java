package cn.rzpt.common.global.exception;


import cn.rzpt.common.global.result.DataResult;
import cn.rzpt.common.global.result.DataResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Optional;


@Slf4j
@Component
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public DataResult<?> ExceptionHandler(Exception e) {
        if (e instanceof GlobalException ex) {
            log.error("系统发生了错误---->{}", Optional.ofNullable(ex.getDataResultCodeEnum().getMessage())
                    .orElse(ex.getMessage()));
            return DataResult.fail(ex.getDataResultCodeEnum().getCode(),ex.getDataResultCodeEnum().getMessage());
        } else if (e instanceof IllegalArgumentException) {
            log.info("参数校验异常 --- > {}", e.getMessage());
            return DataResult.fail(DataResultCodeEnum.FAIL.getCode(), e.getMessage());
        }else if(e instanceof  NoResourceFoundException) {
            log.error("资源未找到---->{}", e.getMessage());
            return DataResult.fail(DataResultCodeEnum.FAIL.getCode(),e.getMessage());
        }else {
            log.error("系统发生了未知错误---->{}", e.getMessage(),e);
            return DataResult.fail(DataResultCodeEnum.FAIL);
        }

    }
}
