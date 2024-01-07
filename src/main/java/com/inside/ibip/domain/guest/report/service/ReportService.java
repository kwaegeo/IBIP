package com.inside.ibip.domain.guest.report.service;

import com.inside.ibip.domain.guest.report.vo.ReportVO;
import com.inside.ibip.global.mstr.MstrObject;
import com.inside.ibip.global.utils.UrlUtils;
import com.microstrategy.web.objects.WebObjectsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.inside.ibip.global.utils.UrlUtils.generateXML;

@Service
public class ReportService {


    @Autowired
    private MstrObject mstrObject;

    public ReportVO getPromptData(String mstrSessionId, String reportId, String documentType) throws WebObjectsException {

        mstrObject.setSession(mstrSessionId);

        ReportVO reportInfo = mstrObject.getReportInfo(reportId, documentType);
        reportInfo = mstrObject.getReportDataInfo(reportId, reportInfo);

        return reportInfo;
    }

    public String getPromptXml(ReportVO reportInfo) {
        String promptXml = generateXML(reportInfo);
        System.out.println(promptXml);
        return promptXml;
    }

    public String getUsrSmgr(){
        return mstrObject.getUsrSmgr();
    }

    public String getReportURL(ReportVO reportInfo, String promptXml, String usrSmgr){

        String reportURL = UrlUtils.getReportURL(reportInfo, promptXml, usrSmgr);

        return reportURL;
    }

}
