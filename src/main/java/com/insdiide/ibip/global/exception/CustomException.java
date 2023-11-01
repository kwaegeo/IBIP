package com.insdiide.ibip.global.exception;

import com.insdiide.ibip.global.exception.code.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@AllArgsConstructor
@Getter
public class CustomException extends RuntimeException{
    private ResultCode resultCode;

    private HttpServletRequest request;

    private HttpServletResponse response;

    public CustomException(ResultCode resultCode){
        this.resultCode = resultCode;
    };

}
