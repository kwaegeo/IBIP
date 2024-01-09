package com.inside.ibip.domain.guest.main.controller;

import com.inside.ibip.domain.guest.folder.vo.TreeVO;
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
import java.util.ArrayList;
import java.util.List;
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
        System.out.println(userId);

        String usrSmgr = comUtils.getUsrSmgr();
        ReportVO reportInfo = mainService.getDashboardReport(userId);
        String dashboardURL = mainService.getDashboardURL(reportInfo, usrSmgr);
        System.out.println(dashboardURL);
        return dashboardURL;
    }

    @GetMapping("/searchReport")
    @ResponseBody
    public SearchVO searchReport(@RequestParam String searchKeyword, HttpServletRequest request, Model model, HttpServletResponse response) throws WebObjectsException {

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

        SearchVO search = mainService.searchReport(searchKeyword);
        return search;
    }

    @GetMapping("/tree")
    public String getTree(){
    return "/tree";
    }

    @GetMapping("/root_data")
    @ResponseBody
    public List<TreeVO> getRootData() {
        System.out.println("루트");
        List<TreeVO> subList = new ArrayList<>();
        subList.add(new TreeVO("EDDA24FD432866CE918CADA08F0A63C6", "이도현 개발", "#", 4, true));
        subList.add(new TreeVO("F025A94B4C03B6DCEE0F5D9DA825DA67", "나무 개발", "#", 4, true));
        subList.add(new TreeVO("032A5E114A59D28267BDD8B6D9E58B22", "바람 개발", "#", 4, true));
        subList.add(new TreeVO("92ADD0F84D07AC532AD03BA0F92A836B", "태양 개발", "#", 4, true));


        // 로트 노드의 데이터를 반환하는 로직
        return subList;
    }

    @GetMapping("/child_data")
    @ResponseBody
    public List<TreeVO> getChildData(@RequestParam String id) {
        System.out.println("차일드");
        // 특정 노드의 하위 노드 데이터를 반환하는 로직
        // 여기에서는 id에 따라 다른 데이터를 반환하도록 예시로 작성
        List<TreeVO> subList = new ArrayList<>();
        subList.add(new TreeVO("ss", "a 개발", "EDDA24FD432866CE918CADA08F0A63C6", 4, true));
        subList.add(new TreeVO("ccc", "b 개발", "EDDA24FD432866CE918CADA08F0A63C6", 4));
        subList.add(new TreeVO("ss", "c 개발", "EDDA24FD432866CE918CADA08F0A63C6", 4));
        subList.add(new TreeVO("zz", "d 개발", "EDDA24FD432866CE918CADA08F0A63C6", 4));
        System.out.println(subList);
        return subList;
    }

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
