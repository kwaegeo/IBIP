package com.insdiide.ibip.global.exception;

import com.insdiide.ibip.global.exception.code.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomException extends RuntimeException{
    private final ResultCode resultCode;
}
