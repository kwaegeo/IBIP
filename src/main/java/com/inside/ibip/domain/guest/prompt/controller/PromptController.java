package com.inside.ibip.domain.guest.prompt.controller;

import com.inside.ibip.domain.guest.folder.vo.FolderVO;
import com.microstrategy.web.objects.WebFolder;
import com.microstrategy.web.objects.WebIServerSession;
import com.microstrategy.web.objects.WebObjectsException;
import com.microstrategy.web.objects.WebObjectsFactory;
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
public class PromptController {

    @GetMapping("/prompt")
    @ResponseBody
    public List<FolderVO> searchPrompt(@RequestParam(name = "folderId") String folderId, HttpServletRequest request) throws WebObjectsException {

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

}
