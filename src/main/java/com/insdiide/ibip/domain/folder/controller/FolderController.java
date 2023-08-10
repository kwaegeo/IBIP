package com.insdiide.ibip.domain.folder.controller;

import com.insdiide.ibip.domain.login.vo.FolderVO;
import com.insdiide.ibip.domain.login.vo.ResponseVO;
import com.microstrategy.web.objects.*;
import com.microstrategy.webapi.EnumDSSXMLObjectTypes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class FolderController {

    @GetMapping("/folder/subList")
    @ResponseBody
    public List<FolderVO> subFolder(@RequestParam(name = "folderId") String folderId, HttpServletRequest request) throws WebObjectsException {

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        WebObjectsFactory factory = WebObjectsFactory.getInstance();
        WebIServerSession serverSession = factory.getIServerSession();


        serverSession.setSessionID(mstrSessionId);
        List<FolderVO> folderList = new ArrayList<>();


        //1. 공유 리포트 하위 목록 싹 가져오기
        WebFolder folder = (WebFolder) factory.getObjectSource().getObject(folderId, EnumDSSXMLObjectTypes.DssXmlTypeFolder);
        folder.populate();
        System.out.println(folder);

        System.out.println(folder.getDisplayName());

        for(int i=0; i<folder.getChildCount(); i++){
            folderList.add(FolderVO.builder()
                    .id(folder.get(i).getID())
                            .name(folder.get(i).getName())
                            .tp(folder.get(i).getType()).build());
            System.out.println(folder.get(i));

        }

        return folderList;
    }

    @GetMapping("/folder/report")
    @ResponseBody
    public String openReport(@RequestParam(name = "reportId") String reportId, HttpServletRequest request) throws WebObjectsException {
        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        WebObjectsFactory factory = WebObjectsFactory.getInstance();
        WebIServerSession serverSession = factory.getIServerSession();
        serverSession.setSessionID(mstrSessionId);


        // Return session
        StringBuilder urlSB = new StringBuilder();
        urlSB.append("http").append("://").append("192.168.70.245:8090"); //Web Server name and port
        urlSB.append("/MicroStrategy/servlet/mstrWeb");
        urlSB.append("?server=").append("192.168.70.245"); //I Server name
        // urlSB.append("&port=0");
        urlSB.append("&project=").append("MicroStrategy+Tutorial"); // Project name
        urlSB.append("&evt=").append(4001);
        urlSB.append("&reportID=").append(reportId); //Report ID
        urlSB.append("&reportViewMode=").append(1);
        urlSB.append("&src=mstrWeb.").append("reportNoHeaderNoFooterNoPath").append(".").append(4001);
        urlSB.append("&usrSmgr=").append(serverSession.saveState(0));


        System.out.println(urlSB.toString());

        return urlSB.toString();
    }

    @GetMapping("/folder/document")
    @ResponseBody
    public String openDocument(@RequestParam(name = "documentId") String documentId, HttpServletRequest request) throws WebObjectsException {
        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        WebObjectsFactory factory = WebObjectsFactory.getInstance();
        WebIServerSession serverSession = factory.getIServerSession();
        serverSession.setSessionID(mstrSessionId);


        // Return session
        StringBuilder urlSB = new StringBuilder();
        urlSB.append("http").append("://").append("192.168.70.245:8090"); //Web Server name and port
        urlSB.append("/MicroStrategy/servlet/mstrWeb");
        urlSB.append("?server=").append("192.168.70.245"); //I Server name
        // urlSB.append("&port=0");
        urlSB.append("&project=").append("MicroStrategy+Tutorial"); // Project name
        urlSB.append("&evt=").append(2048001);
        urlSB.append("&documentID=").append(documentId); //Report ID
        urlSB.append("&currentViewMedia=").append(1);
        urlSB.append("&src=mstrWeb").append(".").append(2048001);
        urlSB.append("&usrSmgr=").append(serverSession.saveState(0));


        System.out.println(urlSB.toString());

        return urlSB.toString();
    }


}
