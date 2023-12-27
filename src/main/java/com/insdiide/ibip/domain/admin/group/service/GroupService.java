package com.insdiide.ibip.domain.admin.group.service;

import com.insdiide.ibip.domain.admin.group.vo.GroupVO;
import com.insdiide.ibip.global.mstr.MstrObject;
import com.insdiide.ibip.global.vo.ResVO;
import com.microstrategy.web.objects.WebObjectsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {

    @Autowired
    private MstrObject mstrObject;

    public List<GroupVO> getGroupList() throws WebObjectsException {
         List<GroupVO> groupList = mstrObject.getGroupList();
         return groupList;
    }

    public ResVO addGroup(){
        ResVO result = mstrObject.addGroup();
        return result;
    }

    public ResVO delGroup() throws WebObjectsException {
        ResVO result = mstrObject.delGroup();
        return result;
    }

    public GroupVO getGroupInfo(String groupId) throws WebObjectsException {
        /**
         *
         * 그룹 정보
         * 사용자 정보 (목록) 그룹 할당유무, 로그인ID, 사용자명, 계정상태, 소유자, 변경시간, 설명
         *
         * **/
        GroupVO groupInfo = mstrObject.getGroupInfo(groupId);
        groupInfo = mstrObject.getGroupUserList(groupInfo);
        return groupInfo;
    }

    public void getGroupTest() throws WebObjectsException {
        mstrObject.getGroupTest();
    }

    public ResVO assign(GroupVO groupInfo){
        ResVO result = mstrObject.assignGroup(groupInfo);
        return result;
    }
}
