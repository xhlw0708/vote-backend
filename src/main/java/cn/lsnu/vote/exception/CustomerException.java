package cn.lsnu.vote.exception;

import lombok.Getter;

/**
 * 自定义业务异常
 */
@Getter
public class CustomerException extends RuntimeException{

    private String code;

    public CustomerException(String code,String msg){
        super(msg);
        this.code = code;
    }
}
