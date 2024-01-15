package com.inside.ibip.domain.admin.group.service;

import com.inside.ibip.domain.admin.group.vo.GroupVO;
import com.inside.ibip.global.mstr.MstrObject;
import com.inside.ibip.global.vo.ResVO;
import com.microstrategy.web.objects.WebObjectsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @FileName     : GroupService.java
 * @Date         : 2023.12.01
 * @Author       : 이도현
 * @Description  : 그룹 Service, 그룹 리스트 조회, 정보 조회, 생성, 수정, 삭제
 * @History
 * =======================================================
 *   DATE			AUTHOR			NOTE
 * =======================================================
 *   2023.12.01     이도현         최초작성
 *
 */
@Slf4j
@Service
public class GroupService {

    @Autowired
    private MstrObject mstrObject;

    /**
     * 그룹 리스트를 조회한다.
     * @Method Name   : getGroupList
     * @Date / Author : 2023.12.01  이도현
     * @return 그룹 객체 리스트
     * @History
     * 2023.12.01	최초생성
     */
    public List<GroupVO> getGroupList(){
         List<GroupVO> groupList = mstrObject.getGroupList();
         return groupList;
    }

    /**
     * 그룹 생성
     * @Method Name   : addGroup
     * @Date / Author : 2023.12.01  이도현
     * @param groupInfo group 정보 객체
     * @return 성공 유무
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    public ResVO addGroup(GroupVO groupInfo){
        ResVO result = mstrObject.addGroup(groupInfo);
        return result;
    }

    /**
     * 그룹 수정
     * @Method Name   : groupModifyProc
     * @Date / Author : 2023.12.01  이도현
     * @param groupInfo group 정보 객체
     * @return 성공 유무
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    public ResVO modifyGroup(GroupVO groupInfo){
        ResVO result = mstrObject.modifyGroup(groupInfo);
        return result;
    }

    /**
     * 그룹 삭제
     * @Method Name   : deleteGroup
     * @Date / Author : 2023.12.01  이도현
     * @param groupId 삭제할 그룹 정보
     * @return 성공 유무
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    public ResVO deleteGroup(String groupId){
        ResVO result = mstrObject.deleteGroup(groupId);
        return result;
    }

    /**
     * 그룹 정보 조회
     * @Method Name   : getGroupInfo
     * @Date / Author : 2023.12.01  이도현
     * @param groupId 조회할 그룹의 Id
     * @return 그룹 객체
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    public GroupVO getGroupInfo(String groupId){

        GroupVO groupInfo = mstrObject.getGroupInfo(groupId);
        groupInfo = mstrObject.getGroupUserList(groupInfo);
        return groupInfo;
    }


    /**
     * 그룹 사용자 할당
     * @Method Name   : groupAssign
     * @Date / Author : 2023.12.01  이도현
     * @param groupInfo group 정보 객체
     * @return 성공 유무
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    public ResVO assign(GroupVO groupInfo){
        ResVO result = mstrObject.assignGroup(groupInfo);
        return result;
    }
}
