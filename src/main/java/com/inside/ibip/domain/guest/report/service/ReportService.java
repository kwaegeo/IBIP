package com.inside.ibip.domain.guest.report.service;

import com.inside.ibip.domain.guest.report.vo.ReportVO;
import com.inside.ibip.global.mstr.MstrObject;
import com.inside.ibip.global.utils.UrlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @FileName     : ReportService.java
 * @Date         : 2023.12.01
 * @Author       : 이도현
 * @Description  : 리포트 정보 조회 및 URL 생성 처리
 * @History
 * =======================================================
 *   DATE			AUTHOR			NOTE
 * =======================================================
 *   2023.12.01     이도현         최초작성
 *
 */
@Service
public class ReportService {


    @Autowired
    private MstrObject mstrObject;

    @Autowired
    private UrlUtils urlUtils;


    /**
     * Mstr 리포트 정보 조회 함수
     * @Method Name   : getReportInfo
     * @Date / Author : 2023.12.01  이도현
     * @param reportId 리포트의 ID
     * @param documentType 문서 타입
     * @return 리포트 객체
     * @History
     * 2023.12.01	최초생성
     */
    public ReportVO getReportInfo(String reportId, String documentType){

        //1. 리포트 정보 조회
        ReportVO reportInfo = mstrObject.getReportInfo(reportId, documentType);

        //2. 리포트 데이터, 프롬프트 조회
        reportInfo = mstrObject.getReportDataInfo(reportId, reportInfo);

        return reportInfo;
    }

    /**
     * Mstr 리포트 정보 조회 함수
     * @Method Name   : getReportInfo
     * @Date / Author : 2023.12.01  이도현
     * @param reportInfo 리포트 정보
     * @return PromptXML String 형태로 반환
     * @History
     * 2023.12.01	최초생성
     */
    public String getPromptXml(ReportVO reportInfo) {
        String promptXml = urlUtils.generatePromptXML(reportInfo);
        return promptXml;
    }

    /**
     * Mstr usrSmgr 정보 조회 함수
     * @Method Name   : getUsrSmgr
     * @Date / Author : 2023.12.01  이도현
     * @return UsrSmgr
     * @History
     * 2023.12.01	최초생성
     */
    public String getUsrSmgr(){
        return mstrObject.getUsrSmgr();
    }

    /**
     * 리포트의 최종 URL 조회 하는 함수 (iframe src)
     * @Method Name   : getReportURL
     * @Date / Author : 2023.12.01  이도현
     * @param reportInfo 리포트 정보
     * @param promptXml PromptXML
     * @param usrSmgr 사용자 세션 정보
     * @return 리포트 URL (String)
     * @History
     * 2023.12.01	최초생성
     */
    public String getReportURL(ReportVO reportInfo, String promptXml, String usrSmgr){

        String reportURL = urlUtils.getReportURL(reportInfo, promptXml, usrSmgr);

        return reportURL;
    }

}
