package com.insdiide.ibip.domain.folder.controller;

import com.insdiide.ibip.domain.folder.service.FolderService;
import com.insdiide.ibip.domain.folder.vo.EntityVO;
import com.insdiide.ibip.domain.folder.vo.TopItemVO;
import com.insdiide.ibip.domain.login.vo.FolderVO;
import com.insdiide.ibip.domain.main.vo.UserInfoVO;
import com.insdiide.ibip.domain.prompt.vo2.PromptDataVO;
import com.insdiide.ibip.global.exception.CustomException;
import com.insdiide.ibip.global.utils.ComUtils;
import com.microstrategy.utils.json.JSONException;
import com.microstrategy.utils.json.XML;
import com.microstrategy.web.objects.*;
import com.microstrategy.webapi.EnumDSSXMLObjectTypes;
import com.microstrategy.webapi.EnumDSSXMLPrivilegeTypes;
import com.microstrategy.webapi.EnumDSSXMLStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
public class FolderController {

    @Autowired
    private FolderService folderService;

    @Autowired
    private ComUtils comUtils;


    //Object 파일 들 참조해서 따로 만들기
    //Prompt만 하면 사실 끝이긴한데
    //일단
    @GetMapping("/folder/subList")
    @ResponseBody
    public List<FolderVO> subFolder(@RequestParam(name = "folderId") String folderId, HttpServletRequest request) throws WebObjectsException {

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        WebObjectsFactory factory = WebObjectsFactory.getInstance();
        WebIServerSession serverSession = factory.getIServerSession();


        serverSession.setSessionID(mstrSessionId);
        List<FolderVO> folderList = new ArrayList<>();
        if (serverSession.checkUserPrivilege(EnumDSSXMLPrivilegeTypes.DssXmlPrivilegesUseServerAdmin)) {
            System.out.println("관리자입니다");
        } else {
            System.out.println("관리자가 아닙니다.");
        }

        //1. 공유 리포트 하위 목록 싹 가져오기
        WebFolder folder = (WebFolder) factory.getObjectSource().getObject(folderId, EnumDSSXMLObjectTypes.DssXmlTypeFolder);
        folder.populate();
        System.out.println(folder);

        System.out.println(folder.getDisplayName());

        for (int i = 0; i < folder.getChildCount(); i++) {
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
        WebReportInstance webReportInstance = serverSession.getFactory().getReportSource().getNewInstance(reportId);
        System.out.println(webReportInstance.pollStatus());
        System.out.println(webReportInstance.pollStatus());
        if(webReportInstance.pollStatus() != EnumDSSXMLStatus.DssXmlStatusPromptXML){
            System.out.println("프롬프트가 없는거임");
        }
        System.out.println(webPrompts.size());
        System.out.println(webPrompts);

        WebPrompt webPrompt = webPrompts.get(0);

        System.out.println("wait");

        if (webPrompt.getPromptType() == 1) {
            WebConstantPrompt constantPrompt = (WebConstantPrompt) webPrompt;
            String anserXml = constantPrompt.getAnswerXML();

        } else {
            WebElementsPrompt elementsPrompt = (WebElementsPrompt) webPrompt;
            System.out.println("이게 프롬프트의 ID인가?");
            System.out.println(elementsPrompt.getID());

            System.out.println("이게 에트리뷰트의 ID인가??");
            System.out.println(elementsPrompt.getOrigin().getID());
            System.out.println("아래가 닶");
            System.out.println(elementsPrompt.getShortAnswerXML());
            System.out.println(elementsPrompt.getShortAnswerXML(true));
            System.out.println(elementsPrompt.getShortPa());
            System.out.println(elementsPrompt.getAnswerXML());
            System.out.println(elementsPrompt);
            System.out.println("아래ㅠㅠ");

            System.out.println(elementsPrompt.getDescription());
            System.out.println("z");
            String anserXml = elementsPrompt.getAnswerXML();
            WebElementSource webElementSource = elementsPrompt.getOrigin().getElementSource();
            String attrId = elementsPrompt.getOrigin().getID(); // attribute id
            String cmt = webPrompt.getDescription();

            System.out.println("음");
            System.out.println(elementsPrompt.getTitle());
//        System.out.println(elementsPrompt.get);
            System.out.println(elementsPrompt.getSuggestedAnswers());

            System.out.println(elementsPrompt.getPIN());
            System.out.println(elementsPrompt.getKey());

            WebElements elementzs = elementsPrompt.getSuggestedAnswers();
            elementzs = webElementSource.getElements();

            for (int i=0; i<elementzs.size(); i++){
                WebElement element = elementzs.get(i);
                System.out.println("엘리먼트들이다~");
                System.out.println(element.getID());
                System.out.println(element.getElementID());
                System.out.println(element.getDisplayName());

            }


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
        urlSB.append("http").append("://").append("localhost:8090"); //Web Server name and port
        urlSB.append("/MicroStrategy/servlet/mstrWeb");
        urlSB.append("?server=").append("localhost"); //I Server name
        // urlSB.append("&port=0");
        urlSB.append("&project=").append("MicroStrategy+Tutorial"); // Project name
        urlSB.append("&evt=").append(4001);
        urlSB.append("&reportID=").append(reportId); //Report ID
        urlSB.append("&reportViewMode=").append(1);
//        urlSB.append("&Hiddensections=dockTop");
        urlSB.append("&src=mstrWeb.").append("reportNoHeaderNoFooterNoPath").append(".").append(4001);
        urlSB.append("&usrSmgr=").append(serverSession.saveState(0));


        System.out.println(urlSB.toString());

        return urlSB.toString();
    }

//    @GetMapping("/folder/report")
//    @ResponseBody
//    public List<PromptDataVO> openReport2(){
//
//        //Prompt가 있으면 Prompt들을 반환하고 없으면 리포트 URL을 반환하도록 해야한다.
//        //        List<PromptDataVO>랑 URL을 들고있는 객체를 만들거나 해야한다.
//
//    }


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
        urlSB.append("http").append("://").append("localhost:8090"); //Web Server name and port
        urlSB.append("/MicroStrategy/servlet/mstrWeb");
        urlSB.append("?server=").append("localhost"); //I Server name
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

    @GetMapping("/folder")
    public String getFolderPage(@RequestParam(name = "folderId") String folderId, HttpServletRequest request, Model model, HttpServletResponse response) throws WebObjectsException {

        //1. 세션 체크
        //2. 전달 받은 상위 폴더 ID 추출
        //2-1. 상위 폴더 하위의 모든 폴더 정보 가져오기
        //2-2. 전달 받은 폴더의 하위 모든 요소들 출력

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        try {
            comUtils.sessionCheck(mstrSessionId, request, response);
        } catch (CustomException ex) {
            throw ex;
        }

        /**
         * 상위 메뉴 조회
         * **/

        // 상위 폴더의 하위 폴더 정보 조회
        TopItemVO topItem = folderService.getTopItem(mstrSessionId, folderId);

        //전달 받은 폴더의 하위 목록 조회
//        List<EntityVO> subList = folderService.getSubList(mstrSessionId, folderId);

        /**
         * 왼쪽 사이드 메뉴 조회 (jstree) child yn이 따로 있는지?
         * **/

        //사용자 정보 조회
        UserInfoVO userInfo = comUtils.getUserInfo(mstrSessionId);

        //왼쪽 폴더메뉴 트리형태로 구현
        //펼치기, 눌렀을 때 펼쳐지는 것은 Ajax요청으로
        //처음에는 일단 그것만 있으면 되잖아(폴더의 하위목록전체 뽑는 것)


        model.addAttribute("topItem", topItem);
//        model.addAttribute("subList", subList);
        model.addAttribute("reqFolderId", folderId);
        model.addAttribute("userInfo", userInfo);

        return "/folder/folder";
    }

    @GetMapping("/getSubFolder")
    @ResponseBody
    public List<EntityVO> getSubFolder(@RequestParam(name = "folderId") String folderId, HttpServletRequest request, HttpServletResponse response) throws WebObjectsException {

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        try {
            comUtils.sessionCheck(mstrSessionId, request, response);
        } catch (CustomException ex) {
            throw ex;
        }
        //전달 받은 폴더의 하위 목록 조회
        List<EntityVO> subList = folderService.getSubList(mstrSessionId, folderId);
        return subList;
    }

    @GetMapping("/getRootFolder")
    @ResponseBody
    public List<EntityVO> getRootFolder(HttpServletRequest request, HttpServletResponse response) throws WebObjectsException {

        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        try {
            comUtils.sessionCheck(mstrSessionId, request, response);
        } catch (CustomException ex) {
            throw ex;
        }

        //전달 받은 폴더의 하위 목록 조회
        List<EntityVO> subList = folderService.getRootFolderList(mstrSessionId);
        return subList;
    }

    @GetMapping("/getUserFolder")
    @ResponseBody
    public List<EntityVO> getUserFolder(HttpServletRequest request, HttpServletResponse response) throws WebObjectsException {


        HttpSession httpSession = request.getSession(true);
        String mstrSessionId = (String) httpSession.getAttribute("mstrSessionId");

        //1. 세션 체크
        try {
            comUtils.sessionCheck(mstrSessionId, request, response);
        } catch (CustomException ex) {
            throw ex;
        }

        //전달 받은 폴더의 하위 목록 조회
        List<EntityVO> subList = folderService.getUserFolderList(mstrSessionId);
        return subList;

    }

//    @GetMapping("/userInfo")
//    @ResponseBody
//    public String userInfo() throws WebObjectsException {
//        folderService.getUserInfo();
//        return "zz";
//    }



}