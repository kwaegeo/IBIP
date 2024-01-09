package com.inside.ibip.domain.admin.license.service;

import com.inside.ibip.domain.admin.license.vo.LicenseVO;
import com.inside.ibip.global.mstr.MstrObject;
import com.microstrategy.web.objects.WebObjectsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LicenseService {

    @Autowired
    private MstrObject mstrObject;

    public List<LicenseVO> getLicenseList() throws WebObjectsException {
        List<LicenseVO> licenseList = mstrObject.getLicenseList();
        return licenseList;
    }

    public LicenseVO getLicenseInfo(int licenseType) throws WebObjectsException {
        LicenseVO licenseInfo = mstrObject.getLicenseInfo(licenseType);
//        licenseInfo = mstrObject.getUserGroupList(licenseInfo);
        return licenseInfo;
    }
}
