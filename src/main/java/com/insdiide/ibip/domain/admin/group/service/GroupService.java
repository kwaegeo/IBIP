package com.insdiide.ibip.domain.admin.group.service;

import com.insdiide.ibip.domain.admin.group.vo.GroupVO;
import com.insdiide.ibip.global.mstr.MstrObject;
import com.microstrategy.web.objects.WebObjectsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {

    @Autowired
    private MstrObject mstrObject;

    public List<GroupVO> getGroupList() throws WebObjectsException {
         List<GroupVO> groupList = mstrObject.getGrupList();
         return groupList;
    }
}
