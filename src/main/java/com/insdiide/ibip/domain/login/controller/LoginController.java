package com.insdiide.ibip.domain.login.controller;

import com.insdiide.ibip.domain.login.service.LoginService;
import com.insdiide.ibip.domain.login.vo.LoginVO;
import com.insdiide.ibip.global.exception.CustomException;
import com.insdiide.ibip.global.exception.code.ResultCode;
import com.insdiide.ibip.global.utils.ComUtils;
import com.insdiide.ibip.global.vo.ResVO;
import com.microstrategy.web.objects.WebObjectsException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Log4j2
@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private ComUtils comUtils;

    @GetMapping("/login")
    public String getLoginPage(HttpServletRequest request){

        /**
         * TODO: Session값을 통해 이미 로그인을 한 사용자는 메인페이지로 redirect 시킬 것
         *
         * **/
        log.info("login 페이지 접속");
        return "/login/login";

    }

    @GetMapping("/a")
    public String a(HttpServletRequest request){

        loginService.createMstrSession2();
        return "/login/login";

    }

    
    // login 시 세션에 사용자 정보도 담기
    @PostMapping("/login")
    @ResponseBody
    public ResVO loginProc(@Valid @RequestBody LoginVO loginVO, Errors errors, Model model, HttpServletRequest request) {
        log.info("로그인 ID : " + loginVO.getId());
        log.info("로그인 PWD : " + loginVO.getPwd());

        //1. 유효성 검사 (id)
        if(errors.hasFieldErrors("id")){
            return new ResVO(ResultCode.ID_MISSING);
        }
        //1-1. 유효성 검사 (pwd)
//        else if (errors.hasFieldErrors("pwd")) {
//            return new ResVO(ResultCode.PASSWORD_MISSING);
//        }

        //2. MSTR 세션 생성
        String mstrSessionId = "";

        try{
            mstrSessionId = loginService.CreateMstrSession(loginVO.getId(), loginVO.getPwd());
        }catch(CustomException ex){
            throw ex;
        } catch (WebObjectsException e) {
            throw new RuntimeException(e);
        }

        //3. HttpSession에 mstrSessionId 저장
        HttpSession httpSession = request.getSession();
        httpSession.setAttribute("mstrSessionId", mstrSessionId);

        //4. 응답
        return new ResVO(ResultCode.SUCCESS);
    }

    @PostMapping("/logout")
    @ResponseBody
    public ResVO logoutProc(Model model, HttpServletRequest request, HttpServletResponse response) {


        //Session 제거 (MSTR Session 제거 => Web Session 제거)
        HttpSession httpSession = request.getSession(true);
        if(httpSession != null){
            String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");
            //MSTR 세션 제거
            try{
                comUtils.sessionCheck(mstrSessionId, request, response);
                comUtils.closeSession();
            }catch(CustomException ex){
                throw ex;
            }
            httpSession.invalidate();

        }

        //4. 응답
        return new ResVO(ResultCode.SUCCESS);
    }


}


// 1. 필수값 입력 안했을 때
// 2. MSTR 세션 생성에 실패했을 때

