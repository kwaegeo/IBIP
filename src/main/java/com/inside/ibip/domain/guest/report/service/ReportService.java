package com.inside.ibip.domain.guest.report.service;

import com.inside.ibip.domain.guest.report.vo.ReportVO;
import com.inside.ibip.global.mstr.MstrObject;
import com.inside.ibip.global.utils.UrlUtils;
import com.microstrategy.web.objects.WebObjectsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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

    public String getPromptXml(ReportVO reportInfo) {
        String promptXml = urlUtils.generateXML(reportInfo);
        System.out.println(promptXml);
        return promptXml;
    }

    public String getUsrSmgr(){
        return mstrObject.getUsrSmgr();
    }

    public String getReportURL(ReportVO reportInfo, String promptXml, String usrSmgr){

        String reportURL = urlUtils.getReportURL(reportInfo, promptXml, usrSmgr);

        return reportURL;
    }

}
