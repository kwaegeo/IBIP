package com.insdiide.ibip.domain.admin.license.service;

import com.insdiide.ibip.global.mstr.MstrObject;
import com.microstrategy.web.objects.WebObjectsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LicenseService {

    @Autowired
    private MstrObject mstrObject;

    public void getLicenseInfo() throws WebObjectsException {
        mstrObject.getLicenseInfo();
    }
}
