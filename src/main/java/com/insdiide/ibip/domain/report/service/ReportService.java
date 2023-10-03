package com.insdiide.ibip.domain.report.service;

import com.insdiide.ibip.domain.report.vo.ReportVO;
import com.insdiide.ibip.global.mstr.MstrObject;
import com.microstrategy.web.objects.WebObjectsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportService {


    @Autowired
    private MstrObject mstrObject;

    public ReportVO getPromptData(String mstrSessionId, String reportId) throws WebObjectsException {

        mstrObject.setSession(mstrSessionId);

        ReportVO reportInfo = mstrObject.getReportInfo(reportId);

        return reportInfo;
    }
}
