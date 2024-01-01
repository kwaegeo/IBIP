package com.insdiide.ibip.domain.admin.license.controller;

import com.insdiide.ibip.domain.admin.group.service.GroupService;
import com.insdiide.ibip.domain.admin.license.service.LicenseService;
import com.insdiide.ibip.domain.admin.license.vo.LicenseVO;
import com.insdiide.ibip.domain.admin.user.vo.UserVO;
import com.insdiide.ibip.global.exception.CustomException;
import com.insdiide.ibip.global.utils.ComUtils;
import com.microstrategy.web.objects.WebObjectsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class LicenseController {

    @Autowired
    private LicenseService licenseService;

    @Autowired
    private ComUtils comUtils;

    @PostMapping("/getLicenseList")
    @ResponseBody
    public List<LicenseVO> getLicenseList(HttpServletRequest request, HttpServletResponse response) throws WebObjectsException {

        //1. 사용자 세션 (MSTR) 유효성 검사

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        try{
            comUtils.sessionCheck(mstrSessionId, request, response);
        }catch(CustomException ex){
            throw ex;
        }

        List<LicenseVO> licenseList = licenseService.getLicenseList();

        return licenseList;
    }

    @GetMapping("/licenseInfo")
    public String LicenseInfo(@RequestParam int licenseType, HttpServletRequest request, HttpServletResponse response, Model model) throws WebObjectsException {
        System.out.println(licenseType);
        //1. 사용자 세션 (MSTR) 유효성 검사

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        try{
            comUtils.sessionCheck(mstrSessionId, request, response);
        }catch(CustomException ex){
            throw ex;
        }

        LicenseVO licenseInfo = licenseService.getLicenseInfo(licenseType);

        model.addAttribute("licenseInfo", licenseInfo);
        return "/admin/license/info";
    }

}
