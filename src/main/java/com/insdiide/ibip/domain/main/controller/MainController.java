package com.insdiide.ibip.domain.main.controller;

import com.microstrategy.web.objects.*;
import com.microstrategy.webapi.EnumDSSXMLObjectTypes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {

@GetMapping("/sample")
public String sample(){
    return "/indexOri";
}




@GetMapping("/main")
public String main(HttpServletRequest request, Model model) throws WebObjectsException {

    HttpSession httpSession = request.getSession(true);
    String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

    WebObjectsFactory factory = WebObjectsFactory.getInstance();
    WebIServerSession serverSession = factory.getIServerSession();

    serverSession.setSessionID(mstrSessionId);

    List<WebObjectInfo> lstMenu = new ArrayList<>();


    //1. 공유 리포트 하위 목록 싹 가져오기
    WebFolder folder = (WebFolder) factory.getObjectSource().getObject("D3C7D461F69C4610AA6BAA5EF51F4125", EnumDSSXMLObjectTypes.DssXmlTypeFolder);
    folder.populate();
    System.out.println(folder);

    System.out.println(folder.getDisplayName());

    for(int i=0; i<folder.getChildCount(); i++){
        if(folder.get(i).getType() == EnumDSSXMLObjectTypes.DssXmlTypeFolder) {
            lstMenu.add(folder.get(i));
            System.out.println(folder.get(i));
        }
    }

    System.out.println("이러면 되는거아니냐");
    System.out.println(lstMenu);

    System.out.println(lstMenu.get(0).getID());
    System.out.println(lstMenu.get(0).getName());


    model.addAttribute("shareFolders", lstMenu);


    WebFolder folder2 = (WebFolder) factory.getObjectSource().getObject("8D67909211D3E4981000E787EC6DE8A4", EnumDSSXMLObjectTypes.DssXmlTypeFolder);
    folder2.populate();
    System.out.println(folder2);
    WebObjectInfo myreport = folder2;

    //2. 내리포트 정보 가져와서 넣기

    model.addAttribute("myFolders",myreport);

    return "/index";
}



}
