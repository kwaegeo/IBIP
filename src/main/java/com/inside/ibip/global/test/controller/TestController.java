package com.inside.ibip.global.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {


    @GetMapping("/test2")
    private String test2(){

        return "404";
    }
    @GetMapping("/OJT/main")
    private String main(){
        return "/test/main";
    }

    @GetMapping("/OJT/news")
    private String news(){
        return "/test/news";
    }

    @GetMapping("/OJT/news2")
    private String news2(){
        return "/test/news2";
    }


    @GetMapping("/OJT/content")
    private String content(){
        return "/test/content";
    }

    @GetMapping("/test3")
    private String test3(){
        return "/test3";
    }

    @GetMapping("/test4")
    private String test4(){
        return "/prompt/elementPrompt";
    }

    @GetMapping("/exod")
    private String exod(){
        return "/exod_test";
    }
}
