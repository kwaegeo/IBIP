package com.inside.ibip.domain.guest.main.service;

import com.inside.ibip.domain.guest.auth.vo.FolderVO;
import com.inside.ibip.domain.guest.main.vo.SearchVO;
import com.inside.ibip.domain.guest.main.vo.SideBarItemVO;
import com.inside.ibip.domain.guest.main.vo.UserInfoVO;
import com.inside.ibip.domain.guest.report.vo.ReportVO;
import com.inside.ibip.global.mstr.MstrObject;
import com.inside.ibip.global.utils.UrlUtils;
import com.inside.ibip.global.vo.EnumFolderNamesKR;
import com.microstrategy.web.objects.WebObjectsException;
import com.microstrategy.webapi.EnumDSSXMLFolderNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MainService {

    @Autowired
    private MstrObject mstrObject;

    public void setMstrSession(String mstrSessionId){
        mstrObject.setSession(mstrSessionId);
    }


    public ReportVO getDashboardReport(String userId) throws WebObjectsException {
        ReportVO reportInfo = mstrObject.getDashboardReport(userId);


        return reportInfo;
    }

    public UserInfoVO getUserInfo(){
        UserInfoVO userInfo = mstrObject.getUserInfo();
        return userInfo;
    }

    public String getHistoryURL(String usrSmgr){
        String historyListURL = UrlUtils.getHistoryURL(usrSmgr);

        return historyListURL;
    }

    public String getSubscriptionURL(String usrSmgr){
        String subscriptionURL = UrlUtils.getSubscriptionURL(usrSmgr);

        return subscriptionURL;
    }

    public String getDashboardURL(ReportVO reportInfo, String usrSmgr){
        String dashboardURL = UrlUtils.getDashboardURL(reportInfo, usrSmgr);

        return dashboardURL;
    }

    public SearchVO searchReport(String searchKeyword) throws WebObjectsException {

        SearchVO search = mstrObject.searchReport(searchKeyword);

        return search;
    }


}
