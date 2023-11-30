package com.insdiide.ibip.domain.report.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.insdiide.ibip.domain.prompt.vo2.PromptDataVO;
import com.insdiide.ibip.domain.prompt.vo2.PromptVO;
import com.insdiide.ibip.domain.prompt.vo2.PromptsVO;
import com.insdiide.ibip.domain.report.service.ReportService;
import com.insdiide.ibip.domain.report.vo.ReportVO;
import com.insdiide.ibip.domain.template.ReqTemplateVO;
import com.insdiide.ibip.global.exception.CustomException;
import com.insdiide.ibip.global.exception.code.ResultCode;
import com.insdiide.ibip.global.utils.ComUtils;
import com.microstrategy.web.objects.WebObjectsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private ComUtils comUtils;


    /**
     * 1. 세션체크
     * 2. Prompt가 있는지 확인 = 2-1. 없을 시 그대로 retrun 있을 시 Prompt 조회한 다음 return
     *
     * **/

    @GetMapping("/getReport")
    @ResponseBody
    public ReportVO getReport(@RequestParam(name = "reportId")String reportId, @RequestParam(name = "documentType")String documentType, HttpServletRequest request, HttpServletResponse response) throws WebObjectsException {

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        try {
            comUtils.sessionCheck(mstrSessionId, request, response);
        } catch (CustomException ex) {
            throw ex;
        }

        System.out.println(documentType);

        //2. Prompt가 있는지 확인하면서 걍 return 때려버려
        ReportVO reportInfo = reportService.getPromptData(mstrSessionId, reportId, documentType);

        System.out.println(reportInfo);
       return reportInfo;
    }

    @PostMapping("/getReportURL")
    @ResponseBody
    public String getReportURL(@RequestBody ReportVO reportInfo, HttpServletRequest request, HttpServletResponse response){
        System.out.println(reportInfo);
        
        
        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        try {
            comUtils.sessionCheck(mstrSessionId, request, response);
        } catch (CustomException ex) {
            throw ex;
        }

        //2. xml 생성
        String promptXml = reportService.getPromptXml(reportInfo);

        //2.5 usrSmgr 가져오기
        String usrSmgr = reportService.getUsrSmgr();

        //3. URL 생성
        String reportURL = reportService.getReportURL(reportInfo, promptXml, usrSmgr);
        //4. return
        return reportURL;
    }

}
