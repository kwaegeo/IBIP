package com.insdiide.ibip.domain.admin.setting.controller;

import com.insdiide.ibip.domain.admin.notice.service.NoticeService;
import com.insdiide.ibip.domain.admin.notice.vo.NoticeVO;
import com.insdiide.ibip.domain.admin.setting.service.SettingService;
import com.insdiide.ibip.global.exception.CustomException;
import com.insdiide.ibip.global.utils.ComUtils;
import com.microstrategy.web.objects.WebObjectsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class SettingController {

    @Autowired
    private SettingService settingService;

    @Autowired
    private ComUtils comUtils;

    @GetMapping("/getSetting")
    public String getSetting(HttpServletRequest request, HttpServletResponse response, Model model){

        return "/admin/setting/sync";
    }


}
