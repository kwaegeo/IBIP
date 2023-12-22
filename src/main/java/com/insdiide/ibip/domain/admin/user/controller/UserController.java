package com.insdiide.ibip.domain.admin.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {

    @GetMapping("/aaa.null")
    @ResponseBody
    public String aacsac(){
        return "ㅇ";
    }
}
