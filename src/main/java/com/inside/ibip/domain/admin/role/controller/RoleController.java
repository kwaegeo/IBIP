package com.inside.ibip.domain.admin.role.controller;

import com.inside.ibip.domain.admin.role.service.RoleService;
import com.inside.ibip.domain.admin.role.vo.PrivilegeAssignVO;
import com.inside.ibip.domain.admin.role.vo.RoleVO;
import com.inside.ibip.global.exception.CustomException;
import com.inside.ibip.global.utils.ComUtils;
import com.inside.ibip.global.vo.ResVO;
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
import java.util.Map;


/**
 * @FileName     : RoleController.java
 * @Date         : 2023.12.01
 * @Author       : 이도현
 * @Description  : 보안역할 Controller, 보안역할 리스트 조회, 정보 조회 등
 * @History
 * =======================================================
 *   DATE			AUTHOR			NOTE
 * =======================================================
 *   2023.12.01     이도현         최초작성
 *
 */
@Log4j2
@Controller
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private ComUtils comUtils;

    /**
     * 보안역할 리스트 조회
     * @Method Name   : getRoleList
     * @Date / Author : 2023.12.01  이도현
     * @param request request 객체
     * @param response response 객체
     * @return 보안역할 리스트 객체
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    @PostMapping("/get/list")
    @ResponseBody
    public List<RoleVO> getRoleList(HttpServletRequest request, HttpServletResponse response){

        //1. 사용자 세션 (MSTR) 유효성 검사
        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        comUtils.sessionCheck(mstrSessionId, request, response);

        List<RoleVO> roleList = roleService.getRoleList();

        return roleList;
    }


    /**
     * 보안역할 정보 조회
     * @Method Name   : getRoleInfo
     * @Date / Author : 2023.12.01  이도현
     * @param roleId 조회할 보안역할의 Id
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
    public String getRoleInfo(@RequestParam String roleId, HttpServletRequest request, HttpServletResponse response, Model model){

        //1. 사용자 세션 (MSTR) 유효성 검사
        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        comUtils.sessionCheck(mstrSessionId, request, response);

        //2. 보안역할 정보 조회
        RoleVO roleInfo = roleService.getRoleInfo(roleId);

        //3. model 객체에 삽입
        model.addAttribute("roleInfo", roleInfo);

        return "/admin/role/info";
    }

    /**
     * 보안역할 생성 페이지 조회
     * @Method Name   : roleAdd
     * @Date / Author : 2023.12.01  이도현
     * @param request request 객체
     * @param response response 객체
     * @return 그룹 생성 페이지
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    @GetMapping("/add")
    public String roleAdd(HttpServletRequest request, HttpServletResponse response){

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        comUtils.sessionCheck(mstrSessionId, request, response);

        return "/admin/role/add";
    }

    /**
     * 보안역할 생성
     * @Method Name   : roleAddProc
     * @Date / Author : 2023.12.01  이도현
     * @param roleInfo role 정보 객체
     * @param request request 객체
     * @param response response 객체
     * @return 성공 유무
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    @PostMapping("/add")
    @ResponseBody
    public ResVO roleAddProc(@RequestBody RoleVO roleInfo, HttpServletRequest request, HttpServletResponse response){

        //1. 사용자 세션 (MSTR) 유효성 검사
        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        comUtils.sessionCheck(mstrSessionId, request, response);

        ResVO result = roleService.addRole(roleInfo);
        return result;
    }

    /**
     * 보안역할 수정
     * @Method Name   : roleModifyProc
     * @Date / Author : 2023.12.01  이도현
     * @param roleInfo role 정보 객체
     * @param request request 객체
     * @param response response 객체
     * @return 성공 유무
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    @PostMapping("/modify")
    @ResponseBody
    public ResVO roleModifyProc(@RequestBody RoleVO roleInfo, HttpServletRequest request, HttpServletResponse response){

        //1. 사용자 세션 (MSTR) 유효성 검사

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        comUtils.sessionCheck(mstrSessionId, request, response);

        ResVO result = roleService.modifyRole(roleInfo);
        return result;
    }

    /**
     * 보안역할 삭제
     * @Method Name   : groupDelProc
     * @Date / Author : 2023.12.01  이도현
     * @param roleInfo role 정보 객체
     * @param request request 객체
     * @param response response 객체
     * @return 성공 유무
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    @PostMapping("/delete")
    @ResponseBody
    public ResVO groupDelProc(@RequestBody Map<String,String> roleInfo, HttpServletRequest request, HttpServletResponse response){
        //1. 사용자 세션 (MSTR) 유효성 검사
        String roleId = roleInfo.get("roleId");

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        comUtils.sessionCheck(mstrSessionId, request, response);

        ResVO result = roleService.deleteRole(roleId);
        return result;
    }

    /**
     * 보안역할 권한 할당
     * @Method Name   : savePrivileges
     * @Date / Author : 2023.12.01  이도현
     * @param privilegeList 권한 리스트 객체
     * @param request request 객체
     * @param response response 객체
     * @return 성공 유무
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    @PostMapping("/save/privileges")
    @ResponseBody
    public ResVO savePrivileges(@RequestBody PrivilegeAssignVO privilegeList, HttpServletRequest request, HttpServletResponse response){

        //1. 사용자 세션 (MSTR) 유효성 검사
        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        comUtils.sessionCheck(mstrSessionId, request, response);

        ResVO result = roleService.savePrivileges(privilegeList);
        return result;
    }

}
