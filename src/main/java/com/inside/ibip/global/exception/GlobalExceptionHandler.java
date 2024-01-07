package com.inside.ibip.global.exception;

import com.inside.ibip.global.exception.code.ResultCode;
import com.inside.ibip.global.vo.ErrorVO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({CustomException.class})
    protected ResponseEntity<ErrorVO> handleCustomException(CustomException ex) throws IOException {
        ex.printStackTrace();

        // Ajax 요청이 아닌 Get요청의 경우 + 세션이 없는 경우 main login 페이지로 리다이렉트
        if(ex.getResultCode().getCode() == ResultCode.MSTR_NO_SESSION.getCode() && !(isAjaxRequest(ex.getRequest()))){
            ex.getResponse().sendRedirect("/auth/login");
        }

        return new ResponseEntity(new ErrorVO(ex.getResultCode().getCode(), ex.getResultCode().getMsg()), HttpStatus.OK);
    }

    //기타 에러 핸들링
    @ExceptionHandler({Exception.class})
    protected ResponseEntity handleException(Exception ex) {
        ex.printStackTrace();
        return new ResponseEntity(new ErrorVO(ResultCode.ETC_ERROR.getCode(), ResultCode.ETC_ERROR.getMsg()), HttpStatus.OK);
    }

    private boolean isAjaxRequest(HttpServletRequest request){
        String requestHeader = request.getHeader("X-Requested-With");
        return "XMLHttpRequest".equals(requestHeader);
    }

}
