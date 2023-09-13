package com.insdiide.ibip.domain.folder.service;

import com.insdiide.ibip.domain.folder.vo.EntityVO;
import com.insdiide.ibip.domain.folder.vo.TopItemVO;
import com.insdiide.ibip.domain.login.vo.FolderVO;
import com.insdiide.ibip.global.mstr.MstrObject;
import com.microstrategy.web.objects.WebObjectsException;
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

    public List<EntityVO> getSubList(String mstrSessionId, String folderId) throws WebObjectsException{
        //세션 정보 삽입
        mstrObject.setSession(mstrSessionId);

        List<EntityVO> subList = mstrObject.getSubList(folderId);

        return subList;
    }

}
