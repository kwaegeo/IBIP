package com.insdiide.ibip.global.mstr;

import com.insdiide.ibip.domain.folder.vo.EntityVO;
import com.insdiide.ibip.domain.admin.group.vo.GroupVO;
import com.insdiide.ibip.domain.login.vo.FolderVO;
import com.insdiide.ibip.domain.main.vo.SearchResultVO;
import com.insdiide.ibip.domain.main.vo.SearchVO;
import com.insdiide.ibip.domain.main.vo.UserInfoVO;
import com.insdiide.ibip.domain.prompt.vo.PromptVO;
import com.insdiide.ibip.domain.report.vo.ReportVO;
import com.insdiide.ibip.global.mstr.prompt.ConstantPrompt;
import com.insdiide.ibip.global.mstr.prompt.ElementPrompt;
import com.insdiide.ibip.global.mstr.prompt.ObjectPrompt;
import com.microstrategy.web.objects.*;
import com.microstrategy.web.objects.admin.users.WebUser;
import com.microstrategy.web.objects.admin.users.WebUserGroup;
import com.microstrategy.web.objects.admin.users.WebUserList;
import com.microstrategy.webapi.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@Component
@Log4j2
public class MstrObject extends MstrSession{

    /**
     * MSTR Object factory
     */
    protected WebObjectsFactory factory		= WebObjectsFactory.getInstance();

    public WebIServerSession serverSession;

    //세션 정보 입력
    public void setSession(String mstrSessionId){
        serverSession = factory.getIServerSession();
        serverSession.setSessionID(mstrSessionId);
    }


    //하위 폴더 요소 가져오기
    public List<FolderVO> getSubfolderList(String folderId) throws WebObjectsException {
        List<FolderVO> subFolderList = new ArrayList<>();
        WebFolder folder = (WebFolder) factory.getObjectSource().getObject(folderId, EnumDSSXMLObjectTypes.DssXmlTypeFolder);
        folder.populate();
        for(int i=0; i<folder.getChildCount(); i++){
            if(folder.get(i).getType() == EnumDSSXMLObjectTypes.DssXmlTypeFolder) {
                subFolderList.add(new FolderVO(folder.get(i).getName(), folder.get(i).getID(), folder.get(i).getType()));
                System.out.println(folder.get(i));
            }
        }
        return subFolderList;
    }

    //하위 폴더 요소 가져오기
    public List<EntityVO> getRootFolderList(String folderId) throws WebObjectsException {
        List<EntityVO> rootFolderList = new ArrayList<>();
        WebFolder folder = (WebFolder) factory.getObjectSource().getObject(folderId, EnumDSSXMLObjectTypes.DssXmlTypeFolder);
        folder.populate();
        for(int i=0; i<folder.getChildCount(); i++){
            if(folder.get(i).getType() == EnumDSSXMLObjectTypes.DssXmlTypeFolder) {
                rootFolderList.add(new EntityVO(folder.get(i).getID(), folder.get(i).getName(), "#", folder.get(i).getType(), true));
                System.out.println(folder.get(i));
            }
        }
        System.out.println(rootFolderList + "zz");
        return rootFolderList;
    }

    //폴더 ID 불러오기
    public String getFolderId(int folderNum) throws WebObjectsException {
        String folderId = factory.getObjectSource().getFolderID(folderNum);
        return folderId;
    }

    //부모의 폴더 ID 불러오기
    public String getParentFolderId(String reqFolderId) throws WebObjectsException{
        WebFolder folder = (WebFolder) factory.getObjectSource().getObject(reqFolderId, EnumDSSXMLObjectTypes.DssXmlTypeFolder);
        WebFolder parent = folder.getParent();
        System.out.println(parent);
        return parent.getID();
    }

    //폴더 정보 불러오기
    public FolderVO getFolderInfo(String folderId) throws WebObjectsException {
        FolderVO folderInfo = new FolderVO();
        WebFolder folder = (WebFolder) factory.getObjectSource().getObject(folderId, EnumDSSXMLObjectTypes.DssXmlTypeFolder);
        System.out.println(folder);
        folderInfo.setName(folder.getName());
        folderInfo.setId(folder.getID());
        folderInfo.setTp(folder.getType());
        return folderInfo;
    }

