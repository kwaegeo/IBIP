package com.insdiide.ibip.domain.folder.controller;

import com.insdiide.ibip.domain.login.vo.FolderVO;
import com.insdiide.ibip.domain.login.vo.ResponseVO;
import com.insdiide.ibip.domain.prompt.VO.PromptDataVO;
import com.microstrategy.utils.json.JSONException;
import com.microstrategy.utils.json.XML;
import com.microstrategy.web.objects.*;
import com.microstrategy.webapi.EnumDSSXMLObjectTypes;
import com.microstrategy.webapi.EnumDSSXMLPrivilegeTypes;
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
        if(serverSession.checkUserPrivilege(EnumDSSXMLPrivilegeTypes.DssXmlPrivilegesUseServerAdmin)){
            System.out.println("관리자입니다");
        }
        else {
           System.out.println("관리자가 아닙니다.");
        }

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
    public String openReport(@RequestParam(name = "reportId") String reportId, HttpServletRequest request) throws WebObjectsException, JSONException {
        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        WebObjectsFactory factory = WebObjectsFactory.getInstance();
        WebIServerSession serverSession = factory.getIServerSession();
        serverSession.setSessionID(mstrSessionId);

        WebPrompts webPrompts = serverSession.getFactory().getReportSource().getNewInstance(reportId).getPrompts();

        System.out.println(webPrompts);

        WebPrompt webPrompt = webPrompts.get(0);

        System.out.println("wait");

        if (webPrompt.getPromptType() == 1){
            WebConstantPrompt constantPrompt = (WebConstantPrompt) webPrompt;
            String anserXml = constantPrompt.getAnswerXML();

        }
        else {
            WebElementsPrompt elementsPrompt = (WebElementsPrompt) webPrompt;
            String anserXml = elementsPrompt.getAnswerXML();
            WebElementSource webElementSource = elementsPrompt.getOrigin().getElementSource();
            String attrId = elementsPrompt.getOrigin().getID(); // attribute id
            String cmt = webPrompt.getDescription();

            System.out.println("음");
            System.out.println(elementsPrompt.getTitle());
//        System.out.println(elementsPrompt.get);


            System.out.println(anserXml);
            elementsPrompt.populate();
            String xml = webElementSource.getElementsXML();
            System.out.println(webElementSource.getElements().get(0));

            com.microstrategy.utils.json.JSONObject jo = XML.toJSONObject(xml);
            com.microstrategy.utils.json.JSONObject mi = null;
            com.microstrategy.utils.json.JSONObject es = null;

            if (jo != null) {
                mi = (com.microstrategy.utils.json.JSONObject) jo.get("mi");
                if (mi != null) {
                    es = (com.microstrategy.utils.json.JSONObject) mi.get("es");
                    if (es != null) {
                        int totCnt = Integer.parseInt(es.get("cc").toString());
                    }
                }
            }
            List<PromptDataVO> promptData = new ArrayList<PromptDataVO>();
            WebElements elements = webElementSource.getElements();

            for (int i = 0; i < elements.size(); i++) { // option 먼저 create

                WebElement element = elements.get(i);
                PromptDataVO elementInfo = new PromptDataVO();
                elementInfo.setEid(element.getID());

                elementInfo.setName(element.getDisplayName());
                elementInfo.setOrgName(element.getDisplayName());

                elementInfo.setType(element.getElementType());
                elementInfo.setSelected(element.isSelected());
                elementInfo.setOid(element.getID().split(":")[0]);
                elementInfo.setEno(element.getID().split(":")[1]);
                elementInfo.setPageCnt(1);

                promptData.add(elementInfo);
            }
//        System.out.println(jo);
//        System.out.println(mi);
//        System.out.println(es);
            System.out.println(promptData);
        }

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
        urlSB.append("&src=mstrWeb").append("NoHeaderNoFooterNoPath.").append(2048001);
        urlSB.append("&usrSmgr=").append(serverSession.saveState(0));

        //        urlSB.append("&hiddensections=dockTop,dockLeft"); 조회 모드일 경우에 해당 옵션 추가

        System.out.println(urlSB.toString());

        return urlSB.toString();
    }


}
