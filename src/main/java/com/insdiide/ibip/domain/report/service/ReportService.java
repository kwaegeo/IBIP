package com.insdiide.ibip.domain.report.service;

import com.insdiide.ibip.domain.prompt.vo2.PromptDataVO;
import com.insdiide.ibip.domain.prompt.vo2.PromptVO;
import com.insdiide.ibip.global.mstr.MstrObject;
import com.microstrategy.web.objects.WebObjectsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {


    @Autowired
    private MstrObject mstrObject;

    public PromptVO getPromptData(String mstrSessionId, String reportId) throws WebObjectsException {

        mstrObject.setSession(mstrSessionId);

        PromptVO PromptList = mstrObject.getPromptData(reportId);

        return PromptList;
    }
}
