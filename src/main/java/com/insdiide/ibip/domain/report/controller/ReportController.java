package com.insdiide.ibip.domain.report.controller;

import com.insdiide.ibip.domain.prompt.vo2.PromptDataVO;
import com.insdiide.ibip.domain.prompt.vo2.PromptVO;
import com.insdiide.ibip.domain.prompt.vo2.PromptsVO;
import com.insdiide.ibip.domain.report.service.ReportService;
import com.insdiide.ibip.domain.report.vo.ReportVO;
import com.insdiide.ibip.global.exception.CustomException;
import com.insdiide.ibip.global.utils.ComUtils;
import com.microstrategy.web.objects.WebObjectsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    public ReportVO getReport(@RequestParam(name = "reportId")String reportId, HttpServletRequest request) throws WebObjectsException {

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        try {
            comUtils.sessionCheck(mstrSessionId);
        } catch (CustomException ex) {
            throw ex;
        }

        //2. Prompt가 있는지 확인하면서 걍 return 때려버려
        ReportVO reportInfo = reportService.getPromptData(mstrSessionId, reportId);

        System.out.println(reportInfo);
       return reportInfo;
    }

    @PostMapping("/getReportURL")
    @ResponseBody
    public String getReportURL(@RequestBody ReportVO reportInfo, HttpServletRequest request){
        System.out.println(reportInfo);
        
        
        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        try {
            comUtils.sessionCheck(mstrSessionId);
        } catch (CustomException ex) {
            throw ex;
        }

        //2. prompt 확인 (이걸 해 말아?) 하는게 맞긴 해

        //3. xml 생성
        String promptXml = reportService.getPromptXml(reportInfo);

        //3.5 usrSmgr 가져오기
        String usrSmgr = reportService.getUsrSmgr();

        //4. URL 생성
        String reportURL = reportService.getReportURL(reportInfo, promptXml, usrSmgr);
        //5. return
        return reportURL;
    }

}
