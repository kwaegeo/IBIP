package com.inside.ibip.domain.guest.template.controller;

import com.inside.ibip.domain.guest.main.vo.UserInfoVO;
import com.inside.ibip.domain.guest.prompt.vo.ObjectVO;
import com.inside.ibip.domain.guest.report.vo.ReportVO;
import com.inside.ibip.domain.guest.template.vo.TemplateVO;
import com.inside.ibip.domain.guest.template.service.TemplateService;
import com.inside.ibip.domain.guest.template.vo.ReqTemplateVO;
import com.inside.ibip.global.exception.code.ResultCode;
import com.inside.ibip.global.utils.ComUtils;
import com.inside.ibip.global.vo.ResVO;
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
 * @FileName     : TemplateController.java
 * @Date         : 2023.12.01
 * @Author       : 이도현
 * @Description  : 템플릿 Controller, 리포트의 템플릿을 관리
 * @History
 * =======================================================
 *   DATE			AUTHOR			NOTE
 * =======================================================
 *   2023.12.01     이도현         최초작성
 *
 */
@Log4j2
@Controller
@RequestMapping("/template")
public class TemplateController {

    @Autowired
    private TemplateService templateService;

    @Autowired
    private ComUtils comUtils;

    /**
     * 템플릿 중복 확인
     * @Method Name   : checkTemplate
     * @Date / Author : 2023.12.01  이도현
     * @param template 템플릿 명
     * @param request request 객체
     * @param response response 객체
     * @return 사용 가능 유무
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    @PostMapping("/check")
    @ResponseBody
    public ResVO checkTemplate(@RequestBody Map<String,String> template, HttpServletRequest request, HttpServletResponse response){

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        comUtils.sessionCheck(mstrSessionId, request, response);

        String templateName = template.get("templateName");
        String reportId = template.get("reportId");

        ResVO result = templateService.checkTemplate(templateName, reportId);

        return result;
    }

    /**
     * 템플릿 생성 페이지 조회
     * @Method Name   : getTemplateCreate
     * @Date / Author : 2023.12.01  이도현
     * @param reportInfo 템플릿 명
     * @param request request 객체
     * @param response response 객체
     * @param model model
     * @return 템플릿 생성 페이지
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    @PostMapping("/get/create")
    public String getTemplateCreate(@RequestBody ReportVO reportInfo, HttpServletRequest request, HttpServletResponse response, Model model){

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        comUtils.sessionCheck(mstrSessionId, request, response);

        //2. 사용자 정보 조회
        UserInfoVO userInfo = comUtils.getUserInfo();

        //3. model에 삽입
        model.addAttribute("reportInfo", reportInfo);
        model.addAttribute("userInfo", userInfo);

        //4. 응답
        return "/template/create";
    }

    /**
     * 템플릿 생성
     * @Method Name   : createTemplate
     * @Date / Author : 2023.12.01  이도현
     * @param templateInfo 템플릿 명
     * @param request request 객체
     * @param response response 객체
     * @return 성공 유무
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    @PostMapping("/create")
    @ResponseBody
    public ResVO createTemplate(@RequestBody ReqTemplateVO templateInfo, HttpServletRequest request, HttpServletResponse response){

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        comUtils.sessionCheck(mstrSessionId, request, response);

        //2. 즐겨찾기에 추가
        templateService.createTemplate(templateInfo);

        return new ResVO(ResultCode.SUCCESS);
    }

    /**
     * 템플릿 생성
     * @Method Name   : createTemplate
     * @Date / Author : 2023.12.01  이도현
     * @param reportId 리포트 Id
     * @param userId 사용자 Id
     * @param request request 객체
     * @param response response 객체
     * @param model model 객체
     * @return 성공 유무
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    @GetMapping("/get/list")
    public String getTemplateList(@RequestParam String reportId, String userId, HttpServletRequest request, HttpServletResponse response, Model model) {

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        comUtils.sessionCheck(mstrSessionId, request, response);

        //2. 사용자 정보와 해당 리포트 정보로 Template 리스트 조회
        List<TemplateVO> templates = templateService.getTemplateList(reportId, userId);

        model.addAttribute("templates", templates);

        return "/template/list";
    }

    /**
     * 템플릿 생성
     * @Method Name   : getTemplate
     * @Date / Author : 2023.12.01  이도현
     * @param template 템플릿 Id
     * @param request request 객체
     * @param response response 객체
     * @return 템플릿 정보 객체
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    @PostMapping("/get/info")
    @ResponseBody
    public List<ObjectVO> getTemplate(@RequestBody Map<String, String> template,  HttpServletRequest request, HttpServletResponse response){

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        comUtils.sessionCheck(mstrSessionId, request, response);

        //2. 템플릿 정보 조회 후 반환
        String templateId = template.get("templateId");
        List<ObjectVO> entities = templateService.getTemplate(templateId);

        return entities;
    }

    /**
     * 템플릿 삭제
     * @Method Name   : deleteTemplate
     * @Date / Author : 2023.12.01  이도현
     * @param template 템플릿 Id
     * @param request request 객체
     * @param response response 객체
     * @return 템플릿 정보 삭제
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    @PostMapping("/delete")
    @ResponseBody
    public ResVO deleteTemplate(@RequestBody Map<String, String> template,  HttpServletRequest request, HttpServletResponse response){

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        comUtils.sessionCheck(mstrSessionId, request, response);

        //2. 템플릿 정보 조회 후 반환
        String templateId = template.get("templateId");
        ResVO result = templateService.deleteTemplate(templateId);

        return result;
    }
}


