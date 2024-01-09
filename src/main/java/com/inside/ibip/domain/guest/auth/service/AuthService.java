package com.inside.ibip.domain.guest.auth.service;

import com.inside.ibip.global.exception.code.ResultCode;
import com.inside.ibip.global.mstr.MstrSession;
import com.inside.ibip.global.vo.ResVO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @FileName     : AuthService.java
 * @Date         : 2023.12.01
 * @Author       : 이도현
 * @Description  : 인증 Service, 로그인과 로그아웃 처리
 * @History
 * =======================================================
 *   DATE			AUTHOR			NOTE
 * =======================================================
 *   2023.12.01     이도현         최초작성
 *
 */
@Log4j2
@Service
public class AuthService {

    @Autowired
    private MstrSession mstrSession;

    /**
     * Mstr 세션 생성 함수
     * @Method Name   : CreateMstrSession
     * @Date / Author : 2023.12.01  이도현
     * @param userId request 객체
     * @param pwd request 객체
     * @return mstrSessionId
     * @History
     * 2023.12.01	최초생성
     */
    public String CreateMstrSession(String userId, String pwd){
        return mstrSession.createSession(userId, pwd);
    }

    /**
     * Mstr 세션 제거 함수
     * @Method Name   : logoutSession
     * @Date / Author : 2023.12.01  이도현
     * @return 응답 코드, 메세지
     * @History
     * 2023.12.01	최초생성
     */
    public ResVO logoutSession(){
        mstrSession.logoutSession();
        return new ResVO(ResultCode.SUCCESS);
    }

}
