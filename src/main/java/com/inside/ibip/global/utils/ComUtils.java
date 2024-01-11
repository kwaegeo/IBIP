package com.inside.ibip.global.utils;

import com.inside.ibip.domain.guest.main.vo.UserInfoVO;
import com.inside.ibip.global.exception.CustomException;
import com.inside.ibip.global.exception.code.ResultCode;
import com.inside.ibip.global.mstr.MstrObject;
import com.inside.ibip.global.mstr.MstrSession;
import com.microstrategy.web.objects.WebObjectsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class ComUtils {

    @Autowired
    private MstrObject mstrObject;

    @Autowired MstrSession mstrSession;

    public void sessionCheck(String mstrSessionId, HttpServletRequest request, HttpServletResponse response){
        if(mstrSession.userIsAlive(mstrSessionId)){
            throw new CustomException(ResultCode.MSTR_NO_SESSION, request, response);
        }
    }

    public boolean sessionGlobalCheck(String mstrSessionId){
        return mstrSession.userIsAlive(mstrSessionId);
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

    public UserInfoVO getUserInfo() {
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