    //사용자 정보 불러오기
    public UserInfoVO getUserInfo() throws WebObjectsException {
        UserInfoVO userInfo = new UserInfoVO();
        WebUser webUser = (WebUser)serverSession.getUserInfo();
        userInfo.setUserId(webUser.getID());
        userInfo.setUserName(webUser.getName());

        if(serverSession.checkUserPrivilege(EnumDSSXMLPrivilegeTypes.DssXmlPrivilegesUseServerAdmin)&& serverSession.checkUserPrivilege(EnumDSSXMLPrivilegeTypes.DssXmlPrivilegesWebAdministrator)){
            userInfo.setAdminYn("Y");
        }
        return userInfo;
    }

    //하위 전체 요소 조회 (TreeVO로 저장)
//    public List<EntityVO> getSubList(String folderId, String parentId) throws WebObjectsException {
//        List<EntityVO> subList = new ArrayList<>();
//        WebFolder folder = (WebFolder) factory.getObjectSource().getObject(folderId, EnumDSSXMLObjectTypes.DssXmlTypeFolder);
//        String currentParentId = parentId.equals("") ? "#" : parentId;
//        folder.populate();
//        if(currentParentId == "#"){
//            subList.add(new EntityVO(folder.getID(), folder.getName(), currentParentId, folder.getType(), false));
//        }
//
//        for(int i=0; i<folder.getChildCount(); i++){
//                subList.add(new EntityVO(folder.get(i).getID(), folder.get(i).getName(), folder.get(i).getParent().getID(), folder.get(i).getType(), false));
//
//                if(folder.get(i).getType() == 8){
//                    folder.get(i).populate();
//                    if(folder.get(i).getChildUnits().size() > 0){
//                        List<EntityVO> childSubList = getSubList(folder.get(i).getID(), folder.get(i).getParent().getID());
//                        subList.addAll(childSubList);
//                    }
//                }
//        }
//        System.out.println(subList);
//        return subList;
//    }

    public List<EntityVO> getSubList(String folderId, String parentId, List<EntityVO> subList) throws WebObjectsException {

        WebFolder folder = (WebFolder) factory.getObjectSource().getObject(folderId, EnumDSSXMLObjectTypes.DssXmlTypeFolder);
        folder.populate();

        for(int i =0; i<folder.getChildCount(); i++){
            if(folder.get(i).getType() == 8){
                folder.get(i).populate();
                System.out.println(folder.get(i).getChildUnits().size());
                if(folder.get(i).getChildUnits().size()>0){
                    getSubList(folder.get(i).getID(), folder.get(i).getParent().getID(), subList);
                }
            }
            subList.add(new EntityVO(folder.get(i).getID(), folder.get(i).getName(), folder.get(i).getParent().getID(), folder.get(i).getType(), false));
        }
        System.out.println(subList);
        return subList;
    }



    public String getUsrSmgr(){
        String usrSmgr = serverSession.saveState(0);
        return usrSmgr;
    }


    public SearchVO searchReport(String searchKeyword) throws WebObjectsException {
        WebSearch webSearch = factory.getObjectSource().getNewSearchObject();
        webSearch.setNamePattern("*" + searchKeyword + "*");
        webSearch.setSearchFlags(webSearch.getSearchFlags() + EnumDSSXMLSearchFlags.DssXmlSearchNameWildCard + EnumDSSXMLSearchFlags.DssXmlSearchRootRecursive);
        webSearch.setAsync(false);
        webSearch.submit();
        WebFolder objectResult = webSearch.getResults();
        System.out.println(objectResult);

        SearchVO search = new SearchVO();
        List<SearchResultVO> searchList = new ArrayList<>();

        for(int i=0; i< objectResult.size(); i++){
            WebObjectInfo woi = objectResult.get(i);
            woi.populate();

            if(woi.getType() == 3 || woi.getType() == 55){
                SimpleList lst = woi.getAncestors(); //Path 경로 넣는 것

                String reportPath = "";
                String reportName = woi.getName();
                for(int j=0; j< lst.size(); j++){
                    WebObjectInfo obj = (WebObjectInfo)lst.item(j);
                    System.out.println(obj.getName());
                    if ("".equals(reportPath)){
                        reportPath += obj.getName();
                    }
                    else {
                        reportPath += " > " + obj.getName();
                    }
                }
                reportPath += " > " + reportName;
                System.out.println(reportPath);
                SearchResultVO searchResult = new SearchResultVO(
                        woi.getID(),
                        woi.getName(),
                        woi.getType(),
                        woi.getOwner().getName(),
                        woi.getCreationTime(),
                        reportPath
                );
                searchList.add(searchResult);
            }
        }

        search.setSearchList(searchList);
        return search;

    }


