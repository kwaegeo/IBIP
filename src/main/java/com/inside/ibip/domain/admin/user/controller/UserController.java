package com.inside.ibip.domain.admin.user.controller;

import com.inside.ibip.domain.admin.user.service.UserService;
import com.inside.ibip.domain.admin.user.vo.UserVO;
import com.inside.ibip.global.exception.CustomException;
import com.inside.ibip.global.utils.ComUtils;
import com.inside.ibip.global.vo.ResVO;
import com.microstrategy.web.objects.WebObjectsException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;


/**
 * @FileName     : UserController.java
 * @Date         : 2023.12.01
 * @Author       : 이도현
 * @Description  : 사용자 Controller, 사용자 리스트 조회, 정보 조회 등
 * @History
 * =======================================================
 *   DATE			AUTHOR			NOTE
 * =======================================================
 *   2023.12.01     이도현         최초작성
 *
 */
@Log4j2
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ComUtils comUtils;

    /**
     * 사용자 리스트 조회
     * @Method Name   : getUserList
     * @Date / Author : 2023.12.01  이도현
     * @param request request 객체
     * @param response response 객체
     * @return 사용자 리스트 객체
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    @PostMapping("/get/list")
    @ResponseBody
    public List<UserVO> getUserList(HttpServletRequest request, HttpServletResponse response){

        //1. 사용자 세션 (MSTR) 유효성 검사

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        comUtils.sessionCheck(mstrSessionId, request, response);

        //2. 사용자 리스트 조회
        List<UserVO> userList = userService.getUserList();

        return userList;
    }

    /**
     * 그룹 정보 조회
     * @Method Name   : getUserInfo
     * @Date / Author : 2023.12.01  이도현
     * @param userId 조회할 사용자의 Id
     * @param request request 객체
     * @param response response 객체
     * @param model model 객체
     * @return 사용자 객체
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    @GetMapping("/get/info")
    public String getUserInfo(@RequestParam String userId, HttpServletRequest request, HttpServletResponse response, Model model){

        //1. 사용자 세션 (MSTR) 유효성 검사
        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        comUtils.sessionCheck(mstrSessionId, request, response);

        //2. 사용자 정보 조회
        UserVO userInfo = userService.getUserInfo(userId);

        model.addAttribute("userInfo", userInfo);
        return "/admin/user/info";
    }

    /**
     * 사용자 그룹 할당
     * @Method Name   : userAssign
     * @Date / Author : 2023.12.01  이도현
     * @param userInfo user 정보 객체
     * @param request request 객체
     * @param response response 객체
     * @return 성공 유무
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    @PostMapping("/assign")
    @ResponseBody
    public ResVO userAssign(@RequestBody UserVO userInfo, HttpServletRequest request, HttpServletResponse response){
        //1. 사용자 세션 (MSTR) 유효성 검사

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        comUtils.sessionCheck(mstrSessionId, request, response);

        ResVO result = userService.assign(userInfo);
        return result;
    }

    /**
     * 비밀번호 초기화
     * @Method Name   : resetPassword
     * @Date / Author : 2023.12.01  이도현
     * @param userInfo user 정보 객체
     * @param request request 객체
     * @param response response 객체
     * @return 성공 유무
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    @PostMapping("/reset/password")
    @ResponseBody
    public ResVO resetPassword(@RequestBody Map<String, String> userInfo,HttpServletRequest request, HttpServletResponse response){
        //1. 사용자 세션 (MSTR) 유효성 검사
        String userId = userInfo.get("userId");

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        comUtils.sessionCheck(mstrSessionId, request, response);

        ResVO result = userService.resetPassword(userId);

        return result;
    }

    /**
     * 계정 활성화/비활성화
     * @Method Name   : enableAccount
     * @Date / Author : 2023.12.01  이도현
     * @param userInfo user 정보 객체
     * @param request request 객체
     * @param response response 객체
     * @return 성공 유무
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    @PostMapping("/enable/status")
    @ResponseBody
    public ResVO enableAccount(@RequestBody UserVO userInfo,HttpServletRequest request, HttpServletResponse response){

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        comUtils.sessionCheck(mstrSessionId, request, response);

        ResVO result = userService.enableAccount(userInfo);

        return result;
    }

    /**
     * 사용자 생성 페이지 조회
     * @Method Name   : userAdd
     * @Date / Author : 2023.12.01  이도현
     * @param request request 객체
     * @param response response 객체
     * @return 사용자 생성 페이지
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    @GetMapping("/add")
    public String userAdd(HttpServletRequest request, HttpServletResponse response){

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        comUtils.sessionCheck(mstrSessionId, request, response);

        return "/admin/user/add";
    }

    /**
     * 사용자 생성
     * @Method Name   : userAddProc
     * @Date / Author : 2023.12.01  이도현
     * @param userInfo group 정보 객체
     * @param request request 객체
     * @param response response 객체
     * @return 성공 유무
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    @PostMapping("/add")
    @ResponseBody
    public ResVO userAddProc(@RequestBody UserVO userInfo, HttpServletRequest request, HttpServletResponse response){

        //1. 사용자 세션 (MSTR) 유효성 검사
        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        comUtils.sessionCheck(mstrSessionId, request, response);

        ResVO result = userService.addUser(userInfo);
        return result;

    }

    /**
     * 사용자 수정
     * @Method Name   : userModifyProc
     * @Date / Author : 2023.12.01  이도현
     * @param userInfo user 정보 객체
     * @param request request 객체
     * @param response response 객체
     * @return 성공 유무
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    @PostMapping("/modify")
    @ResponseBody
    public ResVO userModifyProc(@RequestBody UserVO userInfo, HttpServletRequest request, HttpServletResponse response){

        //1. 사용자 세션 (MSTR) 유효성 검사
        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        comUtils.sessionCheck(mstrSessionId, request, response);

        ResVO result = userService.modifyUser(userInfo);
        return result;
    }

    /**
     * 사용자 삭제
     * @Method Name   : groupDelProc
     * @Date / Author : 2023.12.01  이도현
     * @param userInfo userInfo 정보 객체
     * @param request request 객체
     * @param response response 객체
     * @return 성공 유무
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    @PostMapping("/delete")
    @ResponseBody
    public ResVO userDelProc(@RequestBody Map<String,String> userInfo, HttpServletRequest request, HttpServletResponse response){
        //1. 사용자 세션 (MSTR) 유효성 검사
        String userId = userInfo.get("userId");

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        comUtils.sessionCheck(mstrSessionId, request, response);

        ResVO result = userService.deleteUser(userId);
        return result;
    }



}
