package com.inside.ibip.domain.guest.folder.service;

import com.inside.ibip.domain.guest.folder.vo.TreeVO;
import com.inside.ibip.domain.guest.folder.vo.TopItemVO;
import com.inside.ibip.domain.guest.folder.vo.FolderVO;
import com.inside.ibip.global.mstr.MstrObject;
import com.inside.ibip.global.vo.EnumFolderNamesKR;
import com.microstrategy.web.objects.WebObjectsException;
import com.microstrategy.webapi.EnumDSSXMLFolderNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FolderService {

    @Autowired
    private MstrObject mstrObject;

    public TopItemVO getTopItem(String mstrSessionId, String folderId) throws WebObjectsException {
        //세션 정보 삽입
        mstrObject.setSession(mstrSessionId);

        // 폴더 ID를 통해 부모 폴더 ID 조회
        String parentFolderId = mstrObject.getParentFolderId(folderId);

        // 부모 폴더의 하위 폴더 목록 조회
        List<FolderVO> subFolderList = mstrObject.getSubfolderList(parentFolderId);


        TopItemVO  topItem = new TopItemVO();
        topItem.setSubFolderItems(subFolderList);

        return topItem;
    }

    /**
     * 특정 폴더 목록 조회
     * @Method Name   : getShareReport
     * @Date / Author : 2023.12.01  이도현
     * @param folderId 하위 목록 조회 할 폴더 ID
     * @return 폴더 하위 목록 리스트
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    public List<TreeVO> getSubList(String folderId){
        //세션 정보 삽입

        List<TreeVO> subList = new ArrayList<>();
        subList = mstrObject.getSubList(folderId, "", subList);

        return subList;
    }

    /**
     * 공유 리포트 하위 폴더 목록 조회
     * @Method Name   : getShareReport
     * @Date / Author : 2023.12.01  이도현
     * @return 폴더 리스트 (공유리포트 하위 폴더 목록)
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    public List<TreeVO> getShareReport(){

        //1. 공유 리포트 하위 폴더 목록 담을 리스트 생성
        List<TreeVO> shareReportList;

        //1-1. 공유 리포트 폴더 ID 조회
        String shareFolderId = mstrObject.getFolderId(EnumDSSXMLFolderNames.DssXmlFolderNamePublicReports);

        //2. 폴더 ID로 하위 목록 조회
        shareReportList = mstrObject.getShareReport(shareFolderId);

        return shareReportList;
    }

    /**
     * 내 리포트 하위 목록 조회
     * @Method Name   : getMyData
     * @Date / Author : 2023.12.01  이도현
     * @return 폴더 리스트 (내 리포트 하위 목록, 즐겨찾기)
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    public List<TreeVO> getMyData() {

        //1. 내 리포트 및 즐겨찾기 목록 담을 리스트 생성
        List<TreeVO> myData = new ArrayList<>();

        //1-1. 내 리포트, 즐겨찾기 ID 조회
        String myReportId = mstrObject.getFolderId(EnumDSSXMLFolderNames.DssXmlFolderNameProfileReports);
        String myFavoriteId = mstrObject.getFolderId(EnumDSSXMLFolderNames.DssXmlFolderNameProfileFavorites);

        //2. 폴더 ID로 폴더 정보 조회
        FolderVO myReportInfo = mstrObject.getFolderInfo(myReportId);
        FolderVO myFavoriteInfo = mstrObject.getFolderInfo(myFavoriteId);

        //3. 2개의 Root Tree로 나눠서 전달 (내 리포트, 즐겨찾기)
        myData.add(new TreeVO(myReportInfo.getId(), EnumFolderNamesKR.myReport, "#", myReportInfo.getTp(), true));
        myData.add(new TreeVO(myFavoriteInfo.getId(), EnumFolderNamesKR.myFavorite, "#", myFavoriteInfo.getTp(), true));

        return myData;
    }

}