    //리포트 정보 가져오기 (리포트 정보만)
    public ReportVO getReportInfo(String reportId, String documentType) throws WebObjectsException {
        ReportVO reportInfo = new ReportVO();
        reportInfo.setReportId(reportId);
        reportInfo.setDocumentType(documentType);

        WebObjectSource objectSource = this.serverSession.getFactory().getObjectSource();
        int dssObjectTypes;

        if("D".equals(documentType)){
            dssObjectTypes = EnumDSSXMLObjectTypes.DssXmlTypeDocumentDefinition;
        }
        else{
            dssObjectTypes = EnumDSSXMLObjectTypes.DssXmlTypeReportDefinition;
        }
        WebObjectInfo report = objectSource.getObject(reportId, dssObjectTypes);

        report.populate();
        String reportName = report.getName();
        reportInfo.setReportNm(reportName);

        SimpleList lst = report.getAncestors();
        String reportPath = "";
        for(int i=0; i< lst.size()-3; i++){
            WebObjectInfo obj = (WebObjectInfo)lst.item(i+3);
            System.out.println(obj.getName());
            if ("".equals(reportPath)){
              reportPath += obj.getName();
            }
            else {
                reportPath += " > " + obj.getName();
            }
        }
        reportPath += " > " + reportName;
        reportInfo.setReportPath(reportPath);

        System.out.println(reportPath);

        return reportInfo;
    }



    // 리포트 데이터 정보 가져오기 (프롬프트 데이터 포함) (유형에 맞게 나눠서)
    public ReportVO getReportDataInfo(String reportId, ReportVO reportInfo) throws WebObjectsException {

        WebResultSetInstance webReportInstance = null;

        if("D".equals(reportInfo.getDocumentType())){
            webReportInstance = (WebDocumentInstance) serverSession.getFactory().getDocumentSource().getNewInstance(reportId);
        }
        else{
            webReportInstance = (WebReportInstance) serverSession.getFactory().getReportSource().getNewInstance(reportId);
        }

        //프롬프트가 있는지 없는지 확인 없으면 return
        if(webReportInstance.pollStatus() != EnumDSSXMLStatus.DssXmlStatusPromptXML){
            reportInfo.setPromptExist("N");
            return reportInfo;
        }

        //프롬프트 사이즈가 0이 아니라면 (1개 이상이라면) 있다
        reportInfo.setPromptExist("Y");

        //프롬프트들의 객체 생성
        WebPrompts webPrompts = webReportInstance.getPrompts();

        List<PromptVO> promptList = new ArrayList<>();

        System.out.println(webPrompts.size());
        //프롬프트 개수만큼 반복
        for(int i=0; i<webPrompts.size(); i++) {

            //프롬프트 객체 생성
            WebPrompt webPrompt = webPrompts.get(i);
            PromptVO prompt = new PromptVO();
            System.out.println("타입이?");
            System.out.println(webPrompt.getPromptType());


            /**
             *
             * prompt 타입에 따라 분기 처리
             * TYPE = 1 날짜 프롬프트
             * TYPE = 2 구성요소 프롬프트 (화면 꾸려야 하니까 먼저 처리)
             * TYPE = 3 계층 프롬프트 (잠시 보류) 계층의 경우 파악 먼저
             * TYPE = 4 개체 프롬프트
             * **/
            if (webPrompt.getPromptType() == EnumWebPromptType.WebPromptTypeConstant) { //값 프롬프트
                ConstantPrompt constantPrompt = new ConstantPrompt();
                prompt = constantPrompt.getConstantPromptInfo(prompt, webPrompt);
                System.out.println(prompt);

            } else if (webPrompt.getPromptType() == EnumWebPromptType.WebPromptTypeElements) { // 구성요소 프롬프트
                ElementPrompt elementPrompt = new ElementPrompt();
                prompt = elementPrompt.getElementPromptInfo(prompt, webPrompt);
                System.out.println(prompt);

            } else if (webPrompt.getPromptType() == 3) { // 계층 프롬프트
                //계층 프롬프트 개발 전

            } else if (webPrompt.getPromptType() == EnumWebPromptType.WebPromptTypeObjects) { // 개체 프롬프트
                ObjectPrompt objectPrompt = new ObjectPrompt();
                prompt = objectPrompt.getObjectPromptInfo(prompt, webPrompt);
                System.out.println(prompt);
            }
            promptList.add(prompt);
        }
            reportInfo.setPrompts(promptList);
            return reportInfo;
        }

