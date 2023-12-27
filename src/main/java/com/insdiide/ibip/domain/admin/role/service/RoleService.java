package com.insdiide.ibip.domain.admin.role.service;

import com.insdiide.ibip.domain.admin.group.vo.GroupVO;
import com.insdiide.ibip.domain.admin.role.vo.RoleVO;
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

}
