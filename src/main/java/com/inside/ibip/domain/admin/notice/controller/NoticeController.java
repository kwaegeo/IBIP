package com.inside.ibip.domain.admin.notice.controller;

import com.inside.ibip.domain.admin.notice.service.NoticeService;
import com.inside.ibip.domain.admin.notice.vo.NoticeVO;
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
 * @FileName     : NoticeController.java
 * @Date         : 2023.12.01
 * @Author       : 이도현
 * @Description  : 공지사항 Controller, 공지사항 리스트 조회, 정보 조회 등
 * @History
 * =======================================================
 *   DATE			AUTHOR			NOTE
 * =======================================================
 *   2023.12.01     이도현         최초작성
 *
 */
@Log4j2
@Controller
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private ComUtils comUtils;

    /**
     * 공지사항 리스트 조회
     * @Method Name   : getNoticeList
     * @Date / Author : 2023.12.01  이도현
     * @param request request 객체
     * @param response response 객체
     * @return 그룹 리스트 객체
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    @PostMapping("/get/list")
    @ResponseBody
    public List<NoticeVO> getNoticeList(HttpServletRequest request, HttpServletResponse response){

        //1. 사용자 세션 (MSTR) 유효성 검사

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        comUtils.sessionCheck(mstrSessionId, request, response);

        List<NoticeVO> noticeList = noticeService.getNoticeList();

        return noticeList;
    }

    /**
     * 공지사항 생성 페이지 조회
     * @Method Name   : noticeAdd
     * @Date / Author : 2023.12.01  이도현
     * @param request request 객체
     * @param response response 객체
     * @return 공지사항 생성 페이지
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    @GetMapping("/add")
    public String noticeAdd(HttpServletRequest request, HttpServletResponse response){

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        comUtils.sessionCheck(mstrSessionId, request, response);

        return "/admin/notice/add";
    }

    /**
     * 공지사항 정보 조회
     * @Method Name   : getNoticeInfo
     * @Date / Author : 2023.12.01  이도현
     * @param noticeId 조회할 공지사항의 Id
     * @param request request 객체
     * @param response response 객체
     * @param model model 객체
     * @return 정보 페이지
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    @GetMapping("/get/info")
    public String getNoticeInfo(@RequestParam String noticeId, HttpServletRequest request, HttpServletResponse response, Model model){

        //1. 사용자 세션 (MSTR) 유효성 검사
        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        comUtils.sessionCheck(mstrSessionId, request, response);

        //2. 공지사항 정보 조회
        NoticeVO noticeInfo = noticeService.getNoticeInfo(noticeId);

        //3. model 객체에 삽입
        model.addAttribute("noticeInfo", noticeInfo);

        //4. 응답
        return "/admin/notice/info";
    }


    /**
     * 공지사항 생성
     * @Method Name   : noticeAddProc
     * @Date / Author : 2023.12.01  이도현
     * @param noticeInfo notice 정보 객체
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
    public ResVO noticeAddProc(@RequestBody NoticeVO noticeInfo, HttpServletRequest request, HttpServletResponse response){

        //1. 사용자 세션 (MSTR) 유효성 검사
        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //2 사용자 정보 파싱
        String userId = (String) httpSession.getAttribute("userId");

        //2-1. 세션 체크
        comUtils.sessionCheck(mstrSessionId, request, response);

        //2. 공지사항 추가
        ResVO result = noticeService.addNotice(noticeInfo, userId);
        return result;
    }

    /**
     * 공지사항 수정
     * @Method Name   : noticeModifyProc
     * @Date / Author : 2023.12.01  이도현
     * @param noticeInfo group 정보 객체
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
    public ResVO noticeModifyProc(@RequestBody NoticeVO noticeInfo, HttpServletRequest request, HttpServletResponse response){

        //1. 사용자 세션 (MSTR) 유효성 검사
        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        comUtils.sessionCheck(mstrSessionId, request, response);

        ResVO result = noticeService.modifyNotice(noticeInfo);
        return result;
    }

    /**
     * 공지사항 삭제
     * @Method Name   : noticeDelProc
     * @Date / Author : 2023.12.01  이도현
     * @param noticeInfo notice 정보 객체
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
    public ResVO noticeDelProc(@RequestBody Map<String,String> noticeInfo, HttpServletRequest request, HttpServletResponse response){
        //1. 사용자 세션 (MSTR) 유효성 검사
        String noticeId = noticeInfo.get("noticeId");

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        comUtils.sessionCheck(mstrSessionId, request, response);

        ResVO result = noticeService.deleteNotice(noticeId);
        return result;
    }

    /**
     * 공지사항 팝업 리스트 조회
     * @Method Name   : getNoticePopupList
     * @Date / Author : 2023.12.01  이도현
     * @param request request 객체
     * @param response response 객체
     * @return 성공 유무
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    @GetMapping("/get/popup/list")
    @ResponseBody
    public List<String> getNoticePopupList(HttpServletRequest request, HttpServletResponse response){

        //1. 사용자 세션 (MSTR) 유효성 검사
        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        comUtils.sessionCheck(mstrSessionId, request, response);

        List<String> popupList = noticeService.getNoticePopupList();

        return popupList;
    }

    /**
     * 공지사항 팝업 조회
     * @Method Name   : getNoticePopup
     * @Date / Author : 2023.12.01  이도현
     * @param noticeId group 정보 객체
     * @param request request 객체
     * @param response response 객체
     * @return 성공 유무
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    @GetMapping("/get/popup")
    public String getNoticePopup(@RequestParam String noticeId, HttpServletRequest request, HttpServletResponse response, Model model){

        //1. 사용자 세션 (MSTR) 유효성 검사
        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        comUtils.sessionCheck(mstrSessionId, request, response);

        //2. 공지사항 팝업 정보 조회
        NoticeVO noticeInfo = noticeService.getNoticeInfo(noticeId);

        //3. 응답
        model.addAttribute("noticeInfo", noticeInfo);
        return "/admin/notice/popup";
    }

}
