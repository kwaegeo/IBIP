package com.insdiide.ibip.domain.admin.role.service;

import com.insdiide.ibip.domain.admin.group.vo.GroupVO;
import com.insdiide.ibip.domain.admin.role.vo.RoleVO;
import com.insdiide.ibip.domain.admin.user.vo.UserVO;
import com.insdiide.ibip.global.mstr.MstrObject;
import com.microstrategy.web.objects.WebObjectsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

}
