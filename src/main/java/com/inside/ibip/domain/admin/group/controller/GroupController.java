package com.inside.ibip.domain.admin.group.controller;

import com.inside.ibip.domain.admin.group.service.GroupService;
import com.inside.ibip.domain.admin.group.vo.GroupVO;
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
 * @FileName     : GroupController.java
 * @Date         : 2023.12.01
 * @Author       : 이도현
 * @Description  : 그룹 Controller, 그룹 리스트 조회, 정보 조회 등
 * @History
 * =======================================================
 *   DATE			AUTHOR			NOTE
 * =======================================================
 *   2023.12.01     이도현         최초작성
 *
 */
@Log4j2
@Controller
@RequestMapping("/group")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private ComUtils comUtils;

    /**
     * 그룹 리스트 조회
     * @Method Name   : getGroupList
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
    public List<GroupVO> getGroupList(HttpServletRequest request, HttpServletResponse response){

        //1. 사용자 세션 (MSTR) 유효성 검사

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        comUtils.sessionCheck(mstrSessionId, request, response);

        //2. 그룹 리스트 조회
        List<GroupVO> groupList = groupService.getGroupList();

        return groupList;
    }

    /**
     * 그룹 정보 조회
     * @Method Name   : getGroupInfo
     * @Date / Author : 2023.12.01  이도현
     * @param groupId 조회할 그룹의 Id
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
    public String getGroupInfo(@RequestParam String groupId, HttpServletRequest request, HttpServletResponse response, Model model){

        //1. 사용자 세션 (MSTR) 유효성 검사
        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        comUtils.sessionCheck(mstrSessionId, request, response);

        //2. 그룹 정보 조회
        GroupVO groupInfo = groupService.getGroupInfo(groupId);

        //3. model 객체에 삽입
        model.addAttribute("groupInfo", groupInfo);

        //4. 응답
        return "/admin/group/info";
    }

    /**
     * 그룹 생성 페이지 조회
     * @Method Name   : groupAdd
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
    public String groupAdd(HttpServletRequest request, HttpServletResponse response){

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        comUtils.sessionCheck(mstrSessionId, request, response);

        return "/admin/group/add";
    }

    /**
     * 그룹 생성
     * @Method Name   : groupAddProc
     * @Date / Author : 2023.12.01  이도현
     * @param groupInfo group 정보 객체
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
    public ResVO groupAddProc(@RequestBody GroupVO groupInfo, HttpServletRequest request, HttpServletResponse response){

        //1. 사용자 세션 (MSTR) 유효성 검사
        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        comUtils.sessionCheck(mstrSessionId, request, response);

        ResVO result = groupService.addGroup(groupInfo);
        return result;
    }

    /**
     * 그룹 수정
     * @Method Name   : groupModifyProc
     * @Date / Author : 2023.12.01  이도현
     * @param groupInfo group 정보 객체
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
    public ResVO groupModifyProc(@RequestBody GroupVO groupInfo, HttpServletRequest request, HttpServletResponse response){

        //1. 사용자 세션 (MSTR) 유효성 검사

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        comUtils.sessionCheck(mstrSessionId, request, response);

        ResVO result = groupService.modifyGroup(groupInfo);
        return result;
    }

    /**
     * 그룹 삭제
     * @Method Name   : groupDelProc
     * @Date / Author : 2023.12.01  이도현
     * @param groupInfo group 정보 객체
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
    public ResVO groupDelProc(@RequestBody Map<String,String> groupInfo, HttpServletRequest request, HttpServletResponse response){
        //1. 사용자 세션 (MSTR) 유효성 검사
        String groupId = groupInfo.get("groupId");

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        comUtils.sessionCheck(mstrSessionId, request, response);

        ResVO result = groupService.deleteGroup(groupId);
        return result;
    }

    /**
     * 그룹 사용자 할당
     * @Method Name   : groupAssign
     * @Date / Author : 2023.12.01  이도현
     * @param groupInfo group 정보 객체
     * @param request request 객체
     * @param response response 객체
     * @return 성공 유무
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    @PostMapping("/assign")
    @ResponseBody
    public ResVO groupAssign(@RequestBody GroupVO groupInfo, HttpServletRequest request, HttpServletResponse response){
        //1. 사용자 세션 (MSTR) 유효성 검사

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        comUtils.sessionCheck(mstrSessionId, request, response);

        ResVO result = groupService.assign(groupInfo);
        return result;
    }
}
