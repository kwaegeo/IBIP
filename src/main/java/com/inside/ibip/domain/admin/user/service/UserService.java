package com.inside.ibip.domain.admin.user.service;

import com.inside.ibip.domain.admin.user.vo.UserVO;
import com.inside.ibip.global.mstr.MstrObject;
import com.inside.ibip.global.vo.ResVO;
import com.microstrategy.web.objects.WebObjectsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @FileName     : UserService.java
 * @Date         : 2023.12.01
 * @Author       : 이도현
 * @Description  : 사용자 Service, 사용자 리스트 조회, 정보 조회, 생성, 수정, 삭제
 * @History
 * =======================================================
 *   DATE			AUTHOR			NOTE
 * =======================================================
 *   2023.12.01     이도현         최초작성
 *
 */
@Service
public class UserService {

    @Autowired
    private MstrObject mstrObject;

    /**
     * 사용자 리스트를 조회한다.
     * @Method Name   : getUserList
     * @Date / Author : 2023.12.01  이도현
     * @return 사용자 객체 리스트
     * @History
     * 2023.12.01	최초생성
     */
    public List<UserVO> getUserList(){
        List<UserVO> userList = mstrObject.getUserList();
        return userList;
    }

    /**
     * 사용자 정보 조회
     * @Method Name   : getUserInfo
     * @Date / Author : 2023.12.01  이도현
     * @param userId 조회할 사용자의 Id
     * @return 사용자 객체
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    public UserVO getUserInfo(String userId){

        UserVO userInfo = mstrObject.getUserInfoById(userId);
        userInfo = mstrObject.getUserGroupList(userInfo);

        /** 사용자 보안역할 구성 v2버전에 추가 예정 **/
//        mstrObject.getUserSecurityRole(userInfo);
        return userInfo;
    }

    /**
     * 사용자 그룹 할당
     * @Method Name   : assign
     * @Date / Author : 2023.12.01  이도현
     * @param userInfo user 정보 객체
     * @return 성공 유무
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    public ResVO assign(UserVO userInfo){
        ResVO result = mstrObject.assignUser(userInfo);
        return result;
    }

    /**
     * 사용자 비밀번호 초기화
     * @Method Name   : resetPassword
     * @Date / Author : 2023.12.01  이도현
     * @param userId 조회할 사용자의 Id
     * @return 성공 유무
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    public ResVO resetPassword(String userId){
        ResVO result = mstrObject.resetPassword(userId);
        return result;
    }

    /**
     * 사용자 계정 활성화/비활성화
     * @Method Name   : resetPassword
     * @Date / Author : 2023.12.01  이도현
     * @param userInfo 조회할 사용자 정보 객체
     * @return 성공 유무
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    public ResVO enableAccount(UserVO userInfo){
        ResVO result = mstrObject.enableAccount(userInfo);
        return result;
    }

    /**
     * 사용자 생성
     * @Method Name   : addUser
     * @Date / Author : 2023.12.01  이도현
     * @param userInfo group 정보 객체
     * @return 성공 유무
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    public ResVO addUser(UserVO userInfo){
        ResVO result = mstrObject.addUser(userInfo);
        return result;
    }

    /**
     * 그룹 수정
     * @Method Name   : modifyUser
     * @Date / Author : 2023.12.01  이도현
     * @param userInfo user 정보 객체
     * @return 성공 유무
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    public ResVO modifyUser(UserVO userInfo){
        ResVO result = mstrObject.modifyUser(userInfo);
        return result;
    }

    /**
     * 사용자 삭제
     * @Method Name   : deleteUser
     * @Date / Author : 2023.12.01  이도현
     * @param userId 삭제할 사용자 정보
     * @return 성공 유무
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    public ResVO deleteUser(String userId){
        ResVO result = mstrObject.deleteUser(userId);
        return result;
    }
}
