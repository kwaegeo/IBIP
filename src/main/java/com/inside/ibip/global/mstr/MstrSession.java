package com.inside.ibip.global.mstr;

import com.inside.ibip.global.exception.CustomException;
import com.inside.ibip.global.exception.code.ResultCode;
import com.microstrategy.web.objects.WebIServerSession;
import com.microstrategy.web.objects.WebObjectsException;
import com.microstrategy.web.objects.WebObjectsFactory;
import com.microstrategy.webapi.EnumDSSXMLApplicationType;
import com.microstrategy.webapi.EnumDSSXMLAuthModes;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class MstrSession {

    /**
     * MSTR Object factory
     */
    protected WebObjectsFactory factory	= WebObjectsFactory.getInstance();

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

    public String createSession(String userId, String pwd) {

        serverSession = factory.getIServerSessionList().newSession();

        serverSession.setServerName(serverName);
        serverSession.setServerPort(0);
        serverSession.setProjectName(mstrProjectName);
        serverSession.setLogin(userId);
        serverSession.setPassword(pwd);
        serverSession.setLocaleID(1042);
        serverSession.setAuthMode(EnumDSSXMLAuthModes.DssXmlAuthStandard);
        serverSession.setApplicationType(EnumDSSXMLApplicationType.DssXmlApplicationDSSWeb);

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
            else if(ex.getErrorCode() == -2147479445){
                log.info("MSTR 리소스 엑세스에 대한 권한이 없습니다.");
                throw new CustomException(ResultCode.INVALID_POLICY);
            }
            else {
                System.out.println(ex.getErrorCode());
                System.out.println(ex.getMessage());
                ex.printStackTrace();
                log.info("MSTR 세션을 생성하는 도중 문제가 발생하였습니다. ");
                throw new CustomException(ResultCode.MSTR_ETC_ERROR);
            }
        }

        return mstrSessionId;
    }

    public void setSession(String mstrSessionId){
        serverSession	= factory.getIServerSession();
        serverSession.restoreState(mstrSessionId);
        try {
            serverSession.refresh();
        }catch (WebObjectsException woe){
            log.error("세션 새로고침 중 에러 발생 [Error msg]: " + woe.getMessage());
            throw new CustomException(ResultCode.MSTR_ETC_ERROR);
        }
    }

    public boolean userIsAlive(String mstrSessionId) {
        try {

            if (serverSession.isAlive()) {
                return false;
            } else {
                closeSession(serverSession);
            }
        } catch (WebObjectsException e) {
            log.info("WebObjectsException e >> userIsAlive");
        } catch (Exception e) {
            log.info("Exception e >> userIsAlive");
        }

        return true;
    }


    public void closeSession(WebIServerSession serverSession) {
        try {
            serverSession.closeSession();
        }
        catch (WebObjectsException e) {
            log.info("exception.webObjectsException >> closeSession");
        }
        catch (Exception e) {
            log.info("exception.Exception >> closeSession");
        }
        log.info("Session closed.");
    }

    public void logoutSession(){
        try {
            serverSession.closeSession();
        } catch (WebObjectsException e) {
            throw new CustomException(ResultCode.MSTR_ETC_ERROR);
        }
    }

}
