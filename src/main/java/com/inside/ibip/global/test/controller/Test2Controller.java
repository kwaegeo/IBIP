package com.inside.ibip.global.test.controller;

import com.inside.ibip.global.test.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Test2Controller {

    @Autowired
    private TestService testService;

    @GetMapping("/testGetDB")
    public String getTestDB(){

       testService.testGet();

       return"/folder/zzz";
    }

    @GetMapping("/template")
    public String getTemplate(){
        return "/template/templateAdd";
    }


}
