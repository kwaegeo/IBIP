package com.inside.ibip.domain.guest.auth.controller;

import com.inside.ibip.domain.guest.auth.service.AuthService;
import com.inside.ibip.domain.guest.auth.vo.LoginVO;
import com.inside.ibip.global.exception.code.ResultCode;
import com.inside.ibip.global.utils.ComUtils;
import com.inside.ibip.global.vo.ResVO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @FileName     : AuthController.java
 * @Date         : 2023.12.01
 * @Author       : 이도현
 * @Description  : 인증 Controller, 로그인과 로그아웃 처리
 * @History
 * =======================================================
 *   DATE			AUTHOR			NOTE
 * =======================================================
 *   2023.12.01     이도현         최초작성
 *
 */
@Log4j2
@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private ComUtils comUtils;

    /**
     * 로그인 페이지 조회
     * @Method Name   : getLogin
     * @Date / Author : 2023.12.01  이도현
     * @param request request 객체
     * @return 로그인 페이지
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     *  1. 사용자 세션 (MSTR) 유효성 검사 이후 세션이
     *  있을 경우에 main 페이지로
     *  없을 경우에 login 페이지로
     */
    @GetMapping("/login")
    public String getLogin(HttpServletRequest request){

        //1. 사용자 세션 (MSTR) 유효성 검사
        HttpSession httpSession = request.getSession(true);

        //1-1. 사용자 세션이 있을 경우 main URL로 return
        if(httpSession.getAttribute("mstrSessionId") != null){
            if(!comUtils.sessionGlobalCheck((String) httpSession.getAttribute("mstrSessionId"))){
                return "redirect:/main/guest";
            }
        }

        //1-2. 사용자 세션이 없을 경우 login 페이지 return
        return "/auth/login";
    }


    /**
     * 로그인 페이지 처리
     * @Method Name   : processLogin
     * @Date / Author : 2023.12.01  이도현
     * @param loginInfo 로그인 정보 VO
     * @param errors 에러 파싱 변수
     * @param request request 객체
     * @return 성공 코드, 메세지
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     *  1. 매개변수 유효성 검사
     *  2. MSTR 세션 생성
     *  3. HTTP 세션에 MSTR 세션 정보 저장
     */
    @PostMapping("/login")
    @ResponseBody
    public ResVO processLogin(@Valid @RequestBody LoginVO loginInfo, Errors errors, HttpServletRequest request) {

        log.info("사용자 로그인 정보 [id] : " + loginInfo.getId());
        log.info("사용자 로그인 정보 [pwd] : " + loginInfo.getPwd());

        //1. 유효성 검사 (id) - 대부분의 초기 관리자[Administrator]의 경우 비밀번호를 사용하지 않기 때문에 비밀번호 유효성 검사는 보류.
        if(errors.hasFieldErrors("id")){
            return new ResVO(ResultCode.ID_MISSING);
        }

        //2. MSTR 세션 생성
        String mstrSessionId = "";

        mstrSessionId = authService.CreateMstrSession(loginInfo.getId(), loginInfo.getPwd());

        //3. HttpSession에 mstrSessionId 저장
        HttpSession httpSession = request.getSession();
        httpSession.setAttribute("mstrSessionId", mstrSessionId);

        //4. 응답
        return new ResVO(ResultCode.SUCCESS);
    }

    /**
     * 로그아웃 처리
     * @Method Name   : processLogout
     * @Date / Author : 2023.12.01  이도현
     * @param request request 객체
     * @param response response 객체
     * @return 성공 코드, 메세지
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     *  1. 사용자 세션 (MSTR) 유효성 검사
     *  2. MSTR 세션 제거
     *  3. HTTP 세션 제거
     */
    @PostMapping("/logout")
    @ResponseBody
    public ResVO processLogout(HttpServletRequest request, HttpServletResponse response) {

        //1. 세션 제거
        HttpSession httpSession = request.getSession(true);
        ResVO result = null;

        if(httpSession != null){

            String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

            //1-1. 세션 체크
            comUtils.sessionCheck(mstrSessionId, request, response);

            //1-2. Mstr 세션 제거
            result = authService.logoutSession();

            //1-3. HTTP 세션 제거
            httpSession.invalidate();
        }

        //2. 응답
        return result;
    }

}

