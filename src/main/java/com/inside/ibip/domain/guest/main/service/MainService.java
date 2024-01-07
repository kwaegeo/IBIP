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

    /**
     * 사이드 바에 필요한 정보를 불러오는 함수
     * **/
    public SideBarItemVO getSideBarItems() {

        //1. 폴더 객체 생성 [공유리포트, 내리포트, 즐겨찾기]
        List<FolderVO> shareFolderItems;
        FolderVO myReport;
        FolderVO myFavorite;

        //1. 먼저 공유 폴더의 아이디를 가져오는 함수를 부른다.
        String shareFolderId = mstrObject.getFolderId(EnumDSSXMLFolderNames.DssXmlFolderNamePublicReports);

        //2. 공유 리포트 하위 목록 조회
        shareFolderItems = mstrObject.getSubfolderList(shareFolderId);

        //3. 내 리포트, 즐겨찾기 가져오기 (사용내역 목록, 구독 목록은 안된다 iframe으로 직접 띄워야 함.)
        String myReportId = mstrObject.getFolderId(EnumDSSXMLFolderNames.DssXmlFolderNameProfileReports);
        String myFavoriteId = mstrObject.getFolderId(EnumDSSXMLFolderNames.DssXmlFolderNameProfileFavorites);
        myReport = mstrObject.getFolderInfo(myReportId);
        myFavorite = mstrObject.getFolderInfo(myFavoriteId);
        myReport.setName(EnumFolderNamesKR.myReport);
        myFavorite.setName(EnumFolderNamesKR.myFavorite);

        SideBarItemVO sideBarItem = SideBarItemVO.builder()
                .shareFolderItems(shareFolderItems)
                .myReport(myReport)
                .myFavorite(myFavorite)
                .build();

        return sideBarItem;
    }

    public ReportVO getDashboardReport(String userId) throws WebObjectsException {
        ReportVO reportInfo = mstrObject.getDashboardReport(userId);


        return reportInfo;
    }

    public UserInfoVO getUserInfo(String mstrSessionId){
        mstrObject.setSession(mstrSessionId);
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
