package com.insdiide.ibip.global.mstr;

import com.insdiide.ibip.domain.login.vo.FolderVO;
import com.microstrategy.web.objects.*;
import com.microstrategy.webapi.EnumDSSXMLObjectTypes;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MstrObject extends MstrSession{

    /**
     * MSTR Object factory
     */
    protected WebObjectsFactory factory		= WebObjectsFactory.getInstance();


    public List<FolderVO> getSubfolderList(String folderId) throws WebObjectsException {
        List<FolderVO> subFolderList = new ArrayList<>();
        WebFolder folder = (WebFolder) factory.getObjectSource().getObject(folderId, EnumDSSXMLObjectTypes.DssXmlTypeFolder);
        folder.populate();
        for(int i=0; i<folder.getChildCount(); i++){
            if(folder.get(i).getType() == EnumDSSXMLObjectTypes.DssXmlTypeFolder) {
                subFolderList.add((FolderVO) folder.get(i));
                System.out.println(folder.get(i));
            }
        }

        return subFolderList;
    }

//    public FolderVO getfolderInfo(String folderId){
//
//    }

    public String getFolderId(int folderNum) throws WebObjectsException {
        String folderId = factory.getObjectSource().getFolderID(folderNum);
        return folderId;
    }

}
