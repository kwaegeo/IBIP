package com.insdiide.ibip.global.utils;

import com.insdiide.ibip.domain.main.vo.UserInfoVO;
import com.insdiide.ibip.global.exception.CustomException;
import com.insdiide.ibip.global.exception.code.ResultCode;
import com.insdiide.ibip.global.mstr.MstrObject;
import com.insdiide.ibip.global.mstr.MstrSession;
import com.microstrategy.web.objects.WebObjectsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class ComUtils {

    @Autowired
    private MstrObject mstrObject;

    public void sessionCheck(String mstrSessionId, HttpServletRequest request, HttpServletResponse response){
        if(mstrObject.userIsAlive(mstrSessionId)){
            throw new CustomException(ResultCode.MSTR_NO_SESSION, request, response);
        }
    }
    public String getUsrSmgr(){
        return mstrObject.getUsrSmgr();
    }

    public void closeSession(){
        try {
            mstrObject.serverSession.closeSession();
        } catch (WebObjectsException e) {
            e.printStackTrace();
        }
    }

    public UserInfoVO getUserInfo(String mstrSessionId) throws WebObjectsException {
        mstrObject.setSession(mstrSessionId);
        UserInfoVO userInfo = mstrObject.getUserInfo();
        return userInfo;
    }

    public static boolean isNullOrEmpty(String str){
        if (str == null){
            return true;
        }
        if (str.length() == 0){
            return true;
        }

        return false;
    }
}