    public List<GroupVO> getGroupList() throws WebObjectsException {

//        WebObjectInfo woi = factory.getObjectSource().getObject("73F7482611D3596C60001B8F67019608", EnumDSSXMLObjectTypes.DssXmlTypeFolder);
//        woi.populate();
//
//        woi.
//        WebUserGroup group = (WebUserGroup) woi;
//        WebUserList members = group.getMembers();
//        Enumeration userEnum = members.elements();
//        WebUser user = null;
//        int a=1;
//        while(userEnum.hasMoreElements()){
//            a++;
//            user = (WebUser) userEnum.nextElement();
//            System.out.println("User member = " + user.getName() +
//                    " enabled: " + user.isEnabled());
//        }
//        System.out.println(a);

        WebObjectSource wos = factory.getObjectSource();
        WebSearch search = wos.getNewSearchObject();
        search.setDisplayName("*");
        search.setSearchFlags(search.getSearchFlags() | EnumDSSXMLSearchFlags.DssXmlSearchAbbreviationWildCard);
        search.setAsync(false);

//        search.types().add(new Integer(EnumDSSXMLObjectSubTypes.DssXmlSubTypeUser));
//        search.setDomain(EnumDSSXMLSearchDomain.DssXmlSearchDomainRepository);
//        search.types().add(new Integer(EnumDSSXMLObjectSubTypes.DssXmlSubTypeUserGroup));

        //search.setDomain(EnumDSSXMLSearchDomain.DssXmlSearchDomainConfiguration);
        search.types().add(new Integer(EnumDSSXMLObjectTypes.DssXmlTypeUser));

        search.setSearchRoot("73F7482611D3596C60001B8F67019608");
//        search.setDomain(EnumDSSXMLSearchDomain.DssXmlSearchConfigurationAndAllProjects);
        search.submit();

        WebFolder f = search.getResults();
//        f.populate();
        System.out.println("After search: " + f.size());

        WebObjectInfo objectInfo = null;

        if (f.size() > 0) {
            for (int i = 0; i < f.size(); i++) {
                objectInfo = f.get(i);
                System.out.println(objectInfo.getDisplayName());
            }
        }
        return new ArrayList<>();
    }

    public List<GroupVO> getGrupList() throws WebObjectsException {

        //ObjectSourcec 객체 생성
        WebObjectSource objectSource = factory.getObjectSource();

        //MicroStrategy Groups의 ID를 가지고 User WebObjectInfo로 변경
        WebObjectInfo woi = objectSource.getObject("3D0F5EF8978D4AE086012C196BF01EBA" ,EnumDSSXMLObjectTypes.DssXmlTypeUser);

        //채워넣기
        woi.populate();

        // 사용자 그룹 객체
        WebUserGroup groups = (WebUserGroup) woi;

        //MicroStrategy Groups하위의 모든 그룹 가져오기위한 UserList 객체 초기화
        WebUserList members = groups.getMembers();

        //하위 요소들을 Enumeration으로 치환
        Enumeration  enumeration = members.elements();

        //WebUserGroup 객체 선언
        WebUserGroup group = null;
        List<GroupVO> groupList = new ArrayList<>();

        // 객체 수 만큼 반복
        while(enumeration.hasMoreElements()){
            //그룹 채워넣기
            group = (WebUserGroup) enumeration.nextElement();
            group.populate();

            if(group.isGroup()){
                groupList.add(GroupVO.builder().
                        groupId(group.getID()).
                        groupNm(group.getName()).
                        childCnt(group.getTotalChildCount()).
                        description(group.getDescription()).
                        creationTime("group.getCreationTime()").
                        owner(group.getOwner().getName()).
                        build()
                );
            }
        }
        return groupList;
    }
}
