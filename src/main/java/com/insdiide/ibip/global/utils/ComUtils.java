package com.insdiide.ibip.global.utils;

import com.insdiide.ibip.global.exception.CustomException;
import com.insdiide.ibip.global.exception.code.ResultCode;
import com.insdiide.ibip.global.mstr.MstrSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComUtils {

    @Autowired
    private MstrSession mstrSession;

    public void sessionCheck(String mstrSessionId){
        if(mstrSession.userIsAlive(mstrSessionId)){
            throw new CustomException(ResultCode.MSTR_NO_SESSION);
        };
    }


}
