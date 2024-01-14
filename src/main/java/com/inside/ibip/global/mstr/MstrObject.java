package com.inside.ibip.global.mstr;

import com.inside.ibip.domain.admin.license.vo.LicenseVO;
import com.inside.ibip.domain.admin.role.vo.*;
import com.inside.ibip.domain.admin.user.vo.UserVO;
import com.inside.ibip.domain.guest.favorite.vo.FavoriteVO;
import com.inside.ibip.domain.guest.folder.vo.TreeVO;
import com.inside.ibip.domain.admin.group.vo.GroupVO;
import com.inside.ibip.domain.guest.folder.vo.FolderVO;
import com.inside.ibip.domain.guest.main.vo.SearchResultVO;
import com.inside.ibip.domain.guest.main.vo.SearchVO;
import com.inside.ibip.domain.guest.main.vo.UserInfoVO;
import com.inside.ibip.domain.guest.prompt.vo.PromptVO;
import com.inside.ibip.domain.guest.report.vo.ReportVO;
import com.inside.ibip.global.exception.CustomException;
import com.inside.ibip.global.exception.code.ResultCode;
import com.inside.ibip.global.mstr.prompt.ConstantPrompt;
import com.inside.ibip.global.mstr.prompt.ElementPrompt;
import com.inside.ibip.global.mstr.prompt.ObjectPrompt;
import com.inside.ibip.global.vo.ResVO;
import com.microstrategy.web.beans.*;
import com.microstrategy.web.objects.*;
import com.microstrategy.web.objects.admin.licensing.*;
import com.microstrategy.web.objects.admin.users.*;
import com.microstrategy.webapi.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import static com.inside.ibip.global.utils.StringUtils.*;

@Component
@Log4j2
public class MstrObject extends MstrSession{

    /**
     * MSTR Object Source
     */
    private WebObjectSource objectSource = factory.getObjectSource();
    private WebDocumentSource documentSource = factory.getDocumentSource();

    private WebReportSource reportSource = factory.getReportSource();

    /**
     * Mstr 세션 적용 함수
     * @Method Name   : setSession
     * @Date / Author : 2023.12.01  이도현
     * @param mstrSessionId MstrSession에서 받은 세션 정보 Id
     * @History
     * 2023.12.01	최초생성
     */
    public void setSession(String mstrSessionId){
        serverSession = factory.getIServerSession();
        serverSession.setSessionID(mstrSessionId);
    }

    //하위 폴더 요소 가져오기
    public List<FolderVO> getSubfolderList(String folderId){
        List<FolderVO> subFolderList = new ArrayList<>();
        try{
            WebFolder folder = (WebFolder) factory.getObjectSource().getObject(folderId, EnumDSSXMLObjectTypes.DssXmlTypeFolder);
            folder.populate();

            for(int i=0; i<folder.getChildCount(); i++){
                if(folder.get(i).getType() == EnumDSSXMLObjectTypes.DssXmlTypeFolder) {
                    subFolderList.add(new FolderVO(folder.get(i).getName(), folder.get(i).getID(), folder.get(i).getType()));
                    System.out.println(folder.get(i));
                }
            }
        }catch (WebObjectsException woe){
            log.error("공유 리포트 하위 목록 조회 중 에러 발생 [Error msg]: " + woe.getMessage());
            throw new CustomException(ResultCode.MSTR_ETC_ERROR);
        }catch (IllegalArgumentException iae){
            log.error("공유 리포트 하위 목록 조회 중 에러 발생 (폴더 Id로 객체 조회 실패) [Error msg]: "+ iae.getMessage());
            throw new CustomException(ResultCode.MSTR_ETC_ERROR);
        }
        return subFolderList;
    }

    //하위 폴더 요소 가져오기
    public List<TreeVO> getShareReport(String folderId) {
        List<TreeVO> shareReportList = new ArrayList<>();
        try {
            WebFolder folder = (WebFolder) factory.getObjectSource().getObject(folderId, EnumDSSXMLObjectTypes.DssXmlTypeFolder);
            folder.populate();
            for (int i = 0; i < folder.getChildCount(); i++) {
                if (folder.get(i).getType() == EnumDSSXMLObjectTypes.DssXmlTypeFolder) {
                    shareReportList.add(new TreeVO(folder.get(i).getID(), folder.get(i).getName(), "#", folder.get(i).getType(), true));
                }
            }
        }catch (WebObjectsException ex){
            throw new CustomException(ResultCode.MSTR_ETC_ERROR);
        }catch (IllegalArgumentException iax){
            throw new CustomException(ResultCode.INVALID_FOLDER);
        }
        return shareReportList;
    }

