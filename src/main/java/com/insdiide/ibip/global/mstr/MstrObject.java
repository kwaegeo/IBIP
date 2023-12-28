package com.insdiide.ibip.global.mstr;

import com.insdiide.ibip.domain.admin.role.vo.RoleVO;
import com.insdiide.ibip.domain.admin.user.vo.UserVO;
import com.insdiide.ibip.domain.folder.vo.EntityVO;
import com.insdiide.ibip.domain.admin.group.vo.GroupVO;
import com.insdiide.ibip.domain.login.vo.FolderVO;
import com.insdiide.ibip.domain.main.vo.SearchResultVO;
import com.insdiide.ibip.domain.main.vo.SearchVO;
import com.insdiide.ibip.domain.main.vo.UserInfoVO;
import com.insdiide.ibip.domain.prompt.vo.PromptVO;
import com.insdiide.ibip.domain.report.vo.ReportVO;
import com.insdiide.ibip.global.exception.CustomException;
import com.insdiide.ibip.global.exception.code.ResultCode;
import com.insdiide.ibip.global.mstr.prompt.ConstantPrompt;
import com.insdiide.ibip.global.mstr.prompt.ElementPrompt;
import com.insdiide.ibip.global.mstr.prompt.ObjectPrompt;
import com.insdiide.ibip.global.vo.ResVO;
import com.microstrategy.web.beans.BeanFactory;
import com.microstrategy.web.beans.UserBean;
import com.microstrategy.web.beans.UserGroupBean;
import com.microstrategy.web.beans.WebBeanException;
import com.microstrategy.web.objects.*;
import com.microstrategy.web.objects.admin.licensing.*;
import com.microstrategy.web.objects.admin.users.*;
import com.microstrategy.webapi.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import static com.insdiide.ibip.global.utils.StringUtils.*;

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

    public void testUser() throws WebObjectsException {
        WebObjectInfo user = serverSession.getUserInfo();
        System.out.println(user.getAbbreviation());
        System.out.println(user.getComments());
        System.out.println(user._getObKey());
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
    public UserInfoVO getUserInfo() {
        UserInfoVO userInfo = new UserInfoVO();
        try {
            WebUser webUser = (WebUser) serverSession.getUserInfo();
            userInfo.setUserId(webUser.getID());
            userInfo.setUserName(webUser.getName());

            if (serverSession.checkUserPrivilege(EnumDSSXMLPrivilegeTypes.DssXmlPrivilegesUseServerAdmin) && serverSession.checkUserPrivilege(EnumDSSXMLPrivilegeTypes.DssXmlPrivilegesWebAdministrator)) {
                userInfo.setAdminYn("Y");
            }
        }catch (WebObjectsException e){
            throw new CustomException(ResultCode.MSTR_NO_SESSION);
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

    public List<GroupVO> getGroupTest() throws WebObjectsException {

        WebObjectSource wos = factory.getObjectSource();
        WebSearch search = wos.getNewSearchObject();
        search.setNamePattern("*" + "" + "*");
        search.setSearchFlags(search.getSearchFlags() + EnumDSSXMLSearchFlags.DssXmlSearchNameWildCard + EnumDSSXMLSearchFlags.DssXmlSearchRootRecursive);

        search.setAsync(false);
        search.types().add(EnumDSSXMLObjectSubTypes.DssXmlSubTypeUserGroup);
        search.setDomain(EnumDSSXMLSearchDomain.DssXmlSearchDomainConfiguration);

        search.submit();

        WebFolder f = search.getResults();
//        f.populate();
        System.out.println("After search: " + f.size());

        if (f.size() > 0) {
            for (int i = 0; i < f.size(); i++) {
                WebUserGroup groups= (WebUserGroup) f.get(i);
                System.out.println(groups.getDisplayName());
            }
        }
        return new ArrayList<>();
    }

//    public List<GroupVO> getGroupList() throws WebObjectsException {
//
//        //ObjectSourcec 객체 생성
//        WebObjectSource objectSource = factory.getObjectSource();
//
//        //MicroStrategy Groups의 ID를 가지고 User WebObjectInfo로 변경
//        WebObjectInfo woi = objectSource.getObject("3D0F5EF8978D4AE086012C196BF01EBA" ,EnumDSSXMLObjectTypes.DssXmlTypeUser);
//
//        //채워넣기
//        woi.populate();
//
//        // 사용자 그룹 객체
//        WebUserGroup groups = (WebUserGroup) woi;
//
//        //MicroStrategy Groups하위의 모든 그룹 가져오기위한 UserList 객체 초기화
//        WebUserList members = groups.getMembers();
//
//        //하위 요소들을 Enumeration으로 치환
//        Enumeration  enumeration = members.elements();
//
//        //WebUserGroup 객체 선언
//        WebUserGroup group = null;
//        List<GroupVO> groupList = new ArrayList<>();
//
//        // 객체 수 만큼 반복
//        while(enumeration.hasMoreElements()){
//            //그룹 채워넣기
//            group = (WebUserGroup) enumeration.nextElement();
//            group.populate();
//
//            if(group.isGroup()){
//                groupList.add(GroupVO.builder().
//                        groupId(group.getID()).
//                        groupNm(group.getName()).
//                        childCnt(group.getTotalChildCount()).
//                        description(group.getDescription()).
//                        creationTime(group.getCreationTime()).
//                        owner(group.getOwner().getName()).
//                        build()
//                );
//            }
//        }
//        return groupList;
//    }

    public List<GroupVO> getGroupList() throws WebObjectsException {

        WebObjectSource wos = factory.getObjectSource();

        WebSearch search = wos.getNewSearchObject();

        search.setNamePattern("*" + "" + "*");
        search.setSearchFlags(search.getSearchFlags() + EnumDSSXMLSearchFlags.DssXmlSearchNameWildCard + EnumDSSXMLSearchFlags.DssXmlSearchRootRecursive);
        search.setAsync(false);
        search.types().add(EnumDSSXMLObjectSubTypes.DssXmlSubTypeUserGroup);
        search.setDomain(EnumDSSXMLSearchDomain.DssXmlSearchDomainConfiguration);

        search.submit();
        WebFolder f = search.getResults();

        System.out.println("그룹 총 갯수: " + f.size());

        List<GroupVO> groupList = new ArrayList<>();

        if (f.size() > 0) {
            for (int i = 0; i < f.size(); i++) {
                WebUserGroup group= (WebUserGroup) f.get(i);
                group.populate();
                if(group.isGroup()){
                    groupList.add(GroupVO.builder().
                            groupId(group.getID()).
                            groupNm(group.getName()).
                            childCnt(group.getTotalChildCount()).
                            description(group.getDescription()).
                            creationTime(group.getCreationTime()).
                            owner(group.getOwner().getName()).
                            build()
                    );
                }
            }
        }
        return groupList;
    }

    public List<UserVO> getUserList() throws WebObjectsException {

        WebObjectSource wos = factory.getObjectSource();

        WebSearch search = wos.getNewSearchObject();

        search.setNamePattern("*" + "" + "*");
        search.setSearchFlags(search.getSearchFlags() + EnumDSSXMLSearchFlags.DssXmlSearchNameWildCard + EnumDSSXMLSearchFlags.DssXmlSearchRootRecursive);
        search.setAsync(false);
        search.types().add(EnumDSSXMLObjectSubTypes.DssXmlSubTypeUser);
        search.setDomain(EnumDSSXMLSearchDomain.DssXmlSearchDomainConfiguration);

        search.submit();
        WebFolder f = search.getResults();

        System.out.println("사용자 총 갯수: " + f.size());

        List<UserVO> userList = new ArrayList<>();

        if (f.size() > 0) {
            for (int i = 0; i < f.size(); i++) {
                WebUser user= (WebUser) f.get(i);
                user.populate();

                if(!user.isGroup()){
                    userList.add(UserVO.builder().
                            enableStatus(user.isEnabled()).
                            userId(user.getID()).
                            loginID(user.getLoginName()).
                            userNm(user.getDisplayName()).
                            owner(user.getOwner().getDisplayName()).
                            modification(user.getModificationTime()).
                            description(user.getDescription()).
                            build()
                    );
                }
            }
        }
        return userList;
    }

    public List<RoleVO> getRoleList() throws WebObjectsException {

        WebObjectSource wos = factory.getObjectSource();

        WebSearch search = wos.getNewSearchObject();

        search.setNamePattern("*" + "" + "*");
        search.setSearchFlags(search.getSearchFlags() + EnumDSSXMLSearchFlags.DssXmlSearchNameWildCard + EnumDSSXMLSearchFlags.DssXmlSearchRootRecursive);
        search.setAsync(false);
        search.types().add(EnumDSSXMLObjectSubTypes.DssXmlSubTypeSecurityRole);
        search.setDomain(EnumDSSXMLSearchDomain.DssXmlSearchDomainConfiguration);

        search.submit();
        WebFolder f = search.getResults();

        System.out.println("보안역할 총 갯수: " + f.size());

        List<RoleVO> roleList = new ArrayList<>();

        if (f.size() > 0) {
            for (int i = 0; i < f.size(); i++) {
                WebSecurityRole role= (WebSecurityRole) f.get(i);
                role.populate();
                roleList.add(RoleVO.builder().
                        roleId(role.getID()).
                        roleNm(role.getDisplayName()).
                        owner(role.getOwner().getDisplayName()).
                        modification(role.getModificationTime()).
                        description(role.getDescription()).
                        build()
                );
            }
        }
        return roleList;
    }


    public ResVO addGroup(GroupVO groupInfo){

        if(!isGroupNm(groupInfo.getGroupNm()) || groupInfo.getGroupNm().length() > 64){
            throw new CustomException(ResultCode.INVALID_GROUP_NAME);
        }
        else if(groupInfo.getDescription().length() > 200){
            throw new CustomException(ResultCode.INVALID_REMARK);
        }

        WebObjectSource wos = factory.getObjectSource();
        UserGroupBean group = null;

        try {
            group = (UserGroupBean) BeanFactory.getInstance().newBean("UserGroupBean");
            group.setSessionInfo(serverSession);
            group.InitAsNew();
            group.getUserEntityObject().setFullName(groupInfo.getGroupNm());
            group.getUserEntityObject().setDescription(groupInfo.getDescription());
            group.save();

        } catch (WebBeanException ex) {
            System.out.println(ex.getErrorCode());
            System.out.println(ex.getMessage());
            if(ex.getErrorCode() == -2147217373){
                throw new CustomException(ResultCode.DUPLICATE_GROUP);
            }
            else if(ex.getErrorCode() == -2147213718){
                throw new CustomException(ResultCode.INVALID_GROUP_NAME);
            }

        }
        return new ResVO(ResultCode.SUCCESS);
    }

    public ResVO addUser(UserVO userInfo){
        /** 유효성검사 부분 **/
        if(!isUserNm(userInfo.getLoginID()) ||userInfo.getLoginID().length() > 64){
            throw new CustomException(ResultCode.INVALID_USER_NAME);
        }
        else if(!isUserNm(userInfo.getUserNm()) || userInfo.getUserNm().length() > 64){
            throw new CustomException(ResultCode.INVALID_LOGIN_ID);
        }
        else if(!userInfo.getPassword1().equals(userInfo.getPassword2())){
            throw new CustomException(ResultCode.INVALID_PASSWORD);
        }
        else if(!isPasswordValid(userInfo.getPassword1(),userInfo.getUserNm())){
            throw new CustomException(ResultCode.INVALID_PASSWORD_POLICY);
        }
        else if(userInfo.getDescription().length() > 200){
            throw new CustomException(ResultCode.INVALID_REMARK);
        }

        UserBean user = null;
        try {
            user = (UserBean)BeanFactory.getInstance().newBean("UserBean");
            user.setSessionInfo(serverSession);
            user.InitAsNew();

            WebUser ua = (WebUser) user.getUserEntityObject();
            WebStandardLoginInfo loginInfo = ua.getStandardLoginInfo();

            ua.setLoginName(userInfo.getLoginID());
            ua.setFullName(userInfo.getUserNm());
            user.getObjectInfo().setDescription(userInfo.getDescription());
            loginInfo.setPassword(userInfo.getPassword1());
            user.save();

        } catch (WebBeanException ex) {
            if(ex.getErrorCode() == -2147213795){
                throw new CustomException(ResultCode.EXIST_LOGIN_ID);
            }
            System.out.println(ex.getErrorCode());
            System.out.println(ex.getMessage());
        }
        return new ResVO(ResultCode.SUCCESS);
    }

    public ResVO modifyGroup(GroupVO groupInfo){

        if(!isGroupNm(groupInfo.getGroupNm()) || groupInfo.getGroupNm().length() > 64){
            throw new CustomException(ResultCode.INVALID_GROUP_NAME);
        }
        else if(groupInfo.getDescription().length() > 200){
            throw new CustomException(ResultCode.INVALID_REMARK);
        }

        WebObjectSource wos = factory.getObjectSource();

        try {
            // Getting the User Group Object
            WebUserGroup group = (WebUserGroup) wos.getObject(groupInfo.getGroupId(), EnumDSSXMLObjectTypes.DssXmlTypeUser);

            group.setFullName(groupInfo.getGroupNm());
            group.setDescription(groupInfo.getDescription());

            wos.save(group);

        }catch (WebObjectsException ex){
            throw new CustomException(ResultCode.DUPLICATE_GROUP);
        }catch (IllegalArgumentException iax){
            throw new CustomException(ResultCode.DUPLICATE_GROUP);
        }
        return new ResVO(ResultCode.SUCCESS);
    }


    public ResVO delGroup(String groupId){
        System.out.println(groupId);
        WebObjectSource wos = factory.getObjectSource();

        try {
            // Getting the User Group Object
            WebUserGroup group = (WebUserGroup) wos.getObject(groupId, EnumDSSXMLObjectTypes.DssXmlTypeUser);
            // Deleting the User Group Object
            wos.deleteObject(group);

        }catch (WebObjectsException ex){
            throw new CustomException(ResultCode.INVALID_GROUP_ID);
        }catch (IllegalArgumentException iax){
            throw new CustomException(ResultCode.INVALID_GROUP_ID);
        }
        return new ResVO(ResultCode.SUCCESS);
    }

    //그룹 정보
    public GroupVO getGroupInfo(String groupId) throws WebObjectsException {
        //ObjectSourcec 객체 생성
        WebObjectSource objectSource = factory.getObjectSource();

        //MicroStrategy Groups의 ID를 가지고 User WebObjectInfo로 변경
        WebObjectInfo woi = objectSource.getObject(groupId ,EnumDSSXMLObjectTypes.DssXmlTypeUser);

        //채워넣기
        woi.populate();

        // 사용자 그룹 객체
        WebUserGroup group = (WebUserGroup) woi;
        group.populate();
        GroupVO groupInfo = GroupVO.builder().
                groupId(group.getID()).
                groupNm(group.getDisplayName()).
                childCnt(group.getTotalChildCount()).
                creationTime(group.getCreationTime()).
                description(group.getDescription()).
                owner(group.getOwner().getDisplayName()).
                build();
        return groupInfo;
    }

    public UserVO getUserInfoById(String userId) throws WebObjectsException {
        //ObjectSourcec 객체 생성
        WebObjectSource objectSource = factory.getObjectSource();

        //MicroStrategy Groups의 ID를 가지고 User WebObjectInfo로 변경
        WebObjectInfo woi = objectSource.getObject(userId ,EnumDSSXMLObjectTypes.DssXmlTypeUser);

        //채워넣기
        woi.populate();

        // 사용자 그룹 객체
        WebUser user = (WebUser) woi;
        user.populate();
        UserVO userInfo = UserVO.builder().
                userId(user.getID()).
                loginID(user.getLoginName()).
                userNm(user.getDisplayName()).
                owner(user.getOwner().getDisplayName()).
                modification(user.getModificationTime()).
                description(user.getDescription()).
                enableStatus(user.isEnabled()).
                build();
        WebUserList wul = user.getParents();

        System.out.println(wul.size());

        Enumeration  enumeration = wul.elements();
        WebObjectInfo enumWoi = null;
        // 객체 수 만큼 반복
        while(enumeration.hasMoreElements()) {

            //그룹 채워넣기
            enumWoi = (WebObjectInfo) enumeration.nextElement();

            //WEBUSER로 캐스팅 불가능 한 것은 try catch로 묶던가 하자
            enumWoi.populate();
            System.out.println(enumWoi.getDisplayName());
            System.out.println(enumWoi.getID());
        }

        return userInfo;
    }

    //그룹에 포함된 사용자 정보
    public GroupVO getGroupUserList(GroupVO groupInfo) throws WebObjectsException {

        //ObjectSourcec 객체 생성
        WebObjectSource objectSource = factory.getObjectSource();

        //MicroStrategy Groups의 ID를 가지고 User WebObjectInfo로 변경
        WebObjectInfo woi = objectSource.getObject(groupInfo.getGroupId() ,EnumDSSXMLObjectTypes.DssXmlTypeUser);

        //채워넣기
        woi.populate();

        // 사용자 그룹 객체
        WebUserGroup groups = (WebUserGroup) woi;

        //MicroStrategy Groups하위의 모든 그룹 가져오기위한 UserList 객체 초기화
        WebUserList members = groups.getMembers();

        System.out.println(members.size());

        //하위 요소들을 Enumeration으로 치환
        Enumeration  enumeration = members.elements();

        List<UserVO> users = new ArrayList<>();
        List<GroupVO> childGroups = new ArrayList<>();
        WebObjectInfo enumWoi = null;
        List<String> assignIds = new ArrayList<>();

        // 객체 수 만큼 반복
        while(enumeration.hasMoreElements()){

            //그룹 채워넣기
            enumWoi = (WebObjectInfo) enumeration.nextElement();

            //WEBUSER로 캐스팅 불가능 한 것은 try catch로 묶던가 하자
            enumWoi.populate();
            System.out.println(enumWoi.getSubType());
            if(enumWoi.getSubType() == 8704) {
                WebUser user = (WebUser) enumWoi;
                    assignIds.add(enumWoi.getID());
            }
            /**
             * 그룹할당의 경우를 생각
             * **/
//            else if(enumWoi.getSubType() == 8705){
//                WebUserGroup group = (WebUserGroup) enumWoi;
//                childGroups.add(GroupVO.builder().
//                        groupId(group.getID()).
//                        groupNm(group.getDisplayName()).
//                        childCnt(group.getTotalChildCount()).
//                        creationTime(group.getCreationTime()).
//                        description(group.getDescription()).
//                        owner(group.getOwner().getDisplayName()).
//                        build()
//                );
//            }
        }
        groupInfo.setUsers(users);
        groupInfo.setChildGroups(childGroups);

        WebSearch search = objectSource.getNewSearchObject();

        search.setNamePattern("*" + "" + "*");
        search.setSearchFlags(search.getSearchFlags() + EnumDSSXMLSearchFlags.DssXmlSearchNameWildCard + EnumDSSXMLSearchFlags.DssXmlSearchRootRecursive);
        search.setAsync(false);
        search.types().add(EnumDSSXMLObjectSubTypes.DssXmlSubTypeUser);
        search.setDomain(EnumDSSXMLSearchDomain.DssXmlSearchDomainConfiguration);

        search.submit();
        WebFolder f = search.getResults();

        System.out.println("사용자 총 갯수: " + f.size());
        System.out.println(assignIds);

        if (f.size() > 0) {
            for (int i = 0; i < f.size(); i++) {
                WebUser user= (WebUser) f.get(i);
                user.populate();
                if(!user.isGroup()){
                    String assignYn = "N";

                    // Check if the user ID is in assignIds
                    if (assignIds.contains(user.getID())) {
                        assignYn = "Y";
                    }

                    users.add(UserVO.builder().
                            userId(user.getID()).
                            loginID(user.getLoginName()).
                            userNm(user.getDisplayName()).
                            owner(user.getOwner().getDisplayName()).
                            modification(user.getModificationTime()).
                            description(user.getDescription()).
                            assignYn(assignYn).
                            enableStatus(user.isEnabled()).
                            build()
                    );
                }
            }
        }
        return groupInfo;
    }

    public UserVO getUserGroupList(UserVO userInfo) throws WebObjectsException {

        //ObjectSourcec 객체 생성
        WebObjectSource objectSource = factory.getObjectSource();

        //MicroStrategy Groups의 ID를 가지고 User WebObjectInfo로 변경
        WebObjectInfo woi = objectSource.getObject(userInfo.getUserId() ,EnumDSSXMLObjectTypes.DssXmlTypeUser);

        //채워넣기
        woi.populate();

        // 사용자 그룹 객체
        WebUser user = (WebUser) woi;
        user.populate();
        WebUserList wul = user.getParents();
        Enumeration  enumeration = wul.elements();

        WebObjectInfo enumWoi = null;
        List<String> assignIds = new ArrayList<>();

        // 객체 수 만큼 반복
        while(enumeration.hasMoreElements()) {

            //그룹 채워넣기
            enumWoi = (WebObjectInfo) enumeration.nextElement();

            //WEBUSER로 캐스팅 불가능 한 것은 try catch로 묶던가 하자
            enumWoi.populate();
            assignIds.add(enumWoi.getID());
        }

        WebSearch search = objectSource.getNewSearchObject();

        search.setNamePattern("*" + "" + "*");
        search.setSearchFlags(search.getSearchFlags() + EnumDSSXMLSearchFlags.DssXmlSearchNameWildCard + EnumDSSXMLSearchFlags.DssXmlSearchRootRecursive);
        search.setAsync(false);
        search.types().add(EnumDSSXMLObjectSubTypes.DssXmlSubTypeUserGroup);
        search.setDomain(EnumDSSXMLSearchDomain.DssXmlSearchDomainConfiguration);

        search.submit();
        WebFolder f = search.getResults();
        List<GroupVO> groups = new ArrayList<>();
        System.out.println("그룹 총 갯수: " + f.size());
        System.out.println(assignIds);

        if (f.size() > 0) {
            for (int i = 0; i < f.size(); i++) {
                WebUserGroup group= (WebUserGroup) f.get(i);
                group.populate();
                if(group.isGroup()){
                    String assignYn = "N";

                    // Check if the user ID is in assignIds
                    if (assignIds.contains(group.getID())) {
                        assignYn = "Y";
                    }

                    groups.add(GroupVO.builder().
                            groupId(group.getID()).
                            groupNm(group.getName()).
                            childCnt(group.getTotalChildCount()).
                            description(group.getDescription()).
                            creationTime(group.getCreationTime()).
                            owner(group.getOwner().getName()).
                            assignYn(assignYn).
                            build()
                    );
                }
            }
        }
        userInfo.setParentsGroups(groups);
        return userInfo;
    }




    public ResVO assignGroup(GroupVO groupInfo) {

        //ObjectSourcec 객체 생성
        WebObjectSource objectSource = factory.getObjectSource();

        try {
            WebObjectInfo woi = objectSource.getObject(groupInfo.getGroupId(), EnumDSSXMLObjectTypes.DssXmlTypeUser);

            WebUserGroup group = (WebUserGroup) woi;
            if(group!=null){
                for(int i=0; i<groupInfo.getUsers().size(); i++){
                    WebUser webUser = (WebUser) objectSource.getObject(groupInfo.getUsers().get(i).getUserId(), EnumDSSXMLObjectTypes.DssXmlTypeUser);
                    //Add user to group
                    if(webUser!=null){
                        if("assign".equals(groupInfo.getAssignmentType())){
                            group.getMembers().add(webUser);
                        }
                        else{
                            group.getMembers().remove(webUser);
                        }
                    }
                }
            }
            //Save the group object
            objectSource.save(group);
        } catch (WebObjectsException e) {
            return new ResVO(ResultCode.ERROR_ADD_USER);
        }
        return new ResVO(ResultCode.SUCCESS);
    }

    public ResVO assignUser(UserVO userInfo) {

        //ObjectSourcec 객체 생성
        WebObjectSource objectSource = factory.getObjectSource();

        try {
            WebObjectInfo woi = objectSource.getObject(userInfo.getUserId(), EnumDSSXMLObjectTypes.DssXmlTypeUser);

            WebUser user = (WebUser) woi;
            if(user!=null){
                for(int i=0; i<userInfo.getParentsGroups().size(); i++){
                    WebUserGroup groups = (WebUserGroup) objectSource.getObject(userInfo.getParentsGroups().get(i).getGroupId(), EnumDSSXMLObjectTypes.DssXmlTypeUser);
                    //Add user to group
                    if(groups!=null){
                        if("assign".equals(userInfo.getAssignmentType())){
                            groups.getMembers().add(user);
                        }
                        else{
                            groups.getMembers().remove(user);
                        }
                    }
                    //Save the group object
                    objectSource.save(groups);
                }
            }

        } catch (WebObjectsException e) {
            return new ResVO(ResultCode.ERROR_ADD_USER);
        }
        return new ResVO(ResultCode.SUCCESS);
    }

    public ResVO enableAccount(UserVO userInfo) {

        //ObjectSourcec 객체 생성
        WebObjectSource objectSource = factory.getObjectSource();
        System.out.println(userInfo.isEnableStatus());
        try {
            WebObjectInfo woi = objectSource.getObject(userInfo.getUserId(), EnumDSSXMLObjectTypes.DssXmlTypeUser);

            WebUser user = (WebUser) woi;
            user.populate();
            user.setEnabled(userInfo.isEnableStatus());
            objectSource.save(user);

        } catch (WebObjectsException e) {
            return new ResVO(ResultCode.INVALID_USER_ID);
        } catch(IllegalArgumentException iax){
            return new ResVO(ResultCode.INVALID_USER_ID);
        }
        return new ResVO(ResultCode.SUCCESS);
    }

    public ResVO resetPassword(String userId){
        //ObjectSourcec 객체 생성
        WebObjectSource objectSource = factory.getObjectSource();

        try {
            WebObjectInfo woi = objectSource.getObject(userId, EnumDSSXMLObjectTypes.DssXmlTypeUser);

            WebUser user = (WebUser) woi;
            WebStandardLoginInfo loginInfo = user.getStandardLoginInfo();
            loginInfo.setPassword("12345678");
            objectSource.save(user);
        } catch (WebObjectsException ex) {
            if(ex.getErrorCode() == -2147180957){
                throw new CustomException(ResultCode.INVALID_PASSWORD_POLICY);
            }
        }catch (IllegalArgumentException iax){
            throw new CustomException(ResultCode.INVALID_USER_ID);
        }
        return new ResVO(ResultCode.SUCCESS);
    }


    public static Object performSearch(WebSearch search){
        try {
            search.submit();
            WebFolder folder = search.getResults();
            if(folder.size()>0){
                if(folder.size()==1){
                    return folder.get(0);
                } else {
                    System.out.println("Search returns more than 1 object, returning first object");
                    return folder.get(0);
                }
            }
        } catch (WebObjectsException ex) {
            System.out.println("Error performing search: "+ex.getMessage());
        }
        return null;
    }

    public void getLicenseInfo() throws WebObjectsException {
        //ObjectSourcec 객체 생성
        WebObjectSource objectSource=factory.getObjectSource();

        LicenseSource ls = factory.getLicenseSource();

        WebUserEntity everyone = (WebUserEntity)objectSource.getObject("C82C6B1011D2894CC0009D9F29718E4F",34,true);
        UserLicenseAudit ula = ls.auditUsers(everyone);
        LicensedUsers lus = ula.getUnlicensedUsers();
        String message = "Unlicensed Users:\n" ;
        for (int i=0;i<lus.size();i++)
            message += lus.get(i).getDisplayName() + "\n";

//Get user named licenses
        NamedUserLicense[] sl = ls.getNamedUserCompliance();
        message += "\n\nLicensed Users:\n";

//loop over the different license types
        for(int i=1;i<sl.length;i++){
//Get license name
            message += "License Name = " + sl[i].getName() + "\t\t";

/*Get number of licensed users
LicensedUsers collection would allow to display also
the individual names of the licensed users*/

            message += "Licensed Users: " + ula.getLicensedUsers(sl[i].getLicenseType()).size() + "\t";

//Get license max usage
            message += " Max = " + sl[i].getMaximumUsage() + "\n";
        }

        System.out.println( message);
    }



}
