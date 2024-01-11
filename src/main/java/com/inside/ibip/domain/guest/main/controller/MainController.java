package com.inside.ibip.domain.guest.main.controller;

import com.inside.ibip.domain.guest.main.vo.SearchVO;
import com.inside.ibip.domain.guest.main.vo.UserInfoVO;
import com.inside.ibip.domain.guest.main.service.MainService;
import com.inside.ibip.domain.guest.report.vo.ReportVO;
import com.inside.ibip.global.exception.CustomException;
import com.inside.ibip.global.utils.ComUtils;
import com.microstrategy.web.objects.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @FileName     : MainController.java
 * @Date         : 2023.12.01
 * @Author       : 이도현
 * @Description  : 메인 Controller, 메인 페이지 처리, 사용자 정보 및 대시보드 조회
 * @History
 * =======================================================
 *   DATE			AUTHOR			NOTE
 * =======================================================
 *   2023.12.01     이도현         최초작성
 *
 */
@Slf4j
@Controller
@RequestMapping("/main")
public class MainController {

    @Autowired
    private MainService mainService;

    @Autowired
    private ComUtils comUtils;


    /**
     * 메인 페이지 조회 (사용자)
     * @Method Name   : getGuestMain
     * @Date / Author : 2023.12.01  이도현
     * @param request request 객체
     * @param response response 객체
     * @param model model 객체
     * @return 메인 페이지 (사용자)
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     *  1. 사용자 세션 (MSTR) 유효성 검사
     *  2. 좌측 리스트 메뉴 가져오기 (폴더명, 폴더ID)
     *  3. 사용자 정보 가져오기 (이름, 계정ID)
     */
    @GetMapping("/guest")
    public String getGuestMain(HttpServletRequest request, HttpServletResponse response, Model model){

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        comUtils.sessionCheck(mstrSessionId, request, response);

        //1-1. mstrObject 세션 정보 주입
        mainService.setMstrSession(mstrSessionId);

        //2. 사용자 정보 가져오기
        UserInfoVO userInfo = mainService.getUserInfo();
        model.addAttribute("userInfo", userInfo);

        httpSession.setAttribute("userId", userInfo.getUserId());
        return "/index";
    }

    /**
     * 사용내역 목록 URL 조회
     * @Method Name   : getHistoryList
     * @Date / Author : 2023.12.01  이도현
     * @param request request 객체
     * @param response response 객체
     * @return 사용내역 목록 URL
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     *  1. 사용자 세션 (MSTR) 유효성 검사
     *  2. usrSmgr 조회
     *  3. historyList 기본 웹 URL 조합 후 반환
     */
    @PostMapping("/historyList")
    @ResponseBody
    public String getHistoryList(HttpServletRequest request, HttpServletResponse response) {

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        comUtils.sessionCheck(mstrSessionId, request, response);

        String usrSmgr = comUtils.getUsrSmgr();

        String historyListURL = mainService.getHistoryURL(usrSmgr);

        return historyListURL;
    }

    /**
     * 내 구독물 URL 조회
     * @Method Name   : getSubscription
     * @Date / Author : 2023.12.01  이도현
     * @param request request 객체
     * @param response response 객체
     * @return 사용내역 목록 URL
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     *  1. 사용자 세션 (MSTR) 유효성 검사
     *  2. usrSmgr 조회
     *  3. subscription 기본 웹 URL 조합 후 반환
     */
    @PostMapping("/subscription")
    @ResponseBody
    public String getSubscription(HttpServletRequest request, HttpServletResponse response) throws WebObjectsException {


        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        comUtils.sessionCheck(mstrSessionId, request, response);

        String usrSmgr = comUtils.getUsrSmgr();
        String subscriptionURL = mainService.getSubscriptionURL(usrSmgr);

        return subscriptionURL;
    }

    @PostMapping("/getDashboardURL")
    @ResponseBody
    public String getDashboard(@RequestBody Map<String,String> userInfo, HttpServletRequest request, Model model, HttpServletResponse response) throws WebObjectsException {

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
        String userId = userInfo.get("userId");

        String usrSmgr = comUtils.getUsrSmgr();
        ReportVO reportInfo = mainService.getDashboardReport(userId);
        String dashboardURL = mainService.getDashboardURL(reportInfo, usrSmgr);
        System.out.println(dashboardURL);
        return dashboardURL;
    }

    /**
     * 문서 검색 (리포트, 다큐먼트)
     * @Method Name   : search
     * @Date / Author : 2023.12.01  이도현
     * @param searchKeyword 검색 키워드
     * @param request request 객체
     * @param response response 객체
     * @return 사용내역 목록 URL
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     *  1. 사용자 세션 (MSTR) 유효성 검사
     *  2. 검색 키워드로 MSTR 내부의 문서 검색
     */
    @GetMapping("/search")
    @ResponseBody
    public SearchVO search(@RequestParam String searchKeyword, HttpServletRequest request, HttpServletResponse response) {


        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        comUtils.sessionCheck(mstrSessionId, request, response);

        SearchVO search = mainService.search(searchKeyword);
        return search;
    }


    /**
     * 메인 페이지 조회 (사용자)
     * @Method Name   : getGuestMain
     * @Date / Author : 2023.12.01  이도현
     * @param request request 객체
     * @param response response 객체
     * @param model model 객체
     * @return 메인 페이지 (사용자)
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     *  1. 사용자 세션 (MSTR) 유효성 검사
     *  2. 좌측 리스트 메뉴 가져오기 (폴더명, 폴더ID)
     *  3. 사용자 정보 가져오기 (이름, 계정ID)
     */
    @GetMapping("/admin")
    public String adminPage( HttpServletRequest request, HttpServletResponse response, Model model) throws WebObjectsException {
        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        try{
            comUtils.sessionCheck(mstrSessionId, request, response);
        }catch(CustomException ex){
            throw ex;
        }

        //3. 사용자 정보 가져오기
        UserInfoVO userInfo = mainService.getUserInfo();
        model.addAttribute("userInfo", userInfo);

        return "/admin/admin";
    }

}
