package com.inside.ibip.domain.admin.role.service;

import com.inside.ibip.domain.admin.role.vo.PrivilegeAssignVO;
import com.inside.ibip.domain.admin.role.vo.RoleVO;
import com.inside.ibip.global.mstr.MstrObject;
import com.inside.ibip.global.vo.ResVO;
import com.microstrategy.web.objects.WebObjectsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @FileName     : RoleService.java
 * @Date         : 2023.12.01
 * @Author       : 이도현
 * @Description  : 보안역할 Service, 보안역할 리스트 조회, 정보 조회, 생성, 수정, 삭제
 * @History
 * =======================================================
 *   DATE			AUTHOR			NOTE
 * =======================================================
 *   2023.12.01     이도현         최초작성
 *
 */
@Service
public class RoleService {

    @Autowired
    private MstrObject mstrObject;

    /**
     * 보안역할 리스트를 조회
     * @Method Name   : getRoleList
     * @Date / Author : 2023.12.01  이도현
     * @return 보안역할 객체 리스트
     * @History
     * 2023.12.01	최초생성
     */
    public List<RoleVO> getRoleList(){

        List<RoleVO> roleList = mstrObject.getRoleList();

        return roleList;
    }

    /**
     * 보안역할 생성
     * @Method Name   : addRole
     * @Date / Author : 2023.12.01  이도현
     * @param roleInfo role 정보 객체
     * @return 성공 유무
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    public ResVO addRole(RoleVO roleInfo){
        ResVO result = mstrObject.addRole(roleInfo);
        return result;
    }

    /**
     * 보안역할 수정
     * @Method Name   : modifyRole
     * @Date / Author : 2023.12.01  이도현
     * @param roleInfo role 정보 객체
     * @return 성공 유무
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    public ResVO modifyRole(RoleVO roleInfo){
        ResVO result = mstrObject.modifyRole(roleInfo);
        return result;
    }

    /**
     * 보안역할 삭제
     * @Method Name   : deleteRole
     * @Date / Author : 2023.12.01  이도현
     * @param roleId 삭제할 보안역할 정보
     * @return 성공 유무
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    public ResVO deleteRole(String roleId){
        ResVO result = mstrObject.delRole(roleId);
        return result;
    }

    /**
     * 보안역할 정보 조회
     * @Method Name   : getRoleInfo
     * @Date / Author : 2023.12.01  이도현
     * @param roleId 조회할 보안역할의 Id
     * @return 보안역할 객체
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    public RoleVO getRoleInfo(String roleId){

        RoleVO roleInfo = mstrObject.getRoleInfo(roleId);
//        roleInfo = mstrObject.getUserGroupList(roleInfo);
        return roleInfo;
    }

    /**
     * 보안역할 권한 할당
     * @Method Name   : savePrivileges
     * @Date / Author : 2023.12.01  이도현
     * @param privilegeList 권한 객체 리스트
     * @return 성공 유무
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    public ResVO savePrivileges(PrivilegeAssignVO privilegeList){
        ResVO result = mstrObject.savePrivileges(privilegeList);
        return result;
    }

}
