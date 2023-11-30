package com.insdiide.ibip.domain.template.controller;

import com.insdiide.ibip.domain.main.vo.UserInfoVO;
import com.insdiide.ibip.domain.report.vo.ReportVO;
import com.insdiide.ibip.domain.template.ReqTemplateVO;
import com.insdiide.ibip.domain.template.TemplateVO;
import com.insdiide.ibip.domain.template.service.TemplateService;
import com.insdiide.ibip.global.exception.CustomException;
import com.insdiide.ibip.global.exception.code.ResultCode;
import com.insdiide.ibip.global.utils.ComUtils;
import com.insdiide.ibip.global.vo.ResVO;
import com.microstrategy.web.objects.WebObjectsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class TemplateController {

    @Autowired
    private TemplateService templateService;

    @Autowired
    private ComUtils comUtils;

    @PostMapping("/checkTemplate")
    @ResponseBody
    public ResVO checkTemplate(@RequestBody Map<String,String> template){

        String templateName = template.get("templateName");

        ResVO response = templateService.checkTemplate(templateName);

        return response;
    }

    @PostMapping("/addTemplate")
    @ResponseBody
    public ResVO addTemplate(@RequestBody TemplateVO template){

        System.out.println(template);

        return new ResVO(ResultCode.SUCCESS);

    }

    @PostMapping("/openTemplateAdd")
    public String openTemplateAdd(@RequestBody ReportVO reportInfo, HttpServletRequest request, HttpServletResponse response, Model model) throws WebObjectsException {

        System.out.println(reportInfo);

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        try {
            comUtils.sessionCheck(mstrSessionId, request, response);
        } catch (CustomException ex) {
            throw ex;
        }

        UserInfoVO userInfo = comUtils.getUserInfo(mstrSessionId);

        model.addAttribute("reportInfo", reportInfo);
        model.addAttribute("userInfo", userInfo);
        return "/template/templateAdd";
    }

    @PostMapping("/createTemplate")
    @ResponseBody
    public String createTemplate(@RequestBody ReqTemplateVO templateInfo, HttpServletRequest request){
        System.out.println(templateInfo);

        templateService.createTemplate(templateInfo);

        return "zz";
    }


}
