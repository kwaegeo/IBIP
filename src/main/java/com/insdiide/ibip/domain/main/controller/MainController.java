package com.insdiide.ibip.domain.main.controller;

import com.insdiide.ibip.domain.main.service.MainService;
import com.insdiide.ibip.domain.main.vo.SideBarItemVO;
import com.insdiide.ibip.domain.main.vo.UserInfoVO;
import com.insdiide.ibip.global.exception.CustomException;
import com.insdiide.ibip.global.utils.ComUtils;
import com.microstrategy.web.objects.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@Controller
public class MainController {

    @Autowired
    private MainService mainService;

    @Autowired
    private ComUtils comUtils;


@GetMapping("/sample")
public String sample(){
    return "/index222";
}


@GetMapping("/main")
public String getMainPage(HttpServletRequest request, Model model, HttpServletResponse response) throws WebObjectsException {

    //1. 사용자 세션 (MSTR) 유효성 검사
    //2. 좌측 리스트 메뉴 가져오기 (폴더명, 폴더ID)
    //3. 사용자 정보 가져오기 (이름, 계정ID)

    HttpSession httpSession = request.getSession(true);
    String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

    //1. 세션 체크
    try{
        comUtils.sessionCheck(mstrSessionId, request, response);
    }catch(CustomException ex){
        throw ex;
    }

    //2. 좌측 리스트 가져오기 (하위 목록 가져오는 함수)
    SideBarItemVO sideBarItems = mainService.getSideBarItems(mstrSessionId);
    model.addAttribute("sideBarItem", sideBarItems);

    //3. 사용자 정보 가져오기
    UserInfoVO userInfo = mainService.getUserInfo(mstrSessionId);
    model.addAttribute("userInfo", userInfo);

    return "/index";
}

    @PostMapping("/getHistoryListURL")
    @ResponseBody
    public String getHistoryList(@RequestBody String userId, HttpServletRequest request, Model model, HttpServletResponse response) throws WebObjectsException {

        //1. 사용자 세션 (MSTR) 유효성 검사
        //2. 사용내역목록 URL 생성 반환

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        try{
            comUtils.sessionCheck(mstrSessionId, request, response);
        }catch(CustomException ex){
            throw ex;
        }

        String usrSmgr = comUtils.getUsrSmgr();
        String historyListURL = mainService.getHistoryURL(usrSmgr);

        return historyListURL;
    }

    @PostMapping("/getSubscriptionURL")
    @ResponseBody
    public String getSubscription(@RequestBody String userId, HttpServletRequest request, Model model, HttpServletResponse response) throws WebObjectsException {

        //1. 사용자 세션 (MSTR) 유효성 검사
        //2. 나의 구독물 URL 생성 반환

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        try{
            comUtils.sessionCheck(mstrSessionId, request, response);
        }catch(CustomException ex){
            throw ex;
        }

        String usrSmgr = comUtils.getUsrSmgr();
        String subscriptionURL = mainService.getSubscriptionURL(usrSmgr);

        return subscriptionURL;
    }

    @GetMapping("/searchReport")
    @ResponseBody
    public String searchReport(@RequestParam String searchKeyword, HttpServletRequest request, Model model, HttpServletResponse response) throws WebObjectsException {

        //1. 사용자 세션 (MSTR) 유효성 검사
        //2. 나의 구독물 URL 생성 반환

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        try{
            comUtils.sessionCheck(mstrSessionId, request, response);
        }catch(CustomException ex){
            throw ex;
        }

        System.out.println(searchKeyword);

        mainService.searchReport();
        return "zz";
    }



}
