package com.inside.ibip.domain.guest.main.service;

import com.inside.ibip.domain.guest.main.vo.DashBoardVO;
import com.inside.ibip.domain.guest.main.vo.SearchVO;
import com.inside.ibip.domain.guest.main.vo.UserInfoVO;
import com.inside.ibip.domain.guest.report.vo.ReportVO;
import com.inside.ibip.global.exception.code.ResultCode;
import com.inside.ibip.global.mstr.MstrObject;
import com.inside.ibip.global.utils.UrlUtils;
import com.microstrategy.web.objects.WebObjectsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @FileName     : MainService.java
 * @Date         : 2023.12.01
 * @Author       : 이도현
 * @Description  : 메인 페이지 사이드 바 관리, 검색, URL 생성 처리
 * @History
 * =======================================================
 *   DATE			AUTHOR			NOTE
 * =======================================================
 *   2023.12.01     이도현         최초작성
 *
 */
@Service
public class MainService {

    @Autowired
    private MstrObject mstrObject;

    @Autowired
    private UrlUtils urlUtils;

    /**
     * Mstr 세션 적용 함수 (MstrSession, MstrObject를 Bean으로 등록해두었기 때문에 mstrSession의 정보를 object에 적용)
     * @Method Name   : setMstrSession
     * @Date / Author : 2023.12.01  이도현
     * @param mstrSessionId MstrSession에서 받은 세션 정보 Id
     * @History
     * 2023.12.01	최초생성
     */
    public void setMstrSession(String mstrSessionId){
        mstrObject.setSession(mstrSessionId);
    }


    /**
     * DashBoard 문서 정보를 조회하는 함수
     * @Method Name   : getDashboardReport
     * @Date / Author : 2023.12.01  이도현
     * @param userId 사용자 Id
     * @History
     * 2023.12.01	최초생성
     */
    public ReportVO getDashboardReport(String userId){

        ReportVO reportInfo = mstrObject.getDashboardReport(userId);

        return reportInfo;
    }

    /**
     * 로그인 한 MSTR 세션의 사용자 정보를 가져온다.
     * @Method Name   : getUserInfo
     * @Date / Author : 2023.12.01  이도현
     * @return 사용자 정보
     * @History
     * 2023.12.01	최초생성
     */
    public UserInfoVO getUserInfo(){
        UserInfoVO userInfo = mstrObject.getUserInfo();
        return userInfo;
    }

    /**
     * 내 사용내역 목록의 URL 조회 (로그인 계정)
     * @Method Name   : getHistoryURL
     * @Date / Author : 2023.12.01  이도현
     * @param usrSmgr 사용자 세션 정보
     * @return 사용자 정보
     * @History
     * 2023.12.01	최초생성
     */
    public String getHistoryURL(String usrSmgr){

        String historyListURL = urlUtils.getHistoryURL(usrSmgr);

        return historyListURL;
    }

    /**
     * 내 구독물 URL 조회 (로그인 계정)
     * @Method Name   : getSubscriptionURL
     * @Date / Author : 2023.12.01  이도현
     * @param usrSmgr 사용자 세션 정보
     * @return 사용자 정보
     * @History
     * 2023.12.01	최초생성
     */
    public String getSubscriptionURL(String usrSmgr){

        String subscriptionURL = urlUtils.getSubscriptionURL(usrSmgr);

        return subscriptionURL;
    }

    /**
     * 대시보드 URL 조회
     * @Method Name   : getDashboardURL
     * @Date / Author : 2023.12.01  이도현
     * @param reportInfo 문서 정보
     * @param usrSmgr 사용자 세션 정보
     * @return 사용자 정보
     * @History
     * 2023.12.01	최초생성
     */
    public DashBoardVO getDashboardURL(ReportVO reportInfo, String usrSmgr){

        DashBoardVO result = new DashBoardVO();
        String dashboardURL = urlUtils.getDashboardURL(reportInfo, usrSmgr);

        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMsg(ResultCode.SUCCESS.getMsg());
        result.setDashboardURL(dashboardURL);

        return result;
    }

    /**
     * 문서 검색 (리포트, 다큐먼트)
     * @Method Name   : search
     * @Date / Author : 2023.12.01  이도현
     * @param searchKeyword 검색 키워드
     * @return 검색 리스트
     * @History
     * 2023.12.01	최초생성
     */
    public SearchVO search(String searchKeyword){

        SearchVO search = mstrObject.search(searchKeyword);

        return search;
    }


}
