package com.insdiide.ibip.domain.test.controller;

import com.insdiide.ibip.domain.test.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Test2Controller {

    @Autowired
    private TestService testService;

    @GetMapping("/testGetDB")
    public String getTestDB(){

       testService.testGet();

       return"/folder/zzz";

    }
}
