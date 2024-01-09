package com.inside.ibip.domain.guest.admin.setting.controller;

import com.inside.ibip.global.utils.ComUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
public class SettingController {


    @Autowired
    private ComUtils comUtils;

    @GetMapping("/getSetting")
    public String getSetting(HttpServletRequest request, HttpServletResponse response, Model model){

        return "/admin/setting/sync";
    }


}
