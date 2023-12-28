package com.insdiide.ibip.domain.admin.group.controller;

import com.insdiide.ibip.domain.admin.group.service.GroupService;
import com.insdiide.ibip.domain.admin.group.vo.GroupVO;
import com.insdiide.ibip.domain.report.vo.ReportVO;
import com.insdiide.ibip.global.exception.CustomException;
import com.insdiide.ibip.global.exception.code.ResultCode;
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
import java.util.Map;

@Controller
public class GroupController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private ComUtils comUtils;

    @PostMapping("/getGroupList")
    @ResponseBody
    public List<GroupVO> getGroupList(HttpServletRequest request, HttpServletResponse response) throws WebObjectsException {

        //1. 사용자 세션 (MSTR) 유효성 검사

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        try{
            comUtils.sessionCheck(mstrSessionId, request, response);
        }catch(CustomException ex){
            throw ex;
        }

        List<GroupVO> groupList = groupService.getGroupList();

        return groupList;
    }

    @GetMapping("/groupInfo")
    public String groupInfo(@RequestParam String groupId, HttpServletRequest request, HttpServletResponse response, Model model) throws WebObjectsException {
        System.out.println(groupId);
        //1. 사용자 세션 (MSTR) 유효성 검사

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        try{
            comUtils.sessionCheck(mstrSessionId, request, response);
        }catch(CustomException ex){
            throw ex;
        }

        GroupVO groupInfo = groupService.getGroupInfo(groupId);

        model.addAttribute("groupInfo", groupInfo);
        return "/admin/group/info";
    }

    @GetMapping("/groupAdd")
    public String groupAdd(HttpServletRequest request, HttpServletResponse response, Model model){

        return "/admin/group/add";
    }

    @PostMapping("/groupAddProc")
    @ResponseBody
    public ResVO groupAddProc(@RequestBody GroupVO groupInfo, HttpServletRequest request, HttpServletResponse response){

        //1. 사용자 세션 (MSTR) 유효성 검사

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        try{
            comUtils.sessionCheck(mstrSessionId, request, response);
        }catch(CustomException ex){
            throw ex;
        }

        ResVO result = groupService.addGroup(groupInfo);
        return result;
    }

    @PostMapping("/groupModifyProc")
    @ResponseBody
    public ResVO groupModifyProc(@RequestBody GroupVO groupInfo, HttpServletRequest request, HttpServletResponse response){

        //1. 사용자 세션 (MSTR) 유효성 검사

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        try{
            comUtils.sessionCheck(mstrSessionId, request, response);
        }catch(CustomException ex){
            throw ex;
        }

        ResVO result = groupService.modifyGroup(groupInfo);
        return result;
    }

    @PostMapping("/groupDelProc")
    @ResponseBody
    public ResVO groupDelProc(@RequestBody Map<String,String> groupInfo, HttpServletRequest request, HttpServletResponse response) throws WebObjectsException {
        //1. 사용자 세션 (MSTR) 유효성 검사
        String groupId = groupInfo.get("groupId");

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        try{
            comUtils.sessionCheck(mstrSessionId, request, response);
        }catch(CustomException ex){
            throw ex;
        }

        ResVO result = groupService.delGroup(groupId);
        return result;
    }


    @GetMapping("/testGroup")
    @ResponseBody
    public String getTestGroup() throws WebObjectsException {
        groupService.getGroupTest();
        return "일단 확인";
    }

    @PostMapping("/groupManageAssignment")
    @ResponseBody
    public ResVO groupAssign(@RequestBody GroupVO groupInfo, HttpServletRequest request, HttpServletResponse response){
        //1. 사용자 세션 (MSTR) 유효성 검사

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        try{
            comUtils.sessionCheck(mstrSessionId, request, response);
        }catch(CustomException ex){
            throw ex;
        }

        System.out.println(groupInfo);
        ResVO result = groupService.assign(groupInfo);
        return result;
    }
}
