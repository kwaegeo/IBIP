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

@Controller
@RequestMapping("/favorite")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private ComUtils comUtils;

    @PostMapping("/add")
    @ResponseBody
    public ResVO addFavorite(@RequestBody FavoriteVO favoriteInfo, HttpServletRequest request, HttpServletResponse response){

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        comUtils.sessionCheck(mstrSessionId, request, response);

        favoriteService.addFavorite(favoriteInfo);


        //3. 응답
        return new ResVO(ResultCode.SUCCESS);
    }

}
