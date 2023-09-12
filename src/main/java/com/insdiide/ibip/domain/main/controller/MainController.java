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
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Controller
public class MainController {

    @Autowired
    private MainService mainService;

    @Autowired
    private ComUtils comUtils;


@GetMapping("/sample")
public String sample(){
    return "/indexOri";
}


@GetMapping("/main")
public String getMainPage(HttpServletRequest request, Model model) throws WebObjectsException {

    //1. 사용자 세션 (MSTR) 유효성 검사
    //2. 좌측 리스트 메뉴 가져오기 (폴더명, 폴더ID)
    //3. 사용자 정보 가져오기 (이름, 계정ID)

    HttpSession httpSession = request.getSession(true);
    String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

    //1. 세션 체크
    try{
        comUtils.sessionCheck(mstrSessionId);
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

}
