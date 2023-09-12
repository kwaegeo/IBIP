package com.insdiide.ibip.domain.folder.service;

import com.insdiide.ibip.domain.folder.vo.TopItemVO;
import com.insdiide.ibip.global.mstr.MstrObject;
import com.microstrategy.web.objects.WebObjectsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FolderService {

    @Autowired
    private MstrObject mstrObject;

    public TopItemVO getTopItem(String mstrSessionId, String folderId) throws WebObjectsException {
        mstrObject.setSession(mstrSessionId);
        mstrObject.getFolderId(folderId);

        return new TopItemVO();
    }

}