    //폴더 ID 불러오기
    public String getFolderId(int folderNum) {
        String folderId = null;
        try {
            folderId = objectSource.getFolderID(folderNum);
        } catch (WebObjectsException woe){
            log.error("폴더 ID 조회 중 에러 발생 [Error msg]: " + woe.getMessage());
            throw new CustomException(ResultCode.MSTR_ETC_ERROR);
        }
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
    public FolderVO getFolderInfo(String folderId){
        FolderVO folderInfo = new FolderVO();
        try {
            WebFolder folder = (WebFolder) factory.getObjectSource().getObject(folderId, EnumDSSXMLObjectTypes.DssXmlTypeFolder);

            folderInfo.setName(folder.getName());
            folderInfo.setId(folder.getID());
            folderInfo.setTp(folder.getType());

        }catch (WebObjectsException woe){
            log.error("폴더 정보 조회 중 에러 발생 [Error msg]: " + woe.getMessage());
            throw new CustomException(ResultCode.MSTR_ETC_ERROR);
        }catch (IllegalArgumentException iae){
            log.error("폴더 정보 조회 중 에러 발생 [Error msg]: " + iae.getMessage());
            throw new CustomException(ResultCode.INVALID_FOLDER);
        }
        return folderInfo;
    }

    /**
     * 로그인 한 MSTR 세션의 사용자 정보를 가져온다.
     * @Method Name   : getUserInfo
     * @Date / Author : 2023.12.01  이도현
     * @return 사용자 정보
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     * 권한을 추가로 조회하여 관리자인지 사용자인지 체크
     */
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
            throw new CustomException(ResultCode.MSTR_ETC_ERROR);
        }
        return userInfo;
    }

    public String getUserLoginName(String userId){
        WebUser webUser = null;
        try {
            webUser = (WebUser) factory.getObjectSource().getObject(userId, EnumDSSXMLObjectTypes.DssXmlTypeUser);
            webUser.populate();
        } catch (WebObjectsException e) {
            throw new CustomException(ResultCode.INVALID_USER_ID);
        } catch(IllegalArgumentException iax) {
            throw new CustomException(ResultCode.INVALID_USER_ID);
        }
        return webUser.getLoginName();
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


    /**
     * 특정 폴더 목록 조회
     * @Method Name   : getShareReport
     * @Date / Author : 2023.12.01  이도현
     * @param folderId 하위 목록 조회 할 폴더 ID
     * @param parentId 부모 ID (재귀를 위해 부모 폴더 지정)
     * @param subList 하위 목록 리스트
     * @return 폴더 하위 목록 리스트
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    public List<TreeVO> getSubList(String folderId, String parentId, List<TreeVO> subList){

        try {
            WebFolder folder = (WebFolder) factory.getObjectSource().getObject(folderId, EnumDSSXMLObjectTypes.DssXmlTypeFolder);
            folder.populate();

            for (int i = 0; i < folder.getChildCount(); i++) {
                if (folder.get(i).getType() == 8) {
                    folder.get(i).populate();
                    if (folder.get(i).getChildUnits().size() > 0) {
                        getSubList(folder.get(i).getID(), folder.get(i).getParent().getID(), subList);
                    }
                }
                subList.add(new TreeVO(folder.get(i).getID(), folder.get(i).getName(), folder.get(i).getParent().getID(), folder.get(i).getType(), false));
            }
        } catch (WebObjectsException e) {
            throw new CustomException(ResultCode.MSTR_ETC_ERROR);
        } catch(IllegalArgumentException iax) {
            throw new CustomException(ResultCode.INVALID_FOLDER);
        }
        return subList;
    }

    /**
     * 즐겨찾기 목록 조회 (트리구조 파싱)
     * @Method Name   : getFavorite
     * @Date / Author : 2023.12.01  이도현
     * @param favorites 즐겨찾기에 등록 된 문서 모음
     * @param folderId 부모로 칭할 폴더 Id
     * @return 즐겨찾기 목록 리스트
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    public List<TreeVO> getFavorite(List<FavoriteVO> favorites, String folderId){
        List<TreeVO> favoriteList = new ArrayList<>();
        try{
            for(int i=0; i<favorites.size(); i++){
                WebObjectInfo woi = objectSource.getObject(favorites.get(i).getReportId(), favorites.get(i).getDocType());
                woi.populate();

                favoriteList.add(TreeVO.builder().id(woi.getID()).parent(folderId).children(false).text(woi.getName()).type(woi.getType()).build());
            }
        }catch (WebObjectsException woe){
            log.error("즐겨찾기 데이터 불러오는 중 에러 발생 [Error msg]: " + woe.getMessage());
            throw new CustomException(ResultCode.INVALID_FAVORITE_LIST);
        }
        return favoriteList;
    }

    public String getUsrSmgr(){
        String usrSmgr = serverSession.saveState(0);
        return usrSmgr;
    }

    /**
     * 문서 검색 (리포트, 다큐먼트)
     * @Method Name   : search
     * @Date / Author : 2023.12.01  이도현
     * @param searchKeyword 검색 키워드
     * @return 검색 리스트
     * @History
     * 2023.12.01	최초생성
     */
    public SearchVO search(String searchKeyword){

        //1. 검색 객체,  검색 결과 리스트 객체 생성
        SearchVO search = new SearchVO();
        List<SearchResultVO> searchList = new ArrayList<>();

        try {

            //2. WebSearch 객체 생성
            WebSearch webSearch = objectSource.getNewSearchObject();

            //2-1. 검색 키워드, 검색 종류, 검색 패턴, 지정
            webSearch.setNamePattern("*" + searchKeyword + "*");
            webSearch.setSearchFlags(webSearch.getSearchFlags() + EnumDSSXMLSearchFlags.DssXmlSearchNameWildCard + EnumDSSXMLSearchFlags.DssXmlSearchRootRecursive);
            webSearch.setAsync(false);

            //2-2. 조회 및 파싱 (검색 결과는 WebFolder로 파싱)
            webSearch.submit();
            WebFolder objectResult = webSearch.getResults();

            //2-3. 생성일자를 위한 Date Format 지정
            SimpleDateFormat normalParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat originalParser = new SimpleDateFormat("yy-MM-dd a hh:mm:ss");
            Date creationTime = null;

            //3. 결과의 하위 목록 (결과 리스트 파싱)
            for (int i = 0; i < objectResult.size(); i++) {

                //3-1. 결과 목록 파싱 후 채워넣기
                WebObjectInfo woi = objectResult.get(i);
                woi.populate();

                //3-2. 타입이 리포트 혹은 다큐먼트만 선별
                if (woi.getType() == 3 || woi.getType() == 55) {

                    //3-3. 결과 목록의 경로 추출
                    SimpleList lst = woi.getAncestors();

                    String path = "";
                    String reportName = woi.getName();
                    for (int j = 0; j < lst.size(); j++) {
                        WebObjectInfo obj = (WebObjectInfo) lst.item(j);
                        if ("".equals(path)) {
                            path += obj.getName();
                        } else {
                            path += " > " + obj.getName();
                        }
                    }
                    path += " > " + reportName;

                    //3-4. 생성일자 포맷 변경 (yyyy-MM-dd HH:mm:ss)
                    try {
                        creationTime = originalParser.parse(woi.getCreationTime());
                    } catch (Exception e) {
                        log.error("생성일자 포맷 변경 중 에러 발생 [Error msg]: " + e.getMessage());
                        throw new CustomException(ResultCode.INVALID_CREATION_TIME);
                    }

                    //3-5. 결과 생성 후 삽입
                    SearchResultVO searchResult = new SearchResultVO(
                            woi.getID(),
                            woi.getName(),
                            woi.getType(),
                            woi.getOwner().getName(),
                            normalParser.format(creationTime),
                            path
                    );
                    searchList.add(searchResult);
                }
            }
        }catch (WebObjectsException woe){
            log.error("문서 검색 중 에러 발생 [Error msg]: " + woe.getMessage());
            throw new CustomException(ResultCode.MSTR_ETC_ERROR);
        }

        //4. 반환
        search.setSearchList(searchList);
        return search;

    }


    /**
     * 리포트 정보 조회
     * @Method Name   : getReportInfo
     * @Date / Author : 2023.12.01  이도현
     * @param reportId 리포트의 ID
     * @param documentType 문서 타입
     * @return 리포트 객체
     * @History
     * 2023.12.01	최초생성
     */
    public ReportVO getReportInfo(String reportId, String documentType){

        //1. 리포트 객체 생성 및 Id, type 지정
        ReportVO reportInfo = new ReportVO();
        reportInfo.setReportId(reportId);
        reportInfo.setDocumentType(documentType);

        //1-1. 문서 정보 조회를 위한 문서 타입 설정
        int dssObjectTypes;

        if("D".equals(documentType)){ //다큐먼트, 도씨에
            dssObjectTypes = EnumDSSXMLObjectTypes.DssXmlTypeDocumentDefinition;
        }
        else{ //리포트
            dssObjectTypes = EnumDSSXMLObjectTypes.DssXmlTypeReportDefinition;
        }

        //2. 리포트 정보 조회
        try {
            WebObjectInfo report = objectSource.getObject(reportId, dssObjectTypes);
            report.populate();

            String reportName = report.getName();
            reportInfo.setReportNm(reportName);

            // 리포트 경로를 위한 list 객체 생성 후 파싱
            SimpleList lst = report.getAncestors();
            String reportPath = "";
            for (int i = 0; i < lst.size() - 3; i++) {
                WebObjectInfo obj = (WebObjectInfo) lst.item(i + 3);
                if ("".equals(reportPath)) {
                    reportPath += obj.getName();
                } else {
                    reportPath += " > " + obj.getName();
                }
            }
            reportPath += " > " + reportName;
            reportInfo.setReportPath(reportPath);

        }catch (WebObjectsException woe){
            log.error("리포트 정보 조회 중 에러 발생 [Error msg]: " + woe.getMessage());
            throw new CustomException(ResultCode.MSTR_ETC_ERROR);
        }catch (IllegalArgumentException iae){
            log.error("리포트 정보 조회 중  에러 발생 (리포트 Id로 객체 조회 실패) [Error msg]: "+ iae.getMessage());
            throw new CustomException(ResultCode.INVALID_DOCUMENT);
        }

        return reportInfo;
    }



    /**
     * 리포트 정보 조회
     * @Method Name   : getReportInfo
     * @Date / Author : 2023.12.01  이도현
     * @param reportId 리포트의 ID
     * @param reportInfo 리포트 정보를 담은 객체
     * @return 리포트 객체
     * @History
     * 2023.12.01	최초생성
     */
    public ReportVO getReportDataInfo(String reportId, ReportVO reportInfo){

        //1. WebInstance 객체 생성
        WebResultSetInstance resultInstance = null;

        //1-1. 문서타입에 따라 상속 된 Instance로 초기화
        try {
            if ("D".equals(reportInfo.getDocumentType())) {
                resultInstance = (WebDocumentInstance) documentSource.getNewInstance(reportId);
            } else {
                resultInstance = (WebReportInstance) reportSource.getNewInstance(reportId);
            }
        }catch(WebObjectsException woe){
            log.error("리포트 데이터, 프롬프트 조회 중  에러 발생 (문서 타입으로 source 객체 생성 중 오류 발생) [Error msg]: "+ woe.getMessage());
            throw new CustomException(ResultCode.MSTR_ETC_ERROR);
        }

        //Prompts 객체 (Prompt List) 선언
        WebPrompts webPrompts = null;
        try {
            //1-2. 해당 문서에 프롬프트가 존재하는지 확인 존재하지 않을경우 return
            if (resultInstance.pollStatus() != EnumDSSXMLStatus.DssXmlStatusPromptXML) {
                reportInfo.setPromptExist("N");
                return reportInfo;
            }

            //1-2. 프롬프트 존재 시 변수 설정
            reportInfo.setPromptExist("Y");

            //1-3. WebPrompts 객체 조회
            webPrompts = resultInstance.getPrompts();

        }catch (WebObjectsException woe){
            log.error("프롬프트 불러오는 도중 오류 발생 [Error msg]: "+ woe.getMessage());
            throw new CustomException(ResultCode.INVALID_PROMPT);
        }

        //2. 프롬프트 List 객체 생성
        List<PromptVO> promptList = new ArrayList<>();

        //2-1. 프롬프트 List 수 만큼 반복
        for(int i=0; i<webPrompts.size(); i++) {

            //2-2. 프롬프트 객체 생성
            WebPrompt webPrompt = webPrompts.get(i);
            PromptVO prompt = new PromptVO();

            /**
             *
             * prompt 타입에 따라 분기 처리
             * TYPE = 1 날짜 프롬프트
             * TYPE = 2 구성요소 프롬프트
             * TYPE = 3 계층 프롬프트
             * TYPE = 4 개체 프롬프트
             *
             * **/
            if (webPrompt.getPromptType() == EnumWebPromptType.WebPromptTypeConstant) { //값 프롬프트
                ConstantPrompt constantPrompt = new ConstantPrompt();
                prompt = constantPrompt.getConstantPromptInfo(prompt, webPrompt);

            } else if (webPrompt.getPromptType() == EnumWebPromptType.WebPromptTypeElements) { // 구성요소 프롬프트
                ElementPrompt elementPrompt = new ElementPrompt();
                prompt = elementPrompt.getElementPromptInfo(prompt, webPrompt);

            } else if (webPrompt.getPromptType() == EnumWebPromptType.WebPromptTypeExpression) { // 계층 프롬프트
                /** 계층 프롬프트 보류 중 **/

            } else if (webPrompt.getPromptType() == EnumWebPromptType.WebPromptTypeObjects) { // 개체 프롬프트
                ObjectPrompt objectPrompt = new ObjectPrompt();
                prompt = objectPrompt.getObjectPromptInfo(prompt, webPrompt);
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

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (f.size() > 0) {
            for (int i = 0; i < f.size(); i++) {
                WebUserGroup group= (WebUserGroup) f.get(i);
                group.populate();
                SimpleDateFormat originalFormat = new SimpleDateFormat("yy-MM-dd a hh:mm:ss");
                Date creationTime = null;
                try {
                    creationTime = originalFormat.parse(group.getCreationTime());
                }catch (Exception e){}
                if(group.isGroup()){
                    groupList.add(GroupVO.builder().
                            groupId(group.getID()).
                            groupNm(group.getName()).
                            childCnt(group.getTotalChildCount()).
                            description(group.getDescription()).
                            creationTime(sdf.format(creationTime)).
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

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (f.size() > 0) {
            for (int i = 0; i < f.size(); i++) {
                WebUser user= (WebUser) f.get(i);
                user.populate();
                SimpleDateFormat originalFormat = new SimpleDateFormat("yy-MM-dd a hh:mm:ss");
                Date modification = null;
                try {
                    modification = originalFormat.parse(user.getModificationTime());
                }catch (Exception e){}
                if(!user.isGroup()){
                    userList.add(UserVO.builder().
                            enableStatus(user.isEnabled()).
                            userId(user.getID()).
                            loginID(user.getLoginName()).
                            userNm(user.getDisplayName()).
                            owner(user.getOwner().getDisplayName()).
                            modification(sdf.format(modification)).
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
//        WebPrivilegeCategories webPrivilegeCategories = factory.getObjectSource().getObject();
        System.out.println("보안역할 총 갯수: " + f.size());

        List<RoleVO> roleList = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (f.size() > 0) {
            for (int i = 0; i < f.size(); i++) {
                WebSecurityRole role= (WebSecurityRole) f.get(i);
                role.populate();
                System.out.println(role.getType());
                System.out.println(role.getSubType());
                SimpleDateFormat originalFormat = new SimpleDateFormat("yy-MM-dd a hh:mm:ss");
                Date modification = null;
                try {
                    modification = originalFormat.parse(role.getModificationTime());
                }catch (Exception e){}

                WebEditablePrivileges privileges = role.getPrivileges();
                System.out.println("해당 보안 역할 : "+ role.getName());
                for(int j=0; j<privileges.size(); j++){
                    System.out.println(privileges.get(j).getName());
                    System.out.println(privileges.get(j).getType());
//                    System.out.println(privileges.get(j).);
                    System.out.println(privileges.get(j).getDescription());
                }
                roleList.add(RoleVO.builder().
                        roleId(role.getID()).
                        roleNm(role.getDisplayName()).
                        owner(role.getOwner().getDisplayName()).
                        modification(sdf.format(modification)).
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

    public ResVO addRole(RoleVO roleInfo){
        /** 유효성검사 부분 **/
        if(!isRoleNm(roleInfo.getRoleNm()) ||roleInfo.getRoleNm().length() > 64){
            throw new CustomException(ResultCode.INVALID_LOGIN_ID);
        }
        else if(roleInfo.getDescription().length() > 200){
            throw new CustomException(ResultCode.INVALID_REMARK);
        }
        SecurityRoleBean role = null;

        try{
            role = (SecurityRoleBean) BeanFactory.getInstance().newBean("SecurityRoleBean");
            role.setSessionInfo(serverSession);
            role.InitAsNew();
            role.getSecurityRole().setName(roleInfo.getRoleNm());
            role.getSecurityRole().setDescription(roleInfo.getDescription());
            role.save();

        }catch (WebBeanException wbe){
            if(wbe.getErrorCode() == -2147217373){
                throw new CustomException(ResultCode.DUPLICATE_ROLE);
            }
            else if(wbe.getErrorCode() == -2147213718){
                throw new CustomException(ResultCode.INVALID_ROLE_NAME);
            }
        }

        return new ResVO(ResultCode.SUCCESS);
    }


    public ResVO addUser(UserVO userInfo){
        /** 유효성검사 부분 **/
        if(!isUserNm(userInfo.getLoginID()) ||userInfo.getLoginID().length() > 64){
            throw new CustomException(ResultCode.INVALID_LOGIN_ID);
        }
        else if(!isUserNm(userInfo.getUserNm()) || userInfo.getUserNm().length() > 64){
            throw new CustomException(ResultCode.INVALID_USER_NAME);
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

    public ResVO modifyUser(UserVO userInfo){

        if(!isUserNm(userInfo.getLoginID()) ||userInfo.getLoginID().length() > 64){
            throw new CustomException(ResultCode.INVALID_LOGIN_ID);
        }
        else if(!isUserNm(userInfo.getUserNm()) || userInfo.getUserNm().length() > 64){
            throw new CustomException(ResultCode.INVALID_USER_NAME);
        }
        else if(userInfo.getDescription().length() > 200){
            throw new CustomException(ResultCode.INVALID_REMARK);
        }

        WebObjectSource wos = factory.getObjectSource();

        try {
            // Getting the User Group Object
            WebUser user = (WebUser) wos.getObject(userInfo.getUserId(), EnumDSSXMLObjectTypes.DssXmlTypeUser);

            user.setFullName(userInfo.getUserNm());
            user.setDescription(userInfo.getDescription());
            user.setLoginName(userInfo.getLoginID());

            wos.save(user);

        }catch (WebObjectsException ex){
            throw new CustomException(ResultCode.EXIST_LOGIN_ID);
        }catch (IllegalArgumentException iax){
            throw new CustomException(ResultCode.EXIST_LOGIN_ID);
        }
        return new ResVO(ResultCode.SUCCESS);
    }

    public ResVO modifyRole(RoleVO roleInfo){

        if(!isRoleNm(roleInfo.getRoleNm()) || roleInfo.getRoleNm().length() > 64){
            throw new CustomException(ResultCode.INVALID_GROUP_NAME);
        }
        else if(roleInfo.getDescription().length() > 200){
            throw new CustomException(ResultCode.INVALID_REMARK);
        }

        WebObjectSource wos = factory.getObjectSource();

        try {
            // Getting the User Group Object
            WebSecurityRole role = (WebSecurityRole) wos.getObject(roleInfo.getRoleId(), EnumDSSXMLObjectTypes.DssXmlTypeSecurityRole);
            role.populate();
            role.setName(roleInfo.getRoleNm());
            role.setDisplayName(roleInfo.getRoleNm());
            role.setDescription(roleInfo.getDescription());

            wos.save(role);

        }catch (WebObjectsException ex){
            throw new CustomException(ResultCode.DUPLICATE_ROLE);
        }catch (IllegalArgumentException iax){
            throw new CustomException(ResultCode.DUPLICATE_ROLE);
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

    public ResVO delUser(String userId){
        System.out.println(userId);
        WebObjectSource wos = factory.getObjectSource();

        try {
            // Getting the User Group Object
            WebUser user = (WebUser) wos.getObject(userId, EnumDSSXMLObjectTypes.DssXmlTypeUser);
            // Deleting the User Group Object
            wos.deleteObject(user);

        }catch (WebObjectsException ex){
            throw new CustomException(ResultCode.INVALID_USER_ID);
        }catch (IllegalArgumentException iax){
            throw new CustomException(ResultCode.INVALID_USER_ID);
        }
        return new ResVO(ResultCode.SUCCESS);
    }

    public ResVO delRole(String roleId){
        System.out.println(roleId);
        WebObjectSource wos = factory.getObjectSource();

        try {
            // Getting the User Group Object
            WebSecurityRole role = (WebSecurityRole) wos.getObject(roleId, EnumDSSXMLObjectTypes.DssXmlTypeSecurityRole);
            // Deleting the User Group Object
            wos.deleteObject(role);

        }catch (WebObjectsException ex){
            throw new CustomException(ResultCode.INVALID_ROLE_ID);
        }catch (IllegalArgumentException iax){
            throw new CustomException(ResultCode.INVALID_ROLE_ID);
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
        objectSource.setFlags(objectSource.getFlags()|EnumDSSXMLObjectFlags.DssXmlObjectComments);
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
        System.out.println(user.getAbbreviation());
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

    public RoleVO getRoleInfo(String roleId) throws WebObjectsException {

        //ObjectSourcec 객체 생성
        WebObjectSource objectSource = factory.getObjectSource();

        //MicroStrategy Groups의 ID를 가지고 User WebObjectInfo로 변경
        WebObjectInfo woi = objectSource.getObject(roleId ,EnumDSSXMLObjectTypes.DssXmlTypeSecurityRole);

        //채워넣기
        woi.populate();

        // 사용자 그룹 객체
        WebSecurityRole role = (WebSecurityRole) woi;
        role.populate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat originalFormat = new SimpleDateFormat("yy-MM-dd a hh:mm:ss");

        Date modification = null;
        try {
            modification = originalFormat.parse(role.getModificationTime());
        }catch (Exception e){}
        role.populate();

        RoleVO roleInfo = RoleVO.builder().
                roleId(role.getID()).
                roleNm(role.getDisplayName()).
                owner(role.getOwner().getDisplayName()).
                modification(sdf.format(modification)).
                description(role.getDescription()).
                build();

        List<CategoryVO> categoryList = new ArrayList<>();

        List<Integer> assignIds = new ArrayList<>();
        String assignYn = "N";
        StateVO state = null;
        for(int i=0; i<role.getPrivileges().size(); i++){
            assignIds.add(role.getPrivileges().get(i).getType());
        }
        System.out.println(assignIds);

        // 배당 보안역할의 권한을 뽑아내기 위해 권한목록 객체 생성
        WebPrivilegeCategories categories = objectSource.getUserServicesSource().getPrivilegeCategories(role);
        for(int i=0; i< categories.size(); i++){
            CategoryVO category = CategoryVO.builder().
                    categoryNm(categories.get(i).getName()).
                    categoryType(categories.get(i).getType()).
                    type("cat").
                    text(categories.get(i).getName()).
                    build();

            List<PrivilegeVO> privilegeList = new ArrayList<>();

            for(int j=0; j < categories.get(i).size(); j++){
                System.out.println(categories.get(i).get(j).getType());
                if(assignIds.contains(categories.get(i).get(j).getType())){
                    assignYn = "Y";
                    state = new StateVO(true);
                } else{
                    assignYn = "N";
                    state = new StateVO(false);
                }
                    PrivilegeVO privilege = PrivilegeVO.builder().
                        privilegeNm(categories.get(i).get(j).getName()).
                        privilegeType(categories.get(i).get(j).getType()).
                        description(categories.get(i).get(j).getDescription()).
                        text(categories.get(i).get(j).getName()).
                        categoryType(categories.get(i).getType()).
                        assignYn(assignYn).
                        type("pri").
                        state(state).
                        build();
                privilegeList.add(privilege);
            }
            category.setChildren(privilegeList);
            categoryList.add(category);
        }
        roleInfo.setCategories(categoryList);

        return roleInfo;
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
        userInfo.setParentsGroups(groups);

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
                    GroupVO groupEn = GroupVO.builder().
                            groupId(group.getID()).
                            groupNm(group.getName()).
                            childCnt(group.getTotalChildCount()).
                            description(group.getDescription()).
                            creationTime(group.getCreationTime()).
                            owner(group.getOwner().getName()).
                            assignYn(assignYn).
                            build();
                    groups.add(groupEn);
                }
            }
        }
        return userInfo;
    }

    public void getUserSecurityRole(UserVO userInfo) throws WebObjectsException {

        //ObjectSourcec 객체 생성
        WebObjectSource objectSource = factory.getObjectSource();

        //MicroStrategy Groups의 ID를 가지고 User WebObjectInfo로 변경
        WebObjectInfo woi = objectSource.getObject(userInfo.getUserId(), EnumDSSXMLObjectTypes.DssXmlTypeUser);



        //채워넣기
        woi.populate();

        // 사용자 그룹 객체
        WebUser user = (WebUser) woi;
        user.populate();
        System.out.println(user.getProjectId());
        WebProject wp = (WebProject) objectSource.getObject("B19DEDCC11D4E0EFC000EB9495D0F44F", EnumDSSXMLObjectTypes.DssXmlTypeProject);

        WebUserSecurityRoles securityRoles = user.getSecurityRoles();
        WebSecurityRole[] roles = securityRoles.getRoles(wp);

        for(int i=0; i< roles.length; i++){
            System.out.println(roles[i].getName());
            System.out.println(roles[i].getID());
            System.out.println(roles[i].getDisplayName());
            System.out.println(roles[i].getDescription());
        }


        System.out.println(securityRoles.size());

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
            String[] name=new String[1];
            name[0]="kim";
            user.setComments(name);
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

            message += "Licensed Users: " + ula.getLicensedUsers(sl[i].getLicenseType()).size() + "\n";
//                                            ula.getUn
//            message += "enabled user list: ";
//            for(int j=0; j<lus2.size(); j++){
//                message += lus2.get(j).getDisplayName() +"\n";
//            }

            message += "enabled: " + + sl[i].getCurrentUsage() + "\t";
//Get license max usage
            message += " Max = " + sl[i].getMaximumUsage() + "\n";
        }

        System.out.println( message);
    }

    public List<LicenseVO> getLicenseList() throws WebObjectsException {
        //라이센스 이름 (타입), Max 수, 총 유저 수, 활성화 된 수, 비활성화 된 수
        WebObjectSource objectSource = factory.getObjectSource();
        LicenseSource licenseSource = factory.getLicenseSource();

        NamedUserLicense[] namedUserLicenses = licenseSource.getNamedUserCompliance();
        System.out.println(namedUserLicenses.length);

        //Everyone 그룹으로 모든 사용자 검색
        WebUserEntity everyone = (WebUserEntity)objectSource.getObject("C82C6B1011D2894CC0009D9F29718E4F",34,true);
        UserLicenseAudit audit = licenseSource.auditUsers(everyone);

        List<LicenseVO> licenseList = new ArrayList<>();
        for(int i=0; i< namedUserLicenses.length; i++){
            int totalUsage = audit.getLicensedUsers(namedUserLicenses[i].getLicenseType(), EnumDSSXMLAuditUserFilter.DssXmlAuditIgnoreEnabledFlag).size();
            int enableUsage =  audit.getLicensedUsers(namedUserLicenses[i].getLicenseType(), EnumDSSXMLAuditUserFilter.DssXmlAuditEnabledUsers).size();
            int disableUsage =  audit.getLicensedUsers(namedUserLicenses[i].getLicenseType(), EnumDSSXMLAuditUserFilter.DssXmlAuditDisabledUsers).size();

            LicenseVO license = LicenseVO.builder().
                    licenseType(namedUserLicenses[i].getLicenseType()).
                    licenseNm(namedUserLicenses[i].getName()).
                    maxUsage(namedUserLicenses[i].getMaximumUsage()).
                    totalUsage(totalUsage).
                    enableUsage(enableUsage).
                    disableUsage(disableUsage).
                    build();

            licenseList.add(license);
        }
        return licenseList;
    }

    public LicenseVO getLicenseInfo(int licenseType) throws WebObjectsException {
        //라이센스 이름 (타입), Max 수, 총 유저 수, 활성화 된 수, 비활성화 된 수
        WebObjectSource objectSource = factory.getObjectSource();
        LicenseSource licenseSource = factory.getLicenseSource();
        NamedUserLicense[] namedUserLicenses = licenseSource.getNamedUserCompliance();

        //Everyone 그룹으로 모든 사용자 검색
        WebUserEntity everyone = (WebUserEntity)objectSource.getObject("C82C6B1011D2894CC0009D9F29718E4F",34,true);

        UserLicenseAudit audit = licenseSource.auditUsers(everyone);
        LicensedUsers enableLicensedUsers = audit.getLicensedUsers(licenseType, EnumDSSXMLAuditUserFilter.DssXmlAuditEnabledUsers);
        LicensedUsers disableLicensedUsers = audit.getLicensedUsers(licenseType, EnumDSSXMLAuditUserFilter.DssXmlAuditDisabledUsers);

        List<UserVO> enableUsers = new ArrayList<>();
        List<UserVO> disableUsers = new ArrayList<>();

        for(int i=0; i<enableLicensedUsers.size(); i++){
            WebUser webUser = (WebUser) enableLicensedUsers.get(i);
            webUser.populate();
            UserVO user = UserVO.builder().
                    userId(webUser.getID()).
                    loginID(webUser.getLoginName()).
                    userNm(webUser.getDisplayName()).
                    owner(webUser.getOwner().getDisplayName()).
                    modification(webUser.getModificationTime()).
                    description(webUser.getDescription()).
                    enableStatus(webUser.isEnabled()).
                    build();
            enableUsers.add(user);
        }

        for(int i=0; i<disableLicensedUsers.size(); i++){
            WebUser webUser = (WebUser) disableLicensedUsers.get(i);
            webUser.populate();
            UserVO user = UserVO.builder().
                    userId(webUser.getID()).
                    loginID(webUser.getLoginName()).
                    userNm(webUser.getDisplayName()).
                    owner(webUser.getOwner().getDisplayName()).
                    modification(webUser.getModificationTime()).
                    description(webUser.getDescription()).
                    enableStatus(webUser.isEnabled()).
                    build();
            disableUsers.add(user);
        }

        LicenseVO licenseInfo = new LicenseVO();
        for(int i=0; i<namedUserLicenses.length; i++){
            if(licenseType == namedUserLicenses[i].getLicenseType()){
                licenseInfo.setLicenseNm(namedUserLicenses[i].getName());
                licenseInfo.setLicenseType(namedUserLicenses[i].getLicenseType());
            }
        }

        licenseInfo.setEnableUsers(enableUsers);
        licenseInfo.setDisableUsers(disableUsers);

        return licenseInfo;
    }

    /**
     * 대시보드 문서 정보 조회
     * @Method Name   : getReportInfo
     * @Date / Author : 2023.12.01  이도현
     * @param userId 사용자 ID
     * @return 리포트 정보 객체
     * @History
     * 2023.12.01	최초생성
     */
    public ReportVO getDashboardReport(String userId) {

        //리포트 정보 조회 할 객체 생성
        ReportVO reportInfo = null;
        String reportId = "";
        try {

            //ObjectSource 플래그에 Comments까지 추출하도록 정보 저장
            objectSource.setFlags(objectSource.getFlags() | EnumDSSXMLObjectFlags.DssXmlObjectComments);
            WebObjectInfo wUser = objectSource.getObject(userId, EnumDSSXMLObjectTypes.DssXmlTypeUser);

            //채워넣기
            wUser.populate();

            // 사용자 정보 객체 채워 넣기
            WebUser user = (WebUser) wUser;
            user.populate();


            try {
                 reportId = user.getComments()[0];
            }catch (NullPointerException ne){
                throw new CustomException(ResultCode.NO_DASHBOARD);
            }

            //문서 정보 객체 채워 넣기 (우선 Document, 도씨에만 가능하게 지정)
            WebObjectInfo wReport = objectSource.getObject(reportId, EnumDSSXMLObjectTypes.DssXmlTypeDocumentDefinition);
            wReport.populate();

            reportInfo = ReportVO.builder().
                    reportId(wReport.getID()).
                    reportNm(wReport.getDisplayName()).
                    build();

        }catch (WebObjectsException woe) {
            log.error("대시보드 불러오는 도중 오류 발생 [Error msg]: "+ woe.getMessage());
            throw new CustomException(ResultCode.INVALID_DOCUMENT_ID);
        }catch (IllegalArgumentException iae){
            log.error("대시보드 불러오는 도중 오류 발생 [Error msg]: "+ iae.getMessage());
            throw new CustomException(ResultCode.INVALID_DOCUMENT_ID);
        }

        // 기본 프롬프트 정보 위한 적용
        try {
        //1. WebInstance 객체 생성
        WebDocumentInstance documentInstance = documentSource.getNewInstance(reportId);
        //Prompts 객체 (Prompt List) 선언
        WebPrompts webPrompts = null;

            //1-2. 해당 문서에 프롬프트가 존재하는지 확인 존재하지 않을경우 return
            if (documentInstance.pollStatus() != EnumDSSXMLStatus.DssXmlStatusPromptXML) {
                reportInfo.setPromptExist("N");
            }
            else {
                //1-2. 프롬프트 존재 시 변수 설정
                reportInfo.setPromptExist("Y");

                //1-3. WebPrompts 객체 조회
                webPrompts = documentInstance.getPrompts();
                reportInfo.setPromptAnswerXML(webPrompts.getShortAnswerXML());
            }
        }catch (WebObjectsException woe){
            log.error("대시보드의 프롬프트 불러오는 도중 오류 발생 [Error msg]: "+ woe.getMessage());
            throw new CustomException(ResultCode.INVALID_PROMPT);
        }

        return reportInfo;
    }

    /**
     * 대시보드 등록
     * @Method Name   : getReportInfo
     * @Date / Author : 2023.12.01  이도현
     * @param userId 사용자 ID
     * @return 리포트 정보 객체
     * @History
     * 2023.12.01	최초생성
     */
    public ResVO registerDashboard(String userId, String reportId) {
        WebObjectInfo woi = null;
            try {
                woi = objectSource.getObject(userId, EnumDSSXMLObjectTypes.DssXmlTypeUser);
            }catch (WebObjectsException | IllegalArgumentException e){
                log.error("대시 보드 등록의 사용자 조회 중 에러 발생 [Error msg]: " + e.getMessage());
                throw new CustomException(ResultCode.INVALID_USER_ID);
            }

        WebUser user = (WebUser) woi;
            try {
                if (user != null) {
                    String[] comments = {reportId};
                    user.setComments(comments);
                    //Save the group object
                    objectSource.save(user);
                }
            }catch (WebObjectsException woe){
                log.error("대시 보드 등록 중 에러 발생 [Error msg]: " + woe.getMessage());
                throw new CustomException(ResultCode.INVALID_REGISTER);
            }
            return new ResVO(ResultCode.SUCCESS);
    }

    public ResVO savePrivileges(PrivilegeAssignVO privilegeList) throws WebObjectsException {

        //ObjectSourcec 객체 생성
        WebObjectSource objectSource = factory.getObjectSource();

        //MicroStrategy Groups의 ID를 가지고 User WebObjectInfo로 변경
        WebObjectInfo woi = objectSource.getObject(privilegeList.getRoleId() ,EnumDSSXMLObjectTypes.DssXmlTypeSecurityRole);

        //채워넣기
        woi.populate();

        // 사용자 그룹 객체
        WebSecurityRole role = (WebSecurityRole) woi;
        role.populate();


        // 배당 보안역할의 권한을 뽑아내기 위해 권한목록 객체 생성
        WebPrivilegeCategories categories = objectSource.getUserServicesSource().getPrivilegeCategories(role);
        for(int i=0; i<privilegeList.getAddedPrivileges().size(); i++){
            categories.getItemByType(privilegeList.getAddedPrivileges().get(i).getCategoryType()).
                    getItemByPrivilegeType(privilegeList.getAddedPrivileges().get(i).getPrivilegeType()).grant();
        }

        for(int i=0; i<privilegeList.getRemovedPrivileges().size(); i++){
            categories.getItemByType(privilegeList.getRemovedPrivileges().get(i).getCategoryType()).
                    getItemByPrivilegeType(privilegeList.getRemovedPrivileges().get(i).getPrivilegeType()).revoke();
        }

        objectSource.save(role);

        return new ResVO(ResultCode.SUCCESS);
    }
}
