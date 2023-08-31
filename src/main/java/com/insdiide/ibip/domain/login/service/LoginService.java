package com.insdiide.ibip.domain.login.service;

import com.microstrategy.web.objects.WebIServerSession;
import com.microstrategy.web.objects.WebObjectsException;
import com.microstrategy.web.objects.WebObjectsFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import java.util.HashMap;
import java.util.Map;

@Service
public class LoginService {

    public Map<String, String> validateHandling(Errors errors){
        Map<String, String> validatorResult = new HashMap<>();

        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }

        return validatorResult;
    }

    public String createMstrSession(){

        //2. MSTR 세션 생성 (생성이 되지 않을 시 return)

        WebObjectsFactory factory = WebObjectsFactory.getInstance();
        WebIServerSession serverSession = factory.getIServerSession();

        // Set up session properties
        serverSession.setServerName("192.168.70.245"); // Should be replaced with the name of an Intelligence Server
        serverSession.setServerPort(0);
        serverSession.setProjectName("MicroStrategy Tutorial"); // Project where session is created
        serverSession.setLocaleID(1042); // 한국어
        serverSession.setAuthMode(1);
        serverSession.setLogin("test1"); // User ID
        serverSession.setPassword("123456789"); // Password
        String mstrSessionId = "";
        try {
            mstrSessionId = serverSession.getSessionID();
            System.out.println("nSession created with ID: "+ mstrSessionId);
            System.out.println("Session State: "+ serverSession.saveState(0));
        } catch (WebObjectsException ex) {
            System.out.println( "Error creating session:" + ex.getMessage());
            System.out.println("===============================");
            System.out.println(ex.getLocalizedMessage());
            System.out.println(ex.getErrorCode());

            if(ex.getLocalizedMessage() == "로그인 실패"){
                System.out.println("아이디 또는 비밀번호를 잘못 입력했습니다.");
                return "아이디 또는 비밀번호를 잘못 입력했습니다.";
            }
            else if(ex.getErrorCode() == -2147207418){
                System.out.println("서버 이름이 잘못되었습니다 연결을 다시 확인해주세요.");
                return "서버 이름이 잘못되었습니다. 연결을 다시 확인해주세요.";
            }

            else if (ex.getErrorCode() == -2147209151) {
                System.out.println("프로젝트 명이 잘못되었습니다.");
                return "프로젝트 명이 잘못되었습니다.";
            }
            else {
                System.out.println("MSTR 세션을 생성하는 도중 문제가 발생하였습니다. ");
                return "MSTR 세션을 생성하는 도중 문제가 발생하였습니다.";
            }
        }


        // Return session
        StringBuilder urlSB = new StringBuilder();
        urlSB.append("http").append("://").append("localhost:8090"); //Web Server name and port
        urlSB.append("/MicroStrategy/servlet/mstrWeb");
        urlSB.append("?server=").append("localhost"); //I Server name
        // urlSB.append("&port=0");
        urlSB.append("&project=").append("MicroStrategy+Tutorial"); // Project name
        urlSB.append("&evt=").append(4001);
        urlSB.append("&reportID=").append("F94F34354152449D3359579FA7A12EF7"); //Report ID
        urlSB.append("&reportViewMode=").append(1);
        urlSB.append("&src=mstrWeb.").append("reportNoHeaderNoFooterNoPath").append(".").append(4001);
        urlSB.append("&usrSmgr=").append(serverSession.saveState(0));

        System.out.println(urlSB.toString()); // Final URL is printed to console.

        return mstrSessionId;
    }


}
