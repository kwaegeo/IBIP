package com.inside.ibip.domain.guest.folder.controller;

import com.inside.ibip.domain.guest.folder.service.FolderService;
import com.inside.ibip.domain.guest.folder.vo.TreeVO;
import com.inside.ibip.domain.guest.main.vo.UserInfoVO;
import com.inside.ibip.global.exception.CustomException;
import com.inside.ibip.global.utils.ComUtils;
import com.microstrategy.web.objects.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @FileName     : FolderController.java
 * @Date         : 2023.12.01
 * @Author       : 이도현
 * @Description  : 폴더 Controller, 폴더 정보 조회, 폴더 하위 목록 조회 등
 * @History
 * =======================================================
 *   DATE			AUTHOR			NOTE
 * =======================================================
 *   2023.12.01     이도현         최초작성
 *
 */
@Slf4j
@Controller
@RequestMapping("/folder")
public class FolderController {

    @Autowired
    private FolderService folderService;

    @Autowired
    private ComUtils comUtils;

    /**
     * 특정 폴더 하위 목록 조회
     * @Method Name   : getShareReport
     * @Date / Author : 2023.12.01  이도현
     * @param folderId 하위 목록 조회 할 폴더 ID
     * @param request request 객체
     * @param response response 객체
     * @return 폴더 하위 목록 리스트
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    @GetMapping("/subList")
    @ResponseBody
    public List<TreeVO> getSubList(@RequestParam(name = "folderId") String folderId, HttpServletRequest request, HttpServletResponse response){

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        try {
            comUtils.sessionCheck(mstrSessionId, request, response);
        } catch (CustomException ex) {
            throw ex;
        }

        //3. 사용자 정보 가져오기
        UserInfoVO userInfo = folderService.getUserInfo();

        //전달 받은 폴더의 하위 목록 조회
        List<TreeVO> subList = folderService.getSubList(folderId, userInfo.getUserId());
        return subList;
    }

    /**
     * 공유 리포트 하위 폴더 목록 조회
     * @Method Name   : getShareReport
     * @Date / Author : 2023.12.01  이도현
     * @param request request 객체
     * @param response response 객체
     * @return 폴더 리스트 (공유리포트 하위 폴더 목록)
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    @GetMapping("/get/shareReport")
    @ResponseBody
    public List<TreeVO> getShareReport(HttpServletRequest request, HttpServletResponse response){

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        comUtils.sessionCheck(mstrSessionId, request, response);

        //전달 받은 폴더의 하위 목록 조회
        List<TreeVO> subList = folderService.getShareReport();
        return subList;
    }


    /**
     * 내 리포트, 즐겨찾기 데이터 조회
     * @Method Name   : getMyData
     * @Date / Author : 2023.12.01  이도현
     * @param request request 객체
     * @param response response 객체
     * @return 폴더 리스트 (내 리포트, 즐겨찾기)
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    @GetMapping("/myData")
    @ResponseBody
    public List<TreeVO> getMyData(HttpServletRequest request, HttpServletResponse response) throws WebObjectsException {


        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        comUtils.sessionCheck(mstrSessionId, request, response);

        //전달 받은 폴더의 하위 목록 조회
        List<TreeVO> myData = folderService.getMyData();
        return myData;

    }


}