package com.insdiide.ibip.domain.main.controller;

import com.insdiide.ibip.domain.folder.vo.EntityVO;
import com.insdiide.ibip.domain.main.service.MainService;
import com.insdiide.ibip.domain.main.vo.SearchVO;
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
import java.util.ArrayList;
import java.util.List;


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

//    return "/newIndex";
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
    public List<EntityVO> getRootData() {
        System.out.println("루트");
        List<EntityVO> subList = new ArrayList<>();
        subList.add(new EntityVO("EDDA24FD432866CE918CADA08F0A63C6", "이도현 개발", "#", 4, true));
        subList.add(new EntityVO("F025A94B4C03B6DCEE0F5D9DA825DA67", "나무 개발", "#", 4, true));
        subList.add(new EntityVO("032A5E114A59D28267BDD8B6D9E58B22", "바람 개발", "#", 4, true));
        subList.add(new EntityVO("92ADD0F84D07AC532AD03BA0F92A836B", "태양 개발", "#", 4, true));


        // 로트 노드의 데이터를 반환하는 로직
        return subList;
    }

    @GetMapping("/child_data")
    @ResponseBody
    public List<EntityVO> getChildData(@RequestParam String id) {
        System.out.println("차일드");
        // 특정 노드의 하위 노드 데이터를 반환하는 로직
        // 여기에서는 id에 따라 다른 데이터를 반환하도록 예시로 작성
        List<EntityVO> subList = new ArrayList<>();
        subList.add(new EntityVO("ss", "a 개발", "EDDA24FD432866CE918CADA08F0A63C6", 4, true));
        subList.add(new EntityVO("ccc", "b 개발", "EDDA24FD432866CE918CADA08F0A63C6", 4));
        subList.add(new EntityVO("ss", "c 개발", "EDDA24FD432866CE918CADA08F0A63C6", 4));
        subList.add(new EntityVO("zz", "d 개발", "EDDA24FD432866CE918CADA08F0A63C6", 4));
        System.out.println(subList);
        return subList;
    }

    @GetMapping("/adminPage")
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
        UserInfoVO userInfo = mainService.getUserInfo(mstrSessionId);
        model.addAttribute("userInfo", userInfo);

        return "/admin/group/admin";
    }

}
