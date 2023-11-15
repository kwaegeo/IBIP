package com.insdiide.ibip.domain.main.service;

import com.insdiide.ibip.domain.login.vo.FolderVO;
import com.insdiide.ibip.domain.main.vo.SearchVO;
import com.insdiide.ibip.domain.main.vo.SideBarItemVO;
import com.insdiide.ibip.domain.main.vo.UserInfoVO;
import com.insdiide.ibip.global.mstr.MstrObject;
import com.insdiide.ibip.global.mstr.MstrSession;
import com.insdiide.ibip.global.utils.UrlUtils;
import com.insdiide.ibip.global.vo.EnumFolderNamesKR;
import com.microstrategy.web.objects.WebObjectsException;
import com.microstrategy.web.objects.WebSearch;
import com.microstrategy.webapi.EnumDSSXMLFolderNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MainService {

    @Autowired
    private MstrObject mstrObject;


    /**
     * 왼쪽 폴더 메뉴를 불러오는 함수
     * **/
    public SideBarItemVO getSideBarItems(String mstrSessionId) throws WebObjectsException {

        //1. 담을 리스트를 만들어 주고
        List<FolderVO> shareFolderItems;
        FolderVO myReport;
        FolderVO myFavorite;

        //1-1. mstr set session을 먼저 해준다.
        mstrObject.setSession(mstrSessionId);

        //1. 먼저 공유 폴더의 아이디를 가져오는 함수를 부른다.
        String shareFolderId = mstrObject.getFolderId(EnumDSSXMLFolderNames.DssXmlFolderNamePublicReports);
        //2. 받은 폴더 ID가지고 목록을 가져오는 함수를 부른다
        shareFolderItems = mstrObject.getSubfolderList(shareFolderId);

        //3. 내 리포트, 즐겨찾기 가져오기 (사용내역 목록, 구독 목록은 안된다 iframe으로 직접 띄워야 함.)
        String myReportId = mstrObject.getFolderId(EnumDSSXMLFolderNames.DssXmlFolderNameProfileReports);
        String myFavoriteId = mstrObject.getFolderId(EnumDSSXMLFolderNames.DssXmlFolderNameProfileFavorites);
        myReport = mstrObject.getfolderInfo(myReportId);
        myFavorite = mstrObject.getfolderInfo(myFavoriteId);
        myReport.setName(EnumFolderNamesKR.myReport);
        myFavorite.setName(EnumFolderNamesKR.myFavorite);

        SideBarItemVO sideBarItem = SideBarItemVO.builder()
                .shareFolderItems(shareFolderItems)
                .myReport(myReport)
                .myFavorite(myFavorite)
                .build();

        return sideBarItem;
    }

    public UserInfoVO getUserInfo(String mstrSessionId) throws WebObjectsException {
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

    public SearchVO searchReport(String searchKeyword) throws WebObjectsException {

        SearchVO search = mstrObject.searchReport(searchKeyword);

        return search;
    }


}
