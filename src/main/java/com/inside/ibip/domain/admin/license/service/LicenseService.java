package com.inside.ibip.domain.admin.license.service;

import com.inside.ibip.domain.admin.license.vo.LicenseVO;
import com.inside.ibip.global.mstr.MstrObject;
import com.microstrategy.web.objects.WebObjectsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @FileName     : LicenseService.java
 * @Date         : 2023.12.01
 * @Author       : 이도현
 * @Description  : 라이센스 Service, 라이센스 리스트 조회, 정보 조회, 생성, 수정, 삭제
 * @History
 * =======================================================
 *   DATE			AUTHOR			NOTE
 * =======================================================
 *   2023.12.01     이도현         최초작성
 *
 */
@Service
public class LicenseService {

    @Autowired
    private MstrObject mstrObject;

    /**
     * 라이센스 리스트를 조회한다.
     * @Method Name   : getLicenseList
     * @Date / Author : 2023.12.01  이도현
     * @return 라이센스 객체 리스트
     * @History
     * 2023.12.01	최초생성
     */
    public List<LicenseVO> getLicenseList(){
        List<LicenseVO> licenseList = mstrObject.getLicenseList();
        return licenseList;
    }

    /**
     * 라이센스 정보 조회
     * @Method Name   : getLicenseList
     * @Date / Author : 2023.12.01  이도현
     * @param licenseType 라이센스 타입
     * @return 라이센스 객체 리스트
     * @History
     * 2023.12.01	최초생성
     */
    public LicenseVO getLicenseInfo(int licenseType){
        LicenseVO licenseInfo = mstrObject.getLicenseInfo(licenseType);
//        licenseInfo = mstrObject.getUserGroupList(licenseInfo);
        return licenseInfo;
    }

}
