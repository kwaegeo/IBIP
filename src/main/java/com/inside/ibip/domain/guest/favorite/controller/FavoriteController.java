package com.inside.ibip.domain.guest.favorite.controller;

import com.inside.ibip.domain.guest.favorite.service.FavoriteService;
import com.inside.ibip.domain.guest.favorite.vo.FavoriteVO;
import com.inside.ibip.domain.guest.main.vo.UserInfoVO;
import com.inside.ibip.domain.guest.report.vo.ReportVO;
import com.inside.ibip.global.exception.code.ResultCode;
import com.inside.ibip.global.utils.ComUtils;
import com.inside.ibip.global.vo.ResVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @FileName     : FavoriteController.java
 * @Date         : 2023.12.01
 * @Author       : 이도현
 * @Description  : 즐겨찾기 Controller, 즐겨찾기를 관리
 * @History
 * =======================================================
 *   DATE			AUTHOR			NOTE
 * =======================================================
 *   2023.12.01     이도현         최초작성
 *
 */
@Controller
@RequestMapping("/favorite")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private ComUtils comUtils;

    /**
     * 즐겨찾기 추가
     * @Method Name   : addFavorite
     * @Date / Author : 2023.12.01  이도현
     * @param favoriteInfo 즐겨찾기 정보 객체
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
    public ResVO addFavorite(@RequestBody FavoriteVO favoriteInfo, HttpServletRequest request, HttpServletResponse response){

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        comUtils.sessionCheck(mstrSessionId, request, response);

        //2. 즐겨찾기에 추가
        ResVO result = favoriteService.addFavorite(favoriteInfo);

        //3. 응답
        return result;
    }

    /**
     * 즐겨찾기 삭제
     * @Method Name   : deleteFavorite
     * @Date / Author : 2023.12.01  이도현
     * @param favoriteInfo 즐겨찾기 정보 객체
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
    public ResVO deleteFavorite(@RequestBody FavoriteVO favoriteInfo, HttpServletRequest request, HttpServletResponse response){

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        comUtils.sessionCheck(mstrSessionId, request, response);

        //2. 즐겨찾기에 추가
        ResVO result = favoriteService.deleteFavorite(favoriteInfo);

        //3. 응답
        return result;
    }
}
