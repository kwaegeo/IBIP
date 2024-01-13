package com.inside.ibip.domain.admin.notice.controller;

import com.inside.ibip.domain.admin.notice.service.NoticeService;
import com.inside.ibip.domain.admin.notice.vo.NoticeVO;
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
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private ComUtils comUtils;

    @PostMapping("/getNoticeList")
    @ResponseBody
    public List<NoticeVO> getNoticeList(HttpServletRequest request, HttpServletResponse response) throws WebObjectsException {

        //1. 사용자 세션 (MSTR) 유효성 검사

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        try{
            comUtils.sessionCheck(mstrSessionId, request, response);
        }catch(CustomException ex){
            throw ex;
        }

        List<NoticeVO> noticeList = noticeService.getNoticeList();

        return noticeList;
    }

    @GetMapping("/noticeAdd")
    public String noticeAdd(HttpServletRequest request, HttpServletResponse response, Model model){

        return "/admin/notice/add";
    }

    @GetMapping("/noticeInfo")
    public String noticeInfo(@RequestParam String noticeId, HttpServletRequest request, HttpServletResponse response, Model model) throws WebObjectsException {
        System.out.println(noticeId);
        //1. 사용자 세션 (MSTR) 유효성 검사

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        try{
            comUtils.sessionCheck(mstrSessionId, request, response);
        }catch(CustomException ex){
            throw ex;
        }

        NoticeVO noticeInfo = noticeService.getNoticeInfo(noticeId);

        model.addAttribute("noticeInfo", noticeInfo);
        return "/admin/notice/info";
    }


    @PostMapping("/noticeAddProc")
    @ResponseBody
    public ResVO noticeAddProc(@RequestBody NoticeVO noticeInfo, HttpServletRequest request, HttpServletResponse response){

        //1. 사용자 세션 (MSTR) 유효성 검사

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");
        String userId = (String) httpSession.getAttribute("userId");

        //1. 세션 체크
        try{
            comUtils.sessionCheck(mstrSessionId, request, response);
        }catch(CustomException ex){
            throw ex;
        }

        ResVO result = noticeService.addNotice(noticeInfo, userId);
        return result;
    }


    @PostMapping("/noticeModifyProc")
    @ResponseBody
    public ResVO noticeModifyProc(@RequestBody NoticeVO noticeInfo, HttpServletRequest request, HttpServletResponse response){

        //1. 사용자 세션 (MSTR) 유효성 검사

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        try{
            comUtils.sessionCheck(mstrSessionId, request, response);
        }catch(CustomException ex){
            throw ex;
        }

        ResVO result = noticeService.modifyNotice(noticeInfo);
        return result;
    }

    @PostMapping("/noticeDelProc")
    @ResponseBody
    public ResVO noticeDelProc(@RequestBody Map<String,String> noticeInfo, HttpServletRequest request, HttpServletResponse response) throws WebObjectsException {
        //1. 사용자 세션 (MSTR) 유효성 검사
        String noticeId = noticeInfo.get("noticeId");

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        try{
            comUtils.sessionCheck(mstrSessionId, request, response);
        }catch(CustomException ex){
            throw ex;
        }

        ResVO result = noticeService.delNotice(noticeId);
        return result;
    }

    @GetMapping("/getNoticePopupList")
    @ResponseBody
    public List<String> noticePopupList(HttpServletRequest request, Model model, HttpServletResponse response) throws WebObjectsException {

        //1. 사용자 세션 (MSTR) 유효성 검사

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        comUtils.sessionCheck(mstrSessionId, request, response);

        List<String> popupList = noticeService.getNoticePopupList();

        return popupList;
    }

    @GetMapping("/getNoticePopup")
    public String getNoticePopup(@RequestParam String noticeId, HttpServletRequest request, HttpServletResponse response, Model model) throws WebObjectsException {
        System.out.println(noticeId);
        //1. 사용자 세션 (MSTR) 유효성 검사

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        try{
            comUtils.sessionCheck(mstrSessionId, request, response);
        }catch(CustomException ex){
            throw ex;
        }

        NoticeVO noticeInfo = noticeService.getNoticeInfo(noticeId);

        model.addAttribute("noticeInfo", noticeInfo);
        return "/admin/notice/popup";
    }

}
