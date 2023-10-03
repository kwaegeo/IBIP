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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

}
