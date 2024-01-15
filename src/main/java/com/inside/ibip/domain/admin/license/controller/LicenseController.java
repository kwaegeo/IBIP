package com.inside.ibip.domain.admin.license.controller;

import com.inside.ibip.domain.admin.license.service.LicenseService;
import com.inside.ibip.domain.admin.license.vo.LicenseVO;
import com.inside.ibip.global.exception.CustomException;
import com.inside.ibip.global.utils.ComUtils;
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

/**
 * @FileName     : LicenseController.java
 * @Date         : 2023.12.01
 * @Author       : 이도현
 * @Description  : 라이센스 Controller, 라이센스 및 권한 조회
 * @History
 * =======================================================
 *   DATE			AUTHOR			NOTE
 * =======================================================
 *   2023.12.01     이도현         최초작성
 *
 */
@Log4j2
@Controller
@RequestMapping("/license")
public class LicenseController {

    @Autowired
    private LicenseService licenseService;

    @Autowired
    private ComUtils comUtils;

    /**
     * 라이센스 리스트 조회
     * @Method Name   : getLicenseList
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
    public List<LicenseVO> getLicenseList(HttpServletRequest request, HttpServletResponse response){

        //1. 사용자 세션 (MSTR) 유효성 검사

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        comUtils.sessionCheck(mstrSessionId, request, response);

        //2. 라이센스 리스트 조회
        List<LicenseVO> licenseList = licenseService.getLicenseList();

        return licenseList;
    }

    /**
     * 라이센스 정보 조회
     * @Method Name   : getLicenseInfo
     * @Date / Author : 2023.12.01  이도현
     * @param licenseType 조회할 라이센스의 Type
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
    public String getLicenseInfo(@RequestParam int licenseType, HttpServletRequest request, HttpServletResponse response, Model model){
        //1. 사용자 세션 (MSTR) 유효성 검사

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        comUtils.sessionCheck(mstrSessionId, request, response);

        //2. 라이센스 정보 조회
        LicenseVO licenseInfo = licenseService.getLicenseInfo(licenseType);

        //3. model 객체에 삽입
        model.addAttribute("licenseInfo", licenseInfo);

        //4. 응답
        return "/admin/license/info";
    }

}
