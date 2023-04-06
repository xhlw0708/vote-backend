package cn.lsnu.vote.exception;

import cn.lsnu.vote.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 全局异常处理器
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {


    /**
     * 进行异常处理方法
     * @return
     */
    @ExceptionHandler(CustomerException.class)
    public Result customerException(CustomerException ex){
        return Result.error(ex.getCode(), ex.getMessage());
    }

}
