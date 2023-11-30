package com.insdiide.ibip.domain.template.controller;

import com.insdiide.ibip.domain.template.TemplateVO;
import com.insdiide.ibip.domain.template.service.TemplateService;
import com.insdiide.ibip.global.exception.code.ResultCode;
import com.insdiide.ibip.global.vo.ResVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class TemplateController {

    @Autowired
    private TemplateService templateService;

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
}
