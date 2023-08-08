package com.insdiide.ibip.domain.login.controller;

import com.insdiide.ibip.domain.login.service.LoginService;
import com.insdiide.ibip.domain.login.vo.LoginVO;
import com.insdiide.ibip.domain.login.vo.ResponseVO;
import com.microstrategy.web.objects.WebIServerSession;
import com.microstrategy.web.objects.WebObjectsException;
import com.microstrategy.web.objects.WebObjectsFactory;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Map;

@Log4j2
@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    @GetMapping("/login")
    public String getLoginPage(HttpServletRequest request){

        /**
         * TODO: Session값을 통해 이미 로그인을 한 사용자는 메인페이지로 redirect 시킬 것
         *
         * **/
        log.info("login 페이지 접속");
        return "/login/login";

    }

    @PostMapping("/login")
    public String loginProc(@Valid LoginVO loginVO, Errors errors, Model model, HttpServletRequest request) {
        log.info("로그인 ID : " + loginVO.getId());
        log.info("로그인 PWD : " + loginVO.getPwd());

        //1. 유효성 검사 (필수 값이 없을 시)
        if (errors.hasErrors()) {
            model.addAttribute("loginVO", loginVO);

            //1-1. 핸들링 함수 호출 (에러 키와, 에러메세지 들고옴)
            Map<String, String> validatorResult = loginService.validateHandling(errors);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }
            //1-2. 모델에 담은 후 로그인 페이지로 return
            return "/login/login";
        }

        //2. MSTR 세션 생성
        String mstrSessionId = loginService.createMstrSession();
        HttpSession httpSession = request.getSession();
        httpSession.setAttribute("mstrSessionId", mstrSessionId);


        return "redirect:/main";
    }


}
