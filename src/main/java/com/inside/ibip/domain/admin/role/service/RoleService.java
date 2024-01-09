package com.inside.ibip.domain.admin.role.service;

import com.inside.ibip.domain.admin.role.vo.PrivilegeAssignVO;
import com.inside.ibip.domain.admin.role.vo.RoleVO;
import com.inside.ibip.global.mstr.MstrObject;
import com.inside.ibip.global.vo.ResVO;
import com.microstrategy.web.objects.WebObjectsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private MstrObject mstrObject;
    public List<RoleVO> getRoleList() throws WebObjectsException {

        List<RoleVO> roleList = mstrObject.getRoleList();

        return roleList;
    }


    public RoleVO getRoleInfo(String roleId) throws WebObjectsException {
        /**
         *
         * 그룹 정보
         * 사용자 정보 (목록) 그룹 할당유무, 로그인ID, 사용자명, 계정상태, 소유자, 변경시간, 설명
         *
         * **/
        RoleVO roleInfo = mstrObject.getRoleInfo(roleId);
//        roleInfo = mstrObject.getUserGroupList(roleInfo);
        return roleInfo;
    }

    public ResVO savePrivileges(PrivilegeAssignVO privilegeList) throws WebObjectsException {
        ResVO result = mstrObject.savePrivileges(privilegeList);
        return result;
    }

}
