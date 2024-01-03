package com.insdiide.ibip.domain.admin.role.controller;

import com.insdiide.ibip.domain.admin.group.service.GroupService;
import com.insdiide.ibip.domain.admin.group.vo.GroupVO;
import com.insdiide.ibip.domain.admin.role.service.RoleService;
import com.insdiide.ibip.domain.admin.role.vo.PrivilegeAssignVO;
import com.insdiide.ibip.domain.admin.role.vo.RoleVO;
import com.insdiide.ibip.domain.admin.user.vo.UserVO;
import com.insdiide.ibip.global.exception.CustomException;
import com.insdiide.ibip.global.utils.ComUtils;
import com.insdiide.ibip.global.vo.ResVO;
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
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private ComUtils comUtils;


    @PostMapping("/getRoleList")
    @ResponseBody
    public List<RoleVO> getRoleList(HttpServletRequest request, HttpServletResponse response) throws WebObjectsException {

        //1. 사용자 세션 (MSTR) 유효성 검사

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        try{
            comUtils.sessionCheck(mstrSessionId, request, response);
        }catch(CustomException ex){
            throw ex;
        }

        List<RoleVO> roleList = roleService.getRoleList();

        return roleList;
    }

    @GetMapping("/roleInfo")
    public String roleInfo(@RequestParam String roleId, HttpServletRequest request, HttpServletResponse response, Model model) throws WebObjectsException {
        System.out.println(roleId);
        //1. 사용자 세션 (MSTR) 유효성 검사

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        try{
            comUtils.sessionCheck(mstrSessionId, request, response);
        }catch(CustomException ex){
            throw ex;
        }

        RoleVO roleInfo = roleService.getRoleInfo(roleId);

        model.addAttribute("roleInfo", roleInfo);
        return "/admin/role/info";
    }

    @PostMapping("/savePrivileges")
    @ResponseBody
    public ResVO saveRole(@RequestBody PrivilegeAssignVO privilegeList, HttpServletRequest request, HttpServletResponse response) throws WebObjectsException {
        //1. 사용자 세션 (MSTR) 유효성 검사

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        try{
            comUtils.sessionCheck(mstrSessionId, request, response);
        }catch(CustomException ex){
            throw ex;
        }

        System.out.println(privilegeList);

        ResVO result = roleService.savePrivileges(privilegeList);
        return result;
    }

}
