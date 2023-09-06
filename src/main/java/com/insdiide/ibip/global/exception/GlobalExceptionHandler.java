package com.insdiide.ibip.global.exception;

import com.insdiide.ibip.global.exception.code.ResultCode;
import com.insdiide.ibip.global.vo.ErrorVO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({CustomException.class})
    protected ResponseEntity<ErrorVO> handleCustomException(CustomException ex) {
        ex.printStackTrace();
        return new ResponseEntity(new ErrorVO(ex.getResultCode().getCode(), ex.getResultCode().getMsg()), HttpStatus.OK);
    }

    //기타 에러 핸들링
    @ExceptionHandler({Exception.class})
    protected ResponseEntity handleException(Exception ex) {
        ex.printStackTrace();
        return new ResponseEntity(new ErrorVO(ResultCode.ETC_ERROR.getCode(), ResultCode.ETC_ERROR.getMsg()), HttpStatus.OK);
    }
}
