package com.insdiide.ibip.domain.login.controller;

import com.insdiide.ibip.domain.login.vo.LoginVO;
import com.insdiide.ibip.domain.login.vo.ResponseVO;
import com.microstrategy.web.objects.WebIServerSession;
import com.microstrategy.web.objects.WebObjectsFactory;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Log4j2
@Controller
public class LoginController {

    @GetMapping("/login")
    public String getLoginPage(HttpServletRequest request){

        /**
         * TODO: Session값을 통해 이미 로그인을 한 사용자는 메인페이지로 redirect 시킬 것
         *
         * **/
        log.info("응?");
        return "/login/login";

    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> login(@RequestBody @Valid LoginVO loginVO, BindingResult bindingResult, HttpServletRequest request){
        log.info("로그인 ID : " + loginVO.getId());
        log.info("로그인 PWD : " + loginVO.getPwd());

        if(bindingResult.hasErrors()){
            log.info(bindingResult.getFieldErrors().get(0).getDefaultMessage());
            log.info(bindingResult.getFieldErrors().get(0).getCode());
        }

        //응답 VO 생성
        ResponseVO responseVO = ResponseVO.builder().code("S00").msg("굳").build();

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
        return new ResponseEntity<>(responseVO, HttpStatus.NOT_FOUND);
    }



}
