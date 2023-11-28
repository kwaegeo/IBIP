package com.insdiide.ibip.domain.folder.service;

import com.insdiide.ibip.domain.folder.vo.EntityVO;
import com.insdiide.ibip.domain.folder.vo.TopItemVO;
import com.insdiide.ibip.domain.login.vo.FolderVO;
import com.insdiide.ibip.global.mstr.MstrObject;
import com.insdiide.ibip.global.vo.EnumFolderNamesKR;
import com.microstrategy.web.objects.WebObjectsException;
import com.microstrategy.webapi.EnumDSSXMLFolderNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.parser.Entity;
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

    public List<EntityVO> getSubList(String mstrSessionId, String folderId) throws WebObjectsException{
        //세션 정보 삽입
        mstrObject.setSession(mstrSessionId);

        List<EntityVO> subList = new ArrayList<>();
        subList = mstrObject.getSubList(folderId, "", subList);

        return subList;
    }

    public List<EntityVO> getRootFolderList(String mstrSessionId) throws WebObjectsException {

        //1. 담을 리스트를 만들어 주고
        List<EntityVO> rootFolderList;

        //1-1. mstr set session을 먼저 해준다.
        mstrObject.setSession(mstrSessionId);

        //1. 먼저 공유 폴더의 아이디를 가져오는 함수를 부른다.
        String shareFolderId = mstrObject.getFolderId(EnumDSSXMLFolderNames.DssXmlFolderNamePublicReports);
        System.out.println(shareFolderId);
        //2. 받은 폴더 ID가지고 목록을 가져오는 함수를 부른다
        rootFolderList = mstrObject.getRootFolderList(shareFolderId);

        return rootFolderList;
    }

    public List<EntityVO> getUserFolderList(String mstrSessionId) throws WebObjectsException {
        //1. 담을 리스트를 만들어 주고
        List<EntityVO> userFolderList = new ArrayList<>();

        //1-1. mstr set session을 먼저 해준다.
        mstrObject.setSession(mstrSessionId);

        //1. 먼저 공유 폴더의 아이디를 가져오는 함수를 부른다.
        String myReportId = mstrObject.getFolderId(EnumDSSXMLFolderNames.DssXmlFolderNameProfileReports);
        String myFavoriteId = mstrObject.getFolderId(EnumDSSXMLFolderNames.DssXmlFolderNameProfileFavorites);

        //2. 받은 폴더 ID가지고 목록을 가져오는 함수를 부른다
        FolderVO myReportInfo = mstrObject.getFolderInfo(myReportId);
        FolderVO myFavoriteInfo = mstrObject.getFolderInfo(myFavoriteId);

        userFolderList.add(new EntityVO(myReportInfo.getId(), EnumFolderNamesKR.myReport, "#", myReportInfo.getTp(), true));
        userFolderList.add(new EntityVO(myFavoriteInfo.getId(), EnumFolderNamesKR.myFavorite, "#", myFavoriteInfo.getTp(), true));

        return userFolderList;
    }

    public List<EntityVO> getSubList2(String folderId) throws WebObjectsException {
        List<EntityVO> subList = new ArrayList<>();
        subList = mstrObject.getSubList(folderId, "", subList);
        return subList;
    }
}
