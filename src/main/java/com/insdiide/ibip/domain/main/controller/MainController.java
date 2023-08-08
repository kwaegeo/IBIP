package com.insdiide.ibip.domain.main.controller;

import com.microstrategy.web.objects.WebIServerSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class MainController {

    private WebIServerSession webIServerSession;

@GetMapping("/main")
public String main(HttpServletRequest request, Model model){

    HttpSession httpSession = request.getSession(true);
    String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

    webIServerSession.setSessionID(mstrSessionId);

//    MstrObjet mstrObjet = new MstrObject();


    return "/index";
}



}
