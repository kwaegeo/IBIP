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

    public String createSession(String userId, String pwd) throws WebObjectsException {

        serverSession = factory.getIServerSession();
        System.out.println("이건처음이에용" + serverSession.getLogin());
        if(!(serverSession.getLogin().isEmpty())){
            serverSession.closeSession();
            System.out.println(serverSession);
        }
        serverSession.setServerName("localhost");
        serverSession.setServerPort(0);
        serverSession.setProjectName("MicroStrategy Tutorial");
        serverSession.setLogin(userId);
        serverSession.setPassword(pwd);
        serverSession.setLocaleID(1042);
        serverSession.setAuthMode(1);
        System.out.println("이건 둘째에용" + serverSession.getLogin());

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
                ex.printStackTrace();
                log.info("MSTR 세션을 생성하는 도중 문제가 발생하였습니다. ");
                throw new CustomException(ResultCode.MSTR_ETC_ERROR);
            }
        }

        return mstrSessionId;
    }


    public boolean userIsAlive(String mstrSessionId) {
        serverSession = factory.getIServerSession();
        //현재 살아있으면 false 죽어있으면 true
        serverSession.setSessionID(mstrSessionId);
        String usrSmgr = serverSession.saveState(0);
        serverSession.restoreState(usrSmgr);    //session 가져오기 (복원에 성공하면 true??)
        serverSession.setActive(); //이건 도대체 왜하는거여

        try {
            if (serverSession.isAlive()) { // 살아있으면
                //closeSession(userSession);
                return false; //false를 반환해?
            } else {
                closeSession(serverSession); //죽어있으면 연결을 끊어
            }
        } catch (WebObjectsException e) {
            log.info("WebObjectsException e >> userIsAlive");
        } catch (Exception e) {
            log.info("Exception e >> userIsAlive");
        }

        System.out.println(usrSmgr);
        System.out.println(mstrSessionId);
        System.out.println("이거맞는지?");

        return true; //연결을 끊고 true를 반환해?
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




}
