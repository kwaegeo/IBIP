package com.insdiide.ibip.domain.admin.user.service;

import com.insdiide.ibip.domain.admin.group.vo.GroupVO;
import com.insdiide.ibip.domain.admin.user.vo.UserVO;
import com.insdiide.ibip.global.mstr.MstrObject;
import com.insdiide.ibip.global.vo.ResVO;
import com.microstrategy.web.objects.WebObjectsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private MstrObject mstrObject;

    public List<UserVO> getUserList() throws WebObjectsException {
        List<UserVO> userList = mstrObject.getUserList();
        return userList;
    }

    public UserVO getUserInfo(String userId) throws WebObjectsException {
        /**
         *
         * 그룹 정보
         * 사용자 정보 (목록) 그룹 할당유무, 로그인ID, 사용자명, 계정상태, 소유자, 변경시간, 설명
         *
         * **/
        UserVO userInfo = mstrObject.getUserInfoById(userId);
        userInfo = mstrObject.getUserGroupList(userInfo);
        return userInfo;
    }

    public ResVO assign(UserVO userInfo){
        ResVO result = mstrObject.assignUser(userInfo);
        return result;
    }

    public ResVO resetPassword(String userId){
        ResVO result = mstrObject.resetPassword(userId);
        return result;
    }

    public ResVO enableAccount(UserVO userInfo){
        ResVO result = mstrObject.enableAccount(userInfo);
        return result;
    }

    public ResVO addUser(UserVO userInfo){
        ResVO result = mstrObject.addUser(userInfo);
        return result;
    }
}
