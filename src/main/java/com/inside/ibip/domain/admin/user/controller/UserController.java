package com.inside.ibip.domain.admin.user.controller;

import com.inside.ibip.domain.admin.user.service.UserService;
import com.inside.ibip.domain.admin.user.vo.UserVO;
import com.inside.ibip.global.exception.CustomException;
import com.inside.ibip.global.utils.ComUtils;
import com.inside.ibip.global.vo.ResVO;
import com.microstrategy.web.objects.WebObjectsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ComUtils comUtils;

    @PostMapping("/getUserList")
    @ResponseBody
    public List<UserVO> getUserList(HttpServletRequest request, HttpServletResponse response) throws WebObjectsException {

        //1. 사용자 세션 (MSTR) 유효성 검사

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        try{
            comUtils.sessionCheck(mstrSessionId, request, response);
        }catch(CustomException ex){
            throw ex;
        }

        List<UserVO> userList = userService.getUserList();

        return userList;
    }


    @GetMapping("/userInfo")
    public String userInfo(@RequestParam String userId, HttpServletRequest request, HttpServletResponse response, Model model) throws WebObjectsException {
        System.out.println(userId);
        //1. 사용자 세션 (MSTR) 유효성 검사

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        try{
            comUtils.sessionCheck(mstrSessionId, request, response);
        }catch(CustomException ex){
            throw ex;
        }

        UserVO userInfo = userService.getUserInfo(userId);

        model.addAttribute("userInfo", userInfo);
        return "/admin/user/info";
    }

    @PostMapping("/userManageAssignment")
    @ResponseBody
    public ResVO groupAssign(@RequestBody UserVO userInfo, HttpServletRequest request, HttpServletResponse response){
        //1. 사용자 세션 (MSTR) 유효성 검사

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        try{
            comUtils.sessionCheck(mstrSessionId, request, response);
        }catch(CustomException ex){
            throw ex;
        }

        System.out.println(userInfo);
        ResVO result = userService.assign(userInfo);
        return result;
    }

    @PostMapping("/resetPassword")
    @ResponseBody
    public ResVO resetPassword(@RequestBody Map<String, String> userInfo,HttpServletRequest request, HttpServletResponse response){
        //1. 사용자 세션 (MSTR) 유효성 검사
        String userId = userInfo.get("userId");

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        try{
            comUtils.sessionCheck(mstrSessionId, request, response);
        }catch(CustomException ex){
            throw ex;
        }

        ResVO result = userService.resetPassword(userId);

        return result;
    }

    @PostMapping("/enableAccount")
    @ResponseBody
    public ResVO enableAccount(@RequestBody UserVO userInfo,HttpServletRequest request, HttpServletResponse response){

        System.out.println(userInfo);

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        try{
            comUtils.sessionCheck(mstrSessionId, request, response);
        }catch(CustomException ex){
            throw ex;
        }

        ResVO result = userService.enableAccount(userInfo);

        return result;
    }

    @GetMapping("/userAdd")
    public String userAdd(HttpServletRequest request, HttpServletResponse response){
        return "/admin/user/add";
    }
    @PostMapping("/userAddProc")
    @ResponseBody
    public ResVO userAddProc(@RequestBody UserVO userInfo, HttpServletRequest request, HttpServletResponse response){

        System.out.println(userInfo);

        //1. 사용자 세션 (MSTR) 유효성 검사
        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        try{
            comUtils.sessionCheck(mstrSessionId, request, response);
        }catch(CustomException ex){
            throw ex;
        }

        ResVO result = userService.addUser(userInfo);
        return result;

    }

    @PostMapping("/userModifyProc")
    @ResponseBody
    public ResVO userModifyProc(@RequestBody UserVO userInfo, HttpServletRequest request, HttpServletResponse response){

        System.out.println(userInfo);

        //1. 사용자 세션 (MSTR) 유효성 검사
        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        try{
            comUtils.sessionCheck(mstrSessionId, request, response);
        }catch(CustomException ex){
            throw ex;
        }

        ResVO result = userService.modifyUser(userInfo);
        return result;
    }

    @PostMapping("/userDelProc")
    @ResponseBody
    public ResVO userDelProc(@RequestBody Map<String,String> userInfo, HttpServletRequest request, HttpServletResponse response) throws WebObjectsException {
        //1. 사용자 세션 (MSTR) 유효성 검사
        String userId = userInfo.get("userId");

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        try{
            comUtils.sessionCheck(mstrSessionId, request, response);
        }catch(CustomException ex){
            throw ex;
        }

        ResVO result = userService.delUser(userId);
        return result;
    }



}
