package com.insdiide.ibip.domain.login.controller;

import com.insdiide.ibip.domain.login.vo.LoginVO;
import com.insdiide.ibip.domain.login.vo.ResponseVO;
import com.microstrategy.web.objects.WebIServerSession;
import com.microstrategy.web.objects.WebObjectsFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String getLoginPage(HttpServletRequest request){

        /**
         * TODO: Session값을 통해 이미 로그인을 한 사용자는 메인페이지로 redirect 시킬 것
         *
         * **/
        return "/login/login";

    }

    @PostMapping("/login")
    public String login(@RequestBody @Valid LoginVO loginVO, BindingResult bindingResult, HttpServletRequest request){
        System.out.println("로그인 ID : " + loginVO.getId());
        System.out.println("로그인 PWD : " + loginVO.getPwd());

        if(bindingResult.hasErrors()){
            System.out.println(bindingResult.getFieldErrors());
        }

        //응답 VO 생성
        ResponseVO responseVO = new ResponseVO();

        WebObjectsFactory factory = WebObjectsFactory.getInstance();
        WebIServerSession serverSession = factory.getIServerSession();

        // Set up session properties
        serverSession.setServerName("192.168.70.245"); // Should be replaced with the name of an Intelligence Server
        serverSession.setServerPort(0);
        serverSession.setProjectID("MicroStrategy Tutorial"); // Project where session is created
        serverSession.setLocaleID(1042); // 한국어
        serverSession.setAuthMode(1);
        serverSession.setLogin("administrator"); // User ID
        serverSession.setPassword(""); // Password
        return "/aa";
    }



}
