package com.inside.ibip.domain.guest.template.controller;

import com.inside.ibip.domain.guest.main.vo.UserInfoVO;
import com.inside.ibip.domain.guest.prompt.vo.ObjectVO;
import com.inside.ibip.domain.guest.report.vo.ReportVO;
import com.inside.ibip.domain.guest.template.TemplateVO;
import com.inside.ibip.domain.guest.template.service.TemplateService;
import com.inside.ibip.domain.guest.template.ReqTemplateVO;
import com.inside.ibip.global.exception.CustomException;
import com.inside.ibip.global.exception.code.ResultCode;
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
    public ResVO createTemplate(@RequestBody ReqTemplateVO templateInfo, HttpServletRequest request){
        System.out.println(templateInfo);

        templateService.createTemplate(templateInfo);

        return new ResVO(ResultCode.SUCCESS);
    }

    @GetMapping("/openTemplateList")
    public String openTemplateList(@RequestParam String reportId, String userId, HttpServletRequest request, HttpServletResponse response, Model model) throws WebObjectsException {

        System.out.println(reportId);
        System.out.println(userId);

        List<TemplateVO> templates = templateService.selectTemplate(reportId, userId);
        model.addAttribute("templates", templates);

        return "/template/templateList";
    }


    @PostMapping("/getTemplate")
    @ResponseBody
    public List<ObjectVO> getTemplate(@RequestBody Map<String, String> template,  HttpServletRequest request, HttpServletResponse response){
        String templateId = template.get("templateId");
        System.out.println(templateId);
        List<ObjectVO> entities = templateService.getTemplate(templateId);
        return entities;
    }
}


