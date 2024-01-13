package com.inside.ibip.domain.guest.report.controller;

import com.inside.ibip.domain.guest.main.vo.UserInfoVO;
import com.inside.ibip.domain.guest.report.service.ReportService;
import com.inside.ibip.domain.guest.report.vo.ReportVO;
import com.inside.ibip.global.utils.ComUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @FileName     : ReportController.java
 * @Date         : 2023.12.01
 * @Author       : 이도현
 * @Description  : 리포트 Controller, 리포트 정보 및 프롬프트 정보 조회
 * @History
 * =======================================================
 *   DATE			AUTHOR			NOTE
 * =======================================================
 *   2023.12.01     이도현         최초작성
 *
 */
@Log4j2
@Controller
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private ComUtils comUtils;


    /**
     * 리포트 정보 조회
     * @Method Name   : getReport
     * @Date / Author : 2023.12.01  이도현
     * @param reportId 리포트의 ID
     * @param documentType 문서 타입
     * @param request request 객체
     * @param response response 객체
     * @return 리포트 객체
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    @GetMapping("/get")
    @ResponseBody
    public ReportVO getReport(@RequestParam(name = "reportId")String reportId, @RequestParam(name = "documentType")String documentType, HttpServletRequest request, HttpServletResponse response){

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        comUtils.sessionCheck(mstrSessionId, request, response);

        UserInfoVO userInfo = reportService.getUserInfo();

        //2. 리포트 정보 조회
        ReportVO reportInfo = reportService.getReportInfo(reportId, documentType, userInfo);

        return reportInfo;
    }

    /**
     * 리포트 URL 조회 (리포트 실행)
     * @Method Name   : getReport
     * @Date / Author : 2023.12.01  이도현
     * @param reportInfo 리포트 정보 객체
     * @param request request 객체
     * @param response response 객체
     * @return 리포트 객체
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    @PostMapping("/get/URL")
    @ResponseBody
    public String getReportURL(@RequestBody ReportVO reportInfo, HttpServletRequest request, HttpServletResponse response){

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        comUtils.sessionCheck(mstrSessionId, request, response);

        //2. Prompt XML 생성
        String promptXml = reportService.getPromptXml(reportInfo);

        //2.1 usrSmgr 가져오기
        String usrSmgr = reportService.getUsrSmgr();

        //3. 리포트 실행 URL 생성
        String reportURL = reportService.getReportURL(reportInfo, promptXml, usrSmgr);

        //4. 응답
        return reportURL;
    }

}
