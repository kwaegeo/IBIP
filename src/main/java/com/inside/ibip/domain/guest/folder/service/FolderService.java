package com.inside.ibip.domain.guest.folder.service;

import com.inside.ibip.domain.guest.folder.vo.TreeVO;
import com.inside.ibip.domain.guest.folder.vo.FolderVO;
import com.inside.ibip.global.mstr.MstrObject;
import com.inside.ibip.global.vo.EnumFolderNamesKR;
import com.microstrategy.webapi.EnumDSSXMLFolderNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @FileName     : FolderService.java
 * @Date         : 2023.12.01
 * @Author       : 이도현
 * @Description  : 폴더 Service, 폴더 정보 조회 및 하위 목록 조회
 * @History
 * =======================================================
 *   DATE			AUTHOR			NOTE
 * =======================================================
 *   2023.12.01     이도현         최초작성
 *
 */
@Service
public class FolderService {

    @Autowired
    private MstrObject mstrObject;

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
