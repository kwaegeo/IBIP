package com.insdiide.ibip.domain.main.controller;

import com.insdiide.ibip.domain.login.service.LoginService;
import com.insdiide.ibip.domain.login.vo.FolderVO;
import com.insdiide.ibip.domain.main.service.MainService;
import com.insdiide.ibip.domain.main.vo.SideBarItemVO;
import com.insdiide.ibip.global.exception.CustomException;
import com.insdiide.ibip.global.utils.ComUtils;
import com.microstrategy.web.objects.*;
import com.microstrategy.webapi.EnumDSSXMLFolderNames;
import com.microstrategy.webapi.EnumDSSXMLObjectTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.microstrategy.webapi.EnumDSSXMLFolderNames.DssXmlFolderNameProfileReports;

@Controller
public class MainController {

    @Autowired
    private MainService mainService;

    @Autowired
    private ComUtils comUtils;


@GetMapping("/sample")
public String sample(){
    return "/indexOri";
}




@GetMapping("/main")
public String getMainPage(HttpServletRequest request, Model model) throws WebObjectsException {

    //1. 사용자 세션 (MSTR) 유효성 검사
    //2. 좌측 리스트 메뉴 가져오기 (폴더명, 폴더ID)
    //3. 사용자 정보 가져오기 (이름, 계정ID)

    HttpSession httpSession = request.getSession(true);
    String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

    //1. 세션 체크
    try{
        comUtils.sessionCheck(mstrSessionId);
    }catch(CustomException ex){
        throw ex;
    }

    //2. 좌측 리스트 가져오기 (하위 목록 가져오는 함수)
    SideBarItemVO sideBarItems = mainService.getSideBarItems();


    WebObjectsFactory factory = WebObjectsFactory.getInstance();
    WebIServerSession serverSession = factory.getIServerSession();
    serverSession.setSessionID(mstrSessionId);

    System.out.println(serverSession.saveState(0));


    List<WebObjectInfo> lstMenu = new ArrayList<>();


    String shareReportId = factory.getObjectSource().getFolderID(EnumDSSXMLFolderNames.DssXmlFolderNamePublicReports);
    System.out.println(shareReportId);
    System.out.println("공유리포트 ID 확인");


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

    String myReportId = factory.getObjectSource().getFolderID(EnumDSSXMLFolderNames.DssXmlFolderNameProfileReports);

    WebFolder folder2 = (WebFolder) factory.getObjectSource().getObject(myReportId, EnumDSSXMLObjectTypes.DssXmlTypeFolder);
    folder2.populate();
    System.out.println(myReportId);
    System.out.println("집에가고싶긴해");
    WebObjectInfo myreport = folder2;

    //2. 내리포트 정보 가져와서 넣기

    model.addAttribute("myFolders",myreport);

    return "/index";
}



}
