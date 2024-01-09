package com.inside.ibip.domain.guest.folder.service;

import com.inside.ibip.domain.guest.folder.vo.TreeVO;
import com.inside.ibip.domain.guest.folder.vo.TopItemVO;
import com.inside.ibip.domain.guest.auth.vo.FolderVO;
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

        //1. 공유 리포트 폴더 ID 조회
        String shareFolderId = mstrObject.getFolderId(EnumDSSXMLFolderNames.DssXmlFolderNamePublicReports);

        //2. 폴더 ID로 하위 목록 조회
        shareReportList = mstrObject.getShareReport(shareFolderId);

        return shareReportList;
    }

    public List<TreeVO> getUserFolderList(String mstrSessionId) throws WebObjectsException {
        //1. 담을 리스트를 만들어 주고
        List<TreeVO> userFolderList = new ArrayList<>();

        //1-1. mstr set session을 먼저 해준다.
        mstrObject.setSession(mstrSessionId);

        //1. 먼저 공유 폴더의 아이디를 가져오는 함수를 부른다.
        String myReportId = mstrObject.getFolderId(EnumDSSXMLFolderNames.DssXmlFolderNameProfileReports);
        String myFavoriteId = mstrObject.getFolderId(EnumDSSXMLFolderNames.DssXmlFolderNameProfileFavorites);

        //2. 받은 폴더 ID가지고 목록을 가져오는 함수를 부른다
        FolderVO myReportInfo = mstrObject.getFolderInfo(myReportId);
        FolderVO myFavoriteInfo = mstrObject.getFolderInfo(myFavoriteId);

        userFolderList.add(new TreeVO(myReportInfo.getId(), EnumFolderNamesKR.myReport, "#", myReportInfo.getTp(), true));
        userFolderList.add(new TreeVO(myFavoriteInfo.getId(), EnumFolderNamesKR.myFavorite, "#", myFavoriteInfo.getTp(), true));

        return userFolderList;
    }

    public List<TreeVO> getSubList2(String folderId) throws WebObjectsException {
        List<TreeVO> subList = new ArrayList<>();
        subList = mstrObject.getSubList(folderId, "", subList);
        return subList;
    }
}
