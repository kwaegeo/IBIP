package com.insdiide.ibip.domain.admin.license.controller;

import com.insdiide.ibip.domain.admin.group.service.GroupService;
import com.insdiide.ibip.domain.admin.license.service.LicenseService;
import com.microstrategy.web.objects.WebObjectsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LicenseController {

    @Autowired
    private LicenseService licenseService;

    @GetMapping("/license")
    private String LicenseInfo() throws WebObjectsException {
        licenseService.getLicenseInfo();
        return "z";
    }
}
