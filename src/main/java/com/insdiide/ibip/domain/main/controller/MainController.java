package com.insdiide.ibip.domain.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MainController {

@GetMapping("/main")
public String main(HttpServletRequest request, Model model){



    return "/index";
}



}
