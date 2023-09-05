package com.insdiide.ibip.domain.main.service;

import com.insdiide.ibip.domain.login.vo.FolderVO;
import com.insdiide.ibip.domain.main.vo.SideBarItemVO;
import com.insdiide.ibip.global.mstr.MstrObject;
import com.insdiide.ibip.global.mstr.MstrSession;
import com.microstrategy.web.objects.WebObjectsException;
import com.microstrategy.webapi.EnumDSSXMLFolderNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MainService {

    @Autowired
    private MstrSession mstrSession;

    @Autowired
    private MstrObject mstrObject;

    public SideBarItemVO getSideBarItems() throws WebObjectsException {

        List<FolderVO> shareFolderItems = new ArrayList<>();
        shareFolderItems = mstrObject.getSubfolderList("D3C7D461F69C4610AA6BAA5EF51F4125");

        System.out.println(shareFolderItems);

        return new SideBarItemVO();
    }

}
