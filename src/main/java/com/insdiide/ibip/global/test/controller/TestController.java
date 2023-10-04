package com.insdiide.ibip.global.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {


    @GetMapping("/test2")
    private String test2(){

        return "/test2";
    }

    @GetMapping("/test3")
    private String test3(){
        return "/test3";
    }

    @GetMapping("/test4")
    private String test4(){
        return "/prompt/elementPrompt";
    }

}
