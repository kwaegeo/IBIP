package com.insdiide.ibip.global.mstr;

import com.insdiide.ibip.global.exception.CustomException;
import com.insdiide.ibip.global.exception.code.ResultCode;
import com.microstrategy.web.objects.WebIServerSession;
import com.microstrategy.web.objects.WebObjectsException;
import com.microstrategy.web.objects.WebObjectsFactory;
import com.microstrategy.webapi.EnumDSSXMLApplicationType;
import com.microstrategy.webapi.MSTRWebAPIException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class MstrSession {

    /**
     * MSTR Object factory
     */
    protected WebObjectsFactory factory		= WebObjectsFactory.getInstance();

    /**
     * MSTR 세션 개체
     */
    public WebIServerSession serverSession;

    @Value("${mstrServerName}")
    private String serverName;

    @Value("${mstrProjectName}")
    private String mstrProjectName;

    /**
     * MSTR mstrSessionId
     */
    public String mstrSessionId;

    public String createSession(String userId, String pwd){

        serverSession = factory.getIServerSession();
        serverSession.setServerName(serverName);
        serverSession.setServerPort(0);
        serverSession.setProjectName(mstrProjectName);
        serverSession.setLogin(userId);
        serverSession.setPassword(pwd);
        serverSession.setLocaleID(1042);
        serverSession.setAuthMode(1);
        try {
            mstrSessionId = serverSession.getSessionID();
        }catch (WebObjectsException ex){
            if(ex.getErrorCode() == -2147216959){
                log.info("입력하신 아이디와 패스워드에 해당하는 사용자가 없습니다.");
                throw new CustomException(ResultCode.INCORRECT_USERNAME_OR_PASSWORD);
            }
            else if(ex.getErrorCode() == -2147207418){
                log.info("서버 이름이 잘못되었습니다 연결을 다시 확인해주세요.");
                throw new CustomException(ResultCode.UNREGISTERED_SERVER_NAME);
            }
            else if (ex.getErrorCode() == -2147209151) {
                log.info("프로젝트 명이 잘못되었습니다.");
                throw new CustomException(ResultCode.UNREGISTERED_PROJECT);
            }
            else {
                log.info("MSTR 세션을 생성하는 도중 문제가 발생하였습니다. ");
                throw new CustomException(ResultCode.MSTR_ETC_ERROR);
            }
        }

        return mstrSessionId;
    }

}
