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

    private LicenseSource licenseSource = factory.getLicenseSource();

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

    /**
     * 그룹 리스트를 조회
     * @Method Name   : getGroupList
     * @Date / Author : 2023.12.01  이도현
     * @return 그룹 객체 리스트
     * @History
     * 2023.12.01	최초생성
     */
    public List<GroupVO> getGroupList() {

        //1. 결과 객체 생성
        List<GroupVO> groupList = new ArrayList<>();

        //1-1. WebSearch 객체 생성
        try {
            WebSearch search = objectSource.getNewSearchObject();

            //2. 검색 설정 (패턴, 타입, 도메인)
            search.setNamePattern("*" + "" + "*");
            search.setSearchFlags(search.getSearchFlags() + EnumDSSXMLSearchFlags.DssXmlSearchNameWildCard + EnumDSSXMLSearchFlags.DssXmlSearchRootRecursive);
            search.setAsync(false);
            search.types().add(EnumDSSXMLObjectSubTypes.DssXmlSubTypeUserGroup);
            search.setDomain(EnumDSSXMLSearchDomain.DssXmlSearchDomainConfiguration);

            //3. 검색
            search.submit();

            //4. 결과 값 파싱
            WebFolder f = search.getResults();

            //5. 생성일자를 위한 Date Format 지정
            SimpleDateFormat normalParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat originalParser = new SimpleDateFormat("yy-MM-dd a hh:mm:ss");
            Date creationTime = null;

            //6. 결과 만큼 반복하여 그룹 정보 파싱
            if (f.size() > 0) {
                for (int i = 0; i < f.size(); i++) {
                    WebUserGroup group = (WebUserGroup) f.get(i);

                    /**populate로 인한 속도 저하 문제 (해결 가능한지 확인) **/
                    group.populate();
                    try {
                        creationTime = originalParser.parse(group.getCreationTime());
                    } catch (Exception e) {
                        log.error("생성일자 포맷 변경 중 에러 발생 [Error msg]: " + e.getMessage());
                        throw new CustomException(ResultCode.INVALID_CREATION_TIME);
                    }
                    if (group.isGroup()) {
                        GroupVO groupResult = GroupVO.builder().
                                groupId(group.getID()).
                                groupNm(group.getName()).
                                childCnt(group.getTotalChildCount()).
                                description(group.getDescription()).
                                creationTime(normalParser.format(creationTime)).
                                owner(group.getOwner().getName()).
                                build();

                        groupList.add(groupResult);
                    }
                }
            }
        }catch(WebObjectsException woe){
            log.error("그룹 리스트 조회 중 에러 발생 [Error msg]: " + woe.getMessage());
            throw new CustomException(ResultCode.MSTR_ETC_ERROR);
        }

        //7. 반환
        return groupList;
    }

    /**
     * 사용자 리스트를 조회한다.
     * @Method Name   : getUserList
     * @Date / Author : 2023.12.01  이도현
     * @return 사용자 객체 리스트
     * @History
     * 2023.12.01	최초생성
     */
    public List<UserVO> getUserList(){

        //1. 결과 객체 생성
        List<UserVO> userList = new ArrayList<>();

        //1-1. WebSearch 객체 생성
        try{
            WebSearch search = objectSource.getNewSearchObject();

            //2. 검색 설정 (패턴, 타입, 도메인)
            search.setNamePattern("*" + "" + "*");
            search.setSearchFlags(search.getSearchFlags() + EnumDSSXMLSearchFlags.DssXmlSearchNameWildCard + EnumDSSXMLSearchFlags.DssXmlSearchRootRecursive);
            search.setAsync(false);
            search.types().add(EnumDSSXMLObjectSubTypes.DssXmlSubTypeUser);
            search.setDomain(EnumDSSXMLSearchDomain.DssXmlSearchDomainConfiguration);

            //3. 검색
            search.submit();
            //4. 결과 값 파싱
            WebFolder f = search.getResults();

            //5. 생성일자를 위한 Date Format 지정
            SimpleDateFormat normalParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat originalParser = new SimpleDateFormat("yy-MM-dd a hh:mm:ss");
            Date modification = null;

            //6. 결과 만큼 반복하여 사용자 정보 파싱
            if (f.size() > 0) {
                for (int i = 0; i < f.size(); i++) {
                    WebUser user = (WebUser) f.get(i);

                    /**populate로 인한 속도 저하 문제 (해결 가능한지 확인) **/
                    user.populate();
                    try {
                        modification = originalParser.parse(user.getModificationTime());
                    } catch (Exception e) {
                        log.error("생성일자 포맷 변경 중 에러 발생 [Error msg]: " + e.getMessage());
                        throw new CustomException(ResultCode.INVALID_CREATION_TIME);
                    }
                    if (!user.isGroup()) {
                        UserVO userResult = UserVO.builder().
                                enableStatus(user.isEnabled()).
                                userId(user.getID()).
                                loginID(user.getLoginName()).
                                userNm(user.getDisplayName()).
                                owner(user.getOwner().getDisplayName()).
                                modification(normalParser.format(modification)).
                                description(user.getDescription()).
                                build();
                        userList.add(userResult);
                    }
                }
            }
        }catch(WebObjectsException woe){
            log.error("사용자 리스트 조회 중 에러 발생 [Error msg]: " + woe.getMessage());
            throw new CustomException(ResultCode.MSTR_ETC_ERROR);
        }
        //7. 반환
        return userList;
    }

    /**
     * 보안역할 리스트를 조회
     * @Method Name   : getRoleList
     * @Date / Author : 2023.12.01  이도현
     * @return 보안역할 객체 리스트
     * @History
     * 2023.12.01	최초생성
     */
    public List<RoleVO> getRoleList(){

        //1. 결곽 객체 생성
        List<RoleVO> roleList = new ArrayList<>();

        //1-1. WebSearch 객체 생성
        try {
            WebSearch search = objectSource.getNewSearchObject();

            //2. 검색 설정 (패턴, 타입. 도메인)
            search.setNamePattern("*" + "" + "*");
            search.setSearchFlags(search.getSearchFlags() + EnumDSSXMLSearchFlags.DssXmlSearchNameWildCard + EnumDSSXMLSearchFlags.DssXmlSearchRootRecursive);
            search.setAsync(false);
            search.types().add(EnumDSSXMLObjectSubTypes.DssXmlSubTypeSecurityRole);
            search.setDomain(EnumDSSXMLSearchDomain.DssXmlSearchDomainConfiguration);

            //3. 검색
            search.submit();

            //4. 결과 값 파싱
            WebFolder f = search.getResults();

            //5. 생성일자를 위한 Date Format 지정
            SimpleDateFormat normalParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat originalParser = new SimpleDateFormat("yy-MM-dd a hh:mm:ss");
            Date modification = null;

            //6. 결과 만큼 반복하여 보안역할 정보 파싱
            if (f.size() > 0) {
                for (int i = 0; i < f.size(); i++) {
                    WebSecurityRole role = (WebSecurityRole) f.get(i);

                    /**populate로 인한 속도 저하 문제 (해결 가능한지 확인) **/
                    role.populate();

                    try {
                        modification = originalParser.parse(role.getModificationTime());
                    } catch (Exception e) {
                        log.error("생성일자 포맷 변경 중 에러 발생 [Error msg]: " + e.getMessage());
                        throw new CustomException(ResultCode.INVALID_CREATION_TIME);
                    }
                    RoleVO roleResult = RoleVO.builder().
                            roleId(role.getID()).
                            roleNm(role.getDisplayName()).
                            owner(role.getOwner().getDisplayName()).
                            modification(normalParser.format(modification)).
                            description(role.getDescription()).
                            build();

                    roleList.add(roleResult);
                }
            }
        }catch(WebObjectsException woe){
            log.error("보안역할 리스트 조회 중 에러 발생 [Error msg]: " + woe.getMessage());
            throw new CustomException(ResultCode.MSTR_ETC_ERROR);
        }
        return roleList;
    }

    /**
     * 그룹 생성
     * @Method Name   : addGroup
     * @Date / Author : 2023.12.01  이도현
     * @param groupInfo group 정보 객체
     * @return 성공 유무
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    public ResVO addGroup(GroupVO groupInfo){

        //1. 그룹명 유효성 검사
        if(!isGroupNm(groupInfo.getGroupNm()) || groupInfo.getGroupNm().length() > 64){
            log.error("그룹 생성 중 그룹명 유효성 검사 에러 발생 [Error msg]");
            throw new CustomException(ResultCode.INVALID_GROUP_NAME);
        }
        //1-1. 설명 유효성 검사
        else if(groupInfo.getDescription().length() > 200){
            log.error("그룹 생성 중 설명 유효성 검사 에러 발생 [Error msg]");
            throw new CustomException(ResultCode.INVALID_REMARK);
        }

        //2. 사용자 그룹 객체 생성
        UserGroupBean group = null;

        //3. 생성
        try {
            group = (UserGroupBean) BeanFactory.getInstance().newBean("UserGroupBean");
            group.setSessionInfo(serverSession);
            group.InitAsNew();
            group.getUserEntityObject().setFullName(groupInfo.getGroupNm());
            group.getUserEntityObject().setDescription(groupInfo.getDescription());
            group.save();

        } catch (WebBeanException wbe) {
            //4. 이미 존재하는 그룹명 예외처리
            if(wbe.getErrorCode() == -2147217373){
                log.error("그룹 생성 중 중복 그룹명 에러 발생 [Error msg] :" + wbe.getMessage());
                throw new CustomException(ResultCode.DUPLICATE_GROUP);
            }

            //4-1. 그룹명의 유효성 미통과 예외처리
            else if(wbe.getErrorCode() == -2147213718){
                log.error("그룹 생성 중 유효성 에러 발생 [Error msg] :" + wbe.getMessage());
                throw new CustomException(ResultCode.INVALID_GROUP_NAME);
            }

        }
        //5. 응답
        return new ResVO(ResultCode.SUCCESS);
    }

    /**
     * 보안역할 생성
     * @Method Name   : addRole
     * @Date / Author : 2023.12.01  이도현
     * @param roleInfo role 정보 객체
     * @return 성공 유무
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    public ResVO addRole(RoleVO roleInfo){

        //1. 보안역할명 유효성 검사
        if(!isRoleNm(roleInfo.getRoleNm()) ||roleInfo.getRoleNm().length() > 64){
            throw new CustomException(ResultCode.INVALID_ROLE_NAME);
        }
        //1-1. 설명 유효성 검사
        else if(roleInfo.getDescription().length() > 200){
            throw new CustomException(ResultCode.INVALID_REMARK);
        }

        //2. 보안역할 객체 생성
        SecurityRoleBean role = null;

        //3. 생성
        try{
            role = (SecurityRoleBean) BeanFactory.getInstance().newBean("SecurityRoleBean");
            role.setSessionInfo(serverSession);
            role.InitAsNew();
            role.getSecurityRole().setName(roleInfo.getRoleNm());
            role.getSecurityRole().setDescription(roleInfo.getDescription());
            role.save();

        }catch (WebBeanException wbe){
            //4. 이미 존재하는 보안역할명 예외 처리
            if(wbe.getErrorCode() == -2147217373){
                throw new CustomException(ResultCode.DUPLICATE_ROLE);
            }
            //4-1. 보안역할명 유효성 미통과 예외 처리
            else if(wbe.getErrorCode() == -2147213718){
                throw new CustomException(ResultCode.INVALID_ROLE_NAME);
            }
        }

        //5. 응답
        return new ResVO(ResultCode.SUCCESS);
    }

    /**
     * 사용자 생성
     * @Method Name   : addUser
     * @Date / Author : 2023.12.01  이도현
     * @param userInfo group 정보 객체
     * @return 성공 유무
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    public ResVO addUser(UserVO userInfo){

        //1. 사용자 로그인 ID 유효성 검사
        if(!isUserNm(userInfo.getLoginID()) ||userInfo.getLoginID().length() > 64){
            throw new CustomException(ResultCode.INVALID_LOGIN_ID);
        }

        //1-1. 사용자 명 유효성 검사
        else if(!isUserNm(userInfo.getUserNm()) || userInfo.getUserNm().length() > 64){
            throw new CustomException(ResultCode.INVALID_USER_NAME);
        }

        //1-2. 사용자 패스워드 유효성 검사
        else if(!userInfo.getPassword1().equals(userInfo.getPassword2())){
            throw new CustomException(ResultCode.INVALID_PASSWORD);
        }

        //1-3. 사용자 패스워드 유효성 검사
        else if(!isPasswordValid(userInfo.getPassword1(),userInfo.getUserNm())){
            throw new CustomException(ResultCode.INVALID_PASSWORD_POLICY);
        }

        //1-4. 설명 유효성 검사
        else if(userInfo.getDescription().length() > 200){
            throw new CustomException(ResultCode.INVALID_REMARK);
        }

        //3. 사용자 객체 생성
        UserBean user = null;

        //4. 생성
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

        } catch (WebBeanException wbe) {
            if(wbe.getErrorCode() == -2147213795){
                log.error("사용자 생성 중 중복 로그인 ID 에러 발생 [Error msg] :" + wbe.getMessage());
                throw new CustomException(ResultCode.EXIST_LOGIN_ID);
            }
            else{
                throw new CustomException(ResultCode.MSTR_ETC_ERROR);
            }
        }
        return new ResVO(ResultCode.SUCCESS);
    }

    /**
     * 그룹 수정
     * @Method Name   : groupModifyProc
     * @Date / Author : 2023.12.01  이도현
     * @param groupInfo group 정보 객체
     * @return 성공 유무
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    public ResVO modifyGroup(GroupVO groupInfo){

        //1. 그룹명 유효성 검사
        if(!isGroupNm(groupInfo.getGroupNm()) || groupInfo.getGroupNm().length() > 64){
            log.error("그룹 생성 중 그룹명 유효성 검사 에러 발생 [Error msg]");
            throw new CustomException(ResultCode.INVALID_GROUP_NAME);
        }
        //2. 설명 유효성 검사
        else if(groupInfo.getDescription().length() > 200){
            log.error("그룹 생성 중 설명 유효성 검사 에러 발생 [Error msg]");
            throw new CustomException(ResultCode.INVALID_REMARK);
        }

        try {
            //3. 그룹 객체 생성
            WebUserGroup group = (WebUserGroup) objectSource.getObject(groupInfo.getGroupId(), EnumDSSXMLObjectTypes.DssXmlTypeUser);

            //4. 정보 삽입 (그룹명, 설명)
            group.setFullName(groupInfo.getGroupNm());
            group.setDescription(groupInfo.getDescription());

            //5. 저장
            objectSource.save(group);

        }catch (WebObjectsException woe){
            log.error("그룹 생성 중 중복 그룹명 에러 발생 [Error msg] :" + woe.getMessage());
            throw new CustomException(ResultCode.DUPLICATE_GROUP);
        }catch (IllegalArgumentException iae){
            log.error("그룹 생성 중 그룹 조회 에러 발생 [Error msg] :" + iae.getMessage());
            throw new CustomException(ResultCode.INVALID_GROUP_ID);
        }
        return new ResVO(ResultCode.SUCCESS);
    }

    /**
     * 사용자 수정
     * @Method Name   : modifyUser
     * @Date / Author : 2023.12.01  이도현
     * @param userInfo user 정보 객체
     * @return 성공 유무
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    public ResVO modifyUser(UserVO userInfo){

        //1. 사용자 로그인ID 유효성 검사
        if(!isUserNm(userInfo.getLoginID()) ||userInfo.getLoginID().length() > 64){
            throw new CustomException(ResultCode.INVALID_LOGIN_ID);
        }
        //2. 사용자명 유효성 검사
        else if(!isUserNm(userInfo.getUserNm()) || userInfo.getUserNm().length() > 64){
            throw new CustomException(ResultCode.INVALID_USER_NAME);
        }
        //3. 설명 유효성 검사
        else if(userInfo.getDescription().length() > 200){
            throw new CustomException(ResultCode.INVALID_REMARK);
        }

        try {
            //4. 사용자 객체 생성
            WebUser user = (WebUser) objectSource.getObject(userInfo.getUserId(), EnumDSSXMLObjectTypes.DssXmlTypeUser);

            //5. 정보 삽입 (사용자명, 설명, 로그인ID)
            user.setFullName(userInfo.getUserNm());
            user.setDescription(userInfo.getDescription());
            user.setLoginName(userInfo.getLoginID());

            //6. 저장
            objectSource.save(user);

        }catch (WebObjectsException woe){
            log.error("사용자 생성 중 중복 로그인ID 에러 발생 [Error msg] :" + woe.getMessage());
            throw new CustomException(ResultCode.EXIST_LOGIN_ID);
        }catch (IllegalArgumentException iae){
            log.error("사용자 생성 중성 사용자 조회 에러 발생 [Error msg] :" + iae.getMessage());
            throw new CustomException(ResultCode.INVALID_USER_ID);
        }
        return new ResVO(ResultCode.SUCCESS);
    }

    /**
     * 보안역할 수정
     * @Method Name   : modifyRole
     * @Date / Author : 2023.12.01  이도현
     * @param roleInfo role 정보 객체
     * @return 성공 유무
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    public ResVO modifyRole(RoleVO roleInfo){

        //1. 보안역할명 유효성 검사
        if(!isRoleNm(roleInfo.getRoleNm()) || roleInfo.getRoleNm().length() > 64){
            log.error("그룹 생성 중 그룹명 유효성 검사 에러 발생 [Error msg]");
            throw new CustomException(ResultCode.INVALID_ROLE_NAME);
        }
        //2. 설명 유효성 검사
        else if(roleInfo.getDescription().length() > 200){
            log.error("그룹 생성 중 설명 유효성 검사 에러 발생 [Error msg]");
            throw new CustomException(ResultCode.INVALID_REMARK);
        }

        try {
            //3. 보안역할 객체 생성
            WebSecurityRole role = (WebSecurityRole) objectSource.getObject(roleInfo.getRoleId(), EnumDSSXMLObjectTypes.DssXmlTypeSecurityRole);
            role.populate();

            //4. 정보 삽입 (보안역할명, 설명)
            role.setName(roleInfo.getRoleNm());
            role.setDisplayName(roleInfo.getRoleNm());
            role.setDescription(roleInfo.getDescription());

            //5. 저장
            objectSource.save(role);

        }catch (WebObjectsException woe){
            log.error("보안역할 수정 중 중복 에러 발생 [Error msg] :" + woe.getMessage());
            throw new CustomException(ResultCode.DUPLICATE_ROLE);
        }catch (IllegalArgumentException iae){
            log.error("보안역할 수정 중 중복 보안역할 조회 에러 발생 [Error msg] :" + iae.getMessage());
            throw new CustomException(ResultCode.INVALID_ROLE_ID);
        }
        return new ResVO(ResultCode.SUCCESS);
    }

    /**
     * 그룹 삭제
     * @Method Name   : deleteGroup
     * @Date / Author : 2023.12.01  이도현
     * @param groupId 삭제할 그룹 정보
     * @return 성공 유무
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    public ResVO deleteGroup(String groupId){

        try {
            //1. 그룹 객체 조회
            WebUserGroup group = (WebUserGroup) objectSource.getObject(groupId, EnumDSSXMLObjectTypes.DssXmlTypeUser);
            //2. 그룹 객체 삭제
            objectSource.deleteObject(group);

        }catch (WebObjectsException woe){
            log.error("그룹 삭제 중 MSTR 연계 에러 발생 [Error msg] :" + woe.getMessage());
            throw new CustomException(ResultCode.MSTR_ETC_ERROR);
        }catch (IllegalArgumentException iae){
            log.error("그룹 삭제 중 그룹 조회 에러 발생 [Error msg] :" + iae.getMessage());
            throw new CustomException(ResultCode.INVALID_GROUP_ID);
        }
        return new ResVO(ResultCode.SUCCESS);
    }

    /**
     * 사용자 삭제
     * @Method Name   : deleteUser
     * @Date / Author : 2023.12.01  이도현
     * @param userId 삭제할 사용자 정보
     * @return 성공 유무
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    public ResVO deleteUser(String userId){

        try {
            //1. 사용자 객체 조회
            WebUser user = (WebUser) objectSource.getObject(userId, EnumDSSXMLObjectTypes.DssXmlTypeUser);
            //2. 사용자 객체 삭제
            objectSource.deleteObject(user);

        }catch (WebObjectsException woe){
            log.error("사용자 삭제 중 MSTR 연계 에러 발생 [Error msg] :" + woe.getMessage());
            throw new CustomException(ResultCode.MSTR_ETC_ERROR);
        }catch (IllegalArgumentException iae){
            log.error("사용자 삭제 중 그룹 조회 에러 발생 [Error msg] :" + iae.getMessage());
            throw new CustomException(ResultCode.INVALID_USER_ID);
        }
        return new ResVO(ResultCode.SUCCESS);
    }

    /**
     * 보안역할 삭제
     * @Method Name   : deleteRole
     * @Date / Author : 2023.12.01  이도현
     * @param roleId 삭제할 보안역할 정보
     * @return 성공 유무
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    public ResVO delRole(String roleId){

        try {
            //1. 보안역할 객체 조회
            WebSecurityRole role = (WebSecurityRole) objectSource.getObject(roleId, EnumDSSXMLObjectTypes.DssXmlTypeSecurityRole);
            //2. 보안역할 객체 삭제
            objectSource.deleteObject(role);

        }catch (WebObjectsException woe){
            log.error("보안역할 삭제 중 MSTR 연계 에러 발생 [Error msg] :" + woe.getMessage());
            throw new CustomException(ResultCode.MSTR_ETC_ERROR);
        }catch (IllegalArgumentException iae){
            log.error("보안역할 삭제 중 보안역할 조회 에러 발생 [Error msg] :" + iae.getMessage());
            throw new CustomException(ResultCode.INVALID_ROLE_ID);
        }
        return new ResVO(ResultCode.SUCCESS);
    }

    /**
     * 그룹 정보 조회
     * @Method Name   : getGroupInfo
     * @Date / Author : 2023.12.01  이도현
     * @param groupId 조회할 그룹의 Id
     * @return 그룹 정보 객체
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    //그룹 정보
    public GroupVO getGroupInfo(String groupId){

        //1. 응답할 객체 생성
        GroupVO groupInfo = null;

        try {
            //2. MicroStrategy Groups의 ID를 가지고 User WebObjectInfo로 변경
            WebObjectInfo woi = objectSource.getObject(groupId, EnumDSSXMLObjectTypes.DssXmlTypeUser);

            //2-1. 채워넣기
            woi.populate();

            //2-2. 생성일자를 위한 Date Format 지정
            SimpleDateFormat normalParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat originalParser = new SimpleDateFormat("yy-MM-dd a hh:mm:ss");
            Date creationTime = null;

            try {
                creationTime = originalParser.parse(woi.getCreationTime());
            } catch (Exception e) {
                log.error("생성일자 포맷 변경 중 에러 발생 [Error msg]: " + e.getMessage());
                throw new CustomException(ResultCode.INVALID_CREATION_TIME);
            }

            //3. 사용자 그룹 객체 세팅
            WebUserGroup group = (WebUserGroup) woi;
            group.populate();
             groupInfo =GroupVO.builder().
                        groupId(group.getID()).
                        groupNm(group.getDisplayName()).
                        childCnt(group.getTotalChildCount()).
                        creationTime(normalParser.format(creationTime)).
                        description(group.getDescription()).
                        owner(group.getOwner().getDisplayName()).
                        build();

        }catch (WebObjectsException woe){
            log.error("그룹 정보 조회 중 에러 발생 [Error msg]: " + woe.getMessage());
            throw new CustomException(ResultCode.MSTR_ETC_ERROR);
        }
        return groupInfo;
    }

    /**
     * 사용자 정보 조회 (ID)
     * @Method Name   : getUserInfoById
     * @Date / Author : 2023.12.01  이도현
     * @param userId 조회할 사용자의 Id
     * @return 사용자 정보 객체
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    public UserVO getUserInfoById(String userId){

        //1. 응답할 객체 생성
        UserVO userInfo = null;

        try{
            //2. MicroStrategy Groups의 ID를 가지고 User WebObjectInfo로 변경
            WebObjectInfo woi = objectSource.getObject(userId ,EnumDSSXMLObjectTypes.DssXmlTypeUser);

            //2-1. 채워넣기
            woi.populate();

            //2-2. 생성일자를 위한 Date Format 지정
            SimpleDateFormat normalParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat originalParser = new SimpleDateFormat("yy-MM-dd a hh:mm:ss");
            Date modification = null;

            try {
                modification = originalParser.parse(woi.getModificationTime());
            } catch (Exception e) {
                log.error("생성일자 포맷 변경 중 에러 발생 [Error msg]: " + e.getMessage());
                throw new CustomException(ResultCode.INVALID_CREATION_TIME);
            }

            //3. 사용자 객체 세팅
            WebUser user = (WebUser) woi;
            user.populate();
            userInfo = UserVO.builder().
                    userId(user.getID()).
                    loginID(user.getLoginName()).
                    userNm(user.getDisplayName()).
                    owner(user.getOwner().getDisplayName()).
                    modification(normalParser.format(modification)).
                    description(user.getDescription()).
                    enableStatus(user.isEnabled()).
                    build();

        }catch (WebObjectsException woe){
            log.error("사용자 정보 조회 중 에러 발생 [Error msg]: " + woe.getMessage());
            throw new CustomException(ResultCode.MSTR_ETC_ERROR);
        }
        return userInfo;
    }

    /**
     * 보안역할 정보 조회
     * @Method Name   : getRoleInfo
     * @Date / Author : 2023.12.01  이도현
     * @param roleId 조회할 보안역할의 Id
     * @return 보안역할 정보 객체
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    public RoleVO getRoleInfo(String roleId){

        //1. 응답할 객체 생성
        RoleVO roleInfo = null;
        WebSecurityRole role = null;
        try {
            //2. MicroStrategy role의 ID를 가지고 User WebObjectInfo로 변경
            WebObjectInfo woi = objectSource.getObject(roleId, EnumDSSXMLObjectTypes.DssXmlTypeSecurityRole);

            //2-1. 채워넣기
            woi.populate();

            //2-2. 보안역할 객체로 캐스팅
            role = (WebSecurityRole) woi;
            role.populate();

            //2-2. 생성일자를 위한 Date Format 지정
            SimpleDateFormat normalParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat originalParser = new SimpleDateFormat("yy-MM-dd a hh:mm:ss");
            Date modification = null;

            try {
                modification = originalParser.parse(role.getModificationTime());
            } catch (Exception e) {
                log.error("생성일자 포맷 변경 중 에러 발생 [Error msg]: " + e.getMessage());
                throw new CustomException(ResultCode.INVALID_CREATION_TIME);
            }

            //3. 보안역할 정보 파싱
            roleInfo = RoleVO.builder().
                    roleId(role.getID()).
                    roleNm(role.getDisplayName()).
                    owner(role.getOwner().getDisplayName()).
                    modification(normalParser.format(modification)).
                    description(role.getDescription()).
                    build();

        }catch (WebObjectsException woe){
            log.error("보안역할 정보 조회 중 에러 발생 [Error msg]: " + woe.getMessage());
            throw new CustomException(ResultCode.MSTR_ETC_ERROR);
        }

        //4. 보안역할의 권한 리스트 객체 생성
        List<CategoryVO> categoryList = new ArrayList<>();

        //5. 할당된 권한에 대한 리스트 객체 생성
        List<Integer> assignIds = new ArrayList<>();

        //5-1. 기본 값 상태 정보 값 초기화
        String assignYn = "N";
        StateVO state = null;

        //5-2. 해당 보안역할의 할당 된 권한 추출
        for(int i=0; i<role.getPrivileges().size(); i++){
            assignIds.add(role.getPrivileges().get(i).getType());
        }

        //5-3. 현재 라이센스에서 사용중인 모든 카테고리 (권한모음) 을 추출함.
        WebPrivilegeCategories categories = null;
        try {
            categories = objectSource.getUserServicesSource().getPrivilegeCategories(role);
        }catch (WebObjectsException woe){
            log.error("보안역할 정보 조회 중 에러 발생 [Error msg]: " + woe.getMessage());
            throw new CustomException(ResultCode.MSTR_ETC_ERROR);
        }

        //5-4. 카테고리 수만큼 반복하여 파싱
        for(int i=0; i< categories.size(); i++){
            CategoryVO category = CategoryVO.builder().
                    categoryNm(categories.get(i).getName()).
                    categoryType(categories.get(i).getType()).
                    type("cat").
                    text(categories.get(i).getName()).
                    build();

            //6. 권한 리스트 객체 생성
            List<PrivilegeVO> privilegeList = new ArrayList<>();

            //7. 카테고리의 권한 수만큼 파싱
            for(int j=0; j < categories.get(i).size(); j++){

                //7-1. 할당 유무에 따라 분기 처리
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
        //8. 응답
        roleInfo.setCategories(categoryList);

        return roleInfo;
    }

    /**
     * 그룹에 포함된 사용자 정보 조회
     * @Method Name   : getGroupUserList
     * @Date / Author : 2023.12.01  이도현
     * @param groupInfo 그룹 정보 객체
     * @return 그룹 정보 객체
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    public GroupVO getGroupUserList(GroupVO groupInfo){

        WebObjectInfo woi = null;

        try {
            //1. 그룹 Id로 정보 조회
             woi = objectSource.getObject(groupInfo.getGroupId(), EnumDSSXMLObjectTypes.DssXmlTypeUser);

            //1-1. 그룹 정보 채워넣기
            woi.populate();
        }catch (WebObjectsException woe){
            log.error("그룹 정보의 사용자 리스트 정보 조회 중 에러 발생 [Error msg]: " + woe.getMessage());
            throw new CustomException(ResultCode.INVALID_GROUP_ID);
        }

        //1-2. 사용자 그룹 객체로 캐스팅
        WebUserGroup groups = (WebUserGroup) woi;

        //2. Group하위의 모든 사용자 가져오기위한 UserList 객체 초기화
        WebUserList members = groups.getMembers();

        //2-1. 하위 요소들을 Enumeration으로 치환
        Enumeration  enumeration = members.elements();


        List<UserVO> users = new ArrayList<>();
        List<GroupVO> childGroups = new ArrayList<>();
        List<String> assignIds = new ArrayList<>();
        WebObjectInfo enumWoi = null;

        //2-2. 하위 요소 수 만큼 반복
        while(enumeration.hasMoreElements()){

            try {
                enumWoi = (WebObjectInfo) enumeration.nextElement();
                enumWoi.populate();
            }catch (WebObjectsException woe){
                log.error("그룹 정보의 사용자 리스트 정보 조회 중 에러 발생 [Error msg]: " + woe.getMessage());
                throw new CustomException(ResultCode.MSTR_ETC_ERROR);
            }

            //2-3. 하위 요소의 서브타입이 8704(사용자인 것) 파싱
            if(enumWoi.getSubType() == 8704) {
                WebUser user = (WebUser) enumWoi;
                    assignIds.add(enumWoi.getID());
            }

            /** 그룹 할당의 경우 추가 가능 **/
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

        //3. 모든 사용자 검색을 위한 검색 객체 생성
        WebSearch search = objectSource.getNewSearchObject();

        //3-1. 검색 조건 설정
        search.setNamePattern("*" + "" + "*");
        search.setSearchFlags(search.getSearchFlags() + EnumDSSXMLSearchFlags.DssXmlSearchNameWildCard + EnumDSSXMLSearchFlags.DssXmlSearchRootRecursive);
        search.setAsync(false);
        search.types().add(EnumDSSXMLObjectSubTypes.DssXmlSubTypeUser);
        search.setDomain(EnumDSSXMLSearchDomain.DssXmlSearchDomainConfiguration);

        try {

            //3-2. 검색 및 결과 전달
            search.submit();
            WebFolder f = search.getResults();

            //3-3. 생성일자를 위한 Date Format 지정
            SimpleDateFormat normalParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat originalParser = new SimpleDateFormat("yy-MM-dd a hh:mm:ss");
            Date modification = null;

            if (f.size() > 0) {
                for (int i = 0; i < f.size(); i++) {
                    WebUser user = (WebUser) f.get(i);
                    user.populate();

                    //할당되어진 ID와 비교하여 포함되어져있으면 표시
                    if (!user.isGroup()) {
                        String assignYn = "N";


                        if (assignIds.contains(user.getID())) {
                            assignYn = "Y";
                        }

                        try {
                            modification = originalParser.parse(user.getModificationTime());
                        } catch (Exception e) {
                            log.error("생성일자 포맷 변경 중 에러 발생 [Error msg]: " + e.getMessage());
                            throw new CustomException(ResultCode.INVALID_CREATION_TIME);
                        }

                        UserVO userResult = UserVO.builder().
                                userId(user.getID()).
                                loginID(user.getLoginName()).
                                userNm(user.getDisplayName()).
                                owner(user.getOwner().getDisplayName()).
                                modification(normalParser.format(modification)).
                                description(user.getDescription()).
                                assignYn(assignYn).
                                enableStatus(user.isEnabled()).
                                build();

                        users.add(userResult);
                    }
                }
            }
        }catch (WebObjectsException woe){
            log.error("그룹 정보의 사용자 리스트 정보 조회 중 에러 발생 [Error msg]: " + woe.getMessage());
            throw new CustomException(ResultCode.MSTR_ETC_ERROR);
        }
        return groupInfo;
    }

    /**
     * 사용자에 부모(그룹) 정보 조회
     * @Method Name   : getUserGroupList
     * @Date / Author : 2023.12.01  이도현
     * @param userInfo 사용자 정보 객체
     * @return 사용자 정보 객체
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    public UserVO getUserGroupList(UserVO userInfo){

        WebObjectInfo woi = null;

        try {

            //1. 사용자 Id로 정보 조회
            woi = objectSource.getObject(userInfo.getUserId(), EnumDSSXMLObjectTypes.DssXmlTypeUser);

            //1-1. 사용자 정보 채워넣기
            woi.populate();
        }catch (WebObjectsException woe){
            log.error("사용자 정보의 그룹 리스트 정보 조회 중 에러 발생 [Error msg]: " + woe.getMessage());
            throw new CustomException(ResultCode.INVALID_USER_ID);
        }

        //1-2. 사용자 그룹 객체로 캐스팅
        WebUser user = (WebUser) woi;
        try {
            user.populate();
        }catch (WebObjectsException woe){
            log.error("사용자 정보의 그룹 리스트 정보 조회 중 에러 발생 [Error msg]: " + woe.getMessage());
            throw new CustomException(ResultCode.MSTR_ETC_ERROR);
        }

        //1-3. 사용자에 할당 된 부모(그룹) 파싱
        WebUserList wul = user.getParents();
        Enumeration  enumeration = wul.elements();
        WebObjectInfo enumWoi = null;

        //1-3.1 할당된 그룹 ID를 저장할 리스트 생성
        List<String> assignIds = new ArrayList<>();

        //1-4. 그룹 수 만큼 반복
        while(enumeration.hasMoreElements()) {

            //1-4.1 그룹 채워넣기 및 리스트에 저장
            enumWoi = (WebObjectInfo) enumeration.nextElement();
            assignIds.add(enumWoi.getID());
        }

        //2. 모든 그룹 검색을 위한 검색 객체 생성
        WebSearch search = objectSource.getNewSearchObject();

        //2-1. 검색 조건 설정
        search.setNamePattern("*" + "" + "*");
        search.setSearchFlags(search.getSearchFlags() + EnumDSSXMLSearchFlags.DssXmlSearchNameWildCard + EnumDSSXMLSearchFlags.DssXmlSearchRootRecursive);
        search.setAsync(false);
        search.types().add(EnumDSSXMLObjectSubTypes.DssXmlSubTypeUserGroup);
        search.setDomain(EnumDSSXMLSearchDomain.DssXmlSearchDomainConfiguration);

        try {

            //2-2. 검색 및 결과 전달
            search.submit();
            WebFolder f = search.getResults();

            List<GroupVO> groups = new ArrayList<>();
            userInfo.setParentsGroups(groups);

            //2-3. 생성일자를 위한 Date Format 지정
            SimpleDateFormat normalParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat originalParser = new SimpleDateFormat("yy-MM-dd a hh:mm:ss");
            Date creationTime = null;

            if (f.size() > 0) {
                for (int i = 0; i < f.size(); i++) {
                    WebUserGroup group = (WebUserGroup) f.get(i);
                    group.populate();

                    //할당되어진 ID와 비교하여 포함되어져있으면 표시
                    if (group.isGroup()) {
                        String assignYn = "N";

                        if (assignIds.contains(group.getID())) {
                            assignYn = "Y";
                        }

                        try {
                            creationTime = originalParser.parse(group.getCreationTime());
                        } catch (Exception e) {
                            log.error("생성일자 포맷 변경 중 에러 발생 [Error msg]: " + e.getMessage());
                            throw new CustomException(ResultCode.INVALID_CREATION_TIME);
                        }

                        GroupVO groupResult = GroupVO.builder().
                                groupId(group.getID()).
                                groupNm(group.getName()).
                                childCnt(group.getTotalChildCount()).
                                description(group.getDescription()).
                                creationTime(normalParser.format(creationTime)).
                                owner(group.getOwner().getName()).
                                assignYn(assignYn).
                                build();

                        groups.add(groupResult);
                    }
                }
            }
        }catch (WebObjectsException woe){
            log.error("사용자 정보의 그룹 리스트 정보 조회 중 에러 발생 [Error msg]: " + woe.getMessage());
            throw new CustomException(ResultCode.MSTR_ETC_ERROR);
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

    /**
     * 그룹 사용자 할당
     * @Method Name   : groupAssign
     * @Date / Author : 2023.12.01  이도현
     * @param groupInfo group 정보 객체
     * @return 성공 유무
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    public ResVO assignGroup(GroupVO groupInfo) {

        WebObjectInfo woi = null;
        try {
            //1. 사용자 그룹 조회
             woi = objectSource.getObject(groupInfo.getGroupId(), EnumDSSXMLObjectTypes.DssXmlTypeUser);
        }catch (WebObjectsException woe){
            log.error("그룹 사용자 할당 중 에러 발생 [Error msg] :" + woe.getMessage());
            throw new CustomException(ResultCode.MSTR_ETC_ERROR);
        }catch (IllegalArgumentException iae){
            log.error("그룹 사용자 할당 중 에러 발생 [Error msg] :" + iae.getMessage());
            throw new CustomException(ResultCode.INVALID_GROUP_ID);
        }
            WebUserGroup group = (WebUserGroup) woi;

        try{
            if(group!=null){
                for(int i=0; i<groupInfo.getUsers().size(); i++){
                    //2. 할당, 회수 요청 사용자 조회
                    WebUser webUser = (WebUser) objectSource.getObject(groupInfo.getUsers().get(i).getUserId(), EnumDSSXMLObjectTypes.DssXmlTypeUser);

                    //3. 분기에 맞게 할당 혹은 회수 처리
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
            //4. 저장
            objectSource.save(group);
        }catch (WebObjectsException woe){
            log.error("그룹 사용자 할당 중 에러 발생 [Error msg] :" + woe.getMessage());
            throw new CustomException(ResultCode.ERROR_ADD_USER);
        }catch (IllegalArgumentException iae){
            log.error("그룹 사용자 할당 중 에러 발생 [Error msg] :" + iae.getMessage());
            throw new CustomException(ResultCode.INVALID_USER_ID);
        }
        return new ResVO(ResultCode.SUCCESS);
    }

    /**
     * 사용자 그룹 할당
     * @Method Name   : assignUser
     * @Date / Author : 2023.12.01  이도현
     * @param userInfo user 정보 객체
     * @return 성공 유무
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    public ResVO assignUser(UserVO userInfo) {

        WebObjectInfo woi = null;
        try {
            //1. 사용자 조회
            woi = objectSource.getObject(userInfo.getUserId(), EnumDSSXMLObjectTypes.DssXmlTypeUser);
        }catch (WebObjectsException woe){
            log.error("사용자 그룹 할당 중 에러 발생 [Error msg] :" + woe.getMessage());
            throw new CustomException(ResultCode.MSTR_ETC_ERROR);
        }catch (IllegalArgumentException iae){
            log.error("사용자 그룹 할당 중 에러 발생 [Error msg] :" + iae.getMessage());
            throw new CustomException(ResultCode.INVALID_USER_ID);
        }

            WebUser user = (WebUser) woi;

        try{
            if(user!=null){
                for(int i=0; i<userInfo.getParentsGroups().size(); i++){

                    //2. 할당, 회수할 그룹 조회
                    WebUserGroup groups = (WebUserGroup) objectSource.getObject(userInfo.getParentsGroups().get(i).getGroupId(), EnumDSSXMLObjectTypes.DssXmlTypeUser);

                    //3. 분기에 맞게 할당 혹은 회수 처리
                    if(groups!=null){
                        if("assign".equals(userInfo.getAssignmentType())){
                            groups.getMembers().add(user);
                        }
                        else{
                            groups.getMembers().remove(user);
                        }
                    }
                    //4. 저장
                    objectSource.save(groups);
                }
            }
        }catch (WebObjectsException woe){
            log.error("사용자 그룹 할당 중 에러 발생 [Error msg] :" + woe.getMessage());
            throw new CustomException(ResultCode.ERROR_ADD_USER);
        }catch (IllegalArgumentException iae){
            log.error("사용자 그룹 할당 중 에러 발생 [Error msg] :" + iae.getMessage());
            throw new CustomException(ResultCode.INVALID_GROUP_ID);
        }
        return new ResVO(ResultCode.SUCCESS);
    }

    /**
     * 사용자 계정 활성화/비활성화
     * @Method Name   : enableAccount
     * @Date / Author : 2023.12.01  이도현
     * @param userInfo 조회할 사용자 정보 객체
     * @return 성공 유무
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    public ResVO enableAccount(UserVO userInfo) {

        try {

            //1. 사용자 Id로 객체 조회
            WebObjectInfo woi = objectSource.getObject(userInfo.getUserId(), EnumDSSXMLObjectTypes.DssXmlTypeUser);
            WebUser user = (WebUser) woi;
            user.populate();

            //2. 계정 활성화
            user.setEnabled(userInfo.isEnableStatus());

            //3. 저장
            objectSource.save(user);

        } catch (WebObjectsException woe) {
            log.error("사용자계정 활성화 중 에러 발생 [Error msg] :" + woe.getMessage());
            throw new CustomException(ResultCode.MSTR_ETC_ERROR);
        } catch(IllegalArgumentException iae){
            log.error("사용자 계정 활성화 중 사용자 조회 에러 발생 [Error msg] :" + iae.getMessage());
            return new ResVO(ResultCode.INVALID_USER_ID);
        }
        return new ResVO(ResultCode.SUCCESS);
    }

    /**
     * 사용자 비밀번호 초기화
     * @Method Name   : resetPassword
     * @Date / Author : 2023.12.01  이도현
     * @param userId 조회할 사용자의 Id
     * @return 성공 유무
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    public ResVO resetPassword(String userId){

        try {

            //1. 사용자 Id로 객체 조회
            WebObjectInfo woi = objectSource.getObject(userId, EnumDSSXMLObjectTypes.DssXmlTypeUser);
            WebUser user = (WebUser) woi;

            //2. 사용자 로그인 객체 조회
            WebStandardLoginInfo loginInfo = user.getStandardLoginInfo();

            //3. 비밀번호 설정
            loginInfo.setPassword("12345678");

            //4. 저장
            objectSource.save(user);
        } catch (WebObjectsException woe) {
            if(woe.getErrorCode() == -2147180957){
                log.error("사용자 비밀번호 초기화 중 (비밀번호 정책) 에러 발생 [Error msg] :" + woe.getMessage());
                throw new CustomException(ResultCode.INVALID_PASSWORD_POLICY);
            }
            else{
                log.error("사용자 비밀번호 초기화 중 에러 발생 [Error msg] :" + woe.getMessage());
                throw new CustomException(ResultCode.MSTR_ETC_ERROR);
            }
        }catch (IllegalArgumentException iae){
            log.error("사용자 비밀번호 초기화 중 사용자 조회 에러 발생 [Error msg] :" + iae.getMessage());
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

    /**
     * 라이센스 리스트를 조회한다.
     * @Method Name   : getLicenseList
     * @Date / Author : 2023.12.01  이도현
     * @return 라이센스 객체 리스트
     * @History
     * 2023.12.01	최초생성
     */
    public List<LicenseVO> getLicenseList(){

        //1. named 라이센스 객체, 감사 객체 생성
        NamedUserLicense[] namedUserLicenses = null;
        UserLicenseAudit audit = null;

        try {
            //2. 라이센스 객체 초기화
            namedUserLicenses = licenseSource.getNamedUserCompliance();

            //3. Everyone 그룹으로 모든 사용자 검색
            WebUserEntity everyone = (WebUserEntity) objectSource.getObject("C82C6B1011D2894CC0009D9F29718E4F", 34, true);
            audit = licenseSource.auditUsers(everyone);

        }catch (WebObjectsException woe){
            log.error("라이센스 리스트 조회 중 에러 발생 [Error msg] :" + woe.getMessage());
            throw new CustomException(ResultCode.MSTR_ETC_ERROR);
        }catch (IllegalArgumentException iae){
            log.error("라이센스 리스트 조회 중 그룹 조회 에러 발생 [Error msg] :" + iae.getMessage());
            throw new CustomException(ResultCode.INVALID_GROUP_ID);
        }

        //4. 응답 라이센스 리스트 객체 생성
        List<LicenseVO> licenseList = new ArrayList<>();

        for(int i=0; i< namedUserLicenses.length; i++){

            //5. 총 사용자, 활성화 사용자, 비활성화 사용자 수 조회
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
        //6. 응답
        return licenseList;
    }

    /**
     * 라이센스 정보 조회
     * @Method Name   : getLicenseList
     * @Date / Author : 2023.12.01  이도현
     * @param licenseType 라이센스 타입
     * @return 라이센스 객체 리스트
     * @History
     * 2023.12.01	최초생성
     */
    public LicenseVO getLicenseInfo(int licenseType){

        //1. named 라이센스 객체, 감사 객체 생성
        NamedUserLicense[] namedUserLicenses = null;
        UserLicenseAudit audit = null;

        try {
            //2. 라이센스 객체 초기화
            namedUserLicenses = licenseSource.getNamedUserCompliance();

            //3. Everyone 그룹으로 모든 사용자 검색
            WebUserEntity everyone = (WebUserEntity) objectSource.getObject("C82C6B1011D2894CC0009D9F29718E4F", 34, true);
            audit = licenseSource.auditUsers(everyone);
        }catch (WebObjectsException woe){
            log.error("라이센스 정보 조회 중 에러 발생 [Error msg] :" + woe.getMessage());
            throw new CustomException(ResultCode.MSTR_ETC_ERROR);
        }catch (IllegalArgumentException iae){
            log.error("라이센스 정보 조회 중 그룹 조회 에러 발생 [Error msg] :" + iae.getMessage());
            throw new CustomException(ResultCode.INVALID_GROUP_ID);
        }

        //4. 활성화 라이센스 유저, 비활성화 라이센스 유저 객체 생성
        LicensedUsers enableLicensedUsers = audit.getLicensedUsers(licenseType, EnumDSSXMLAuditUserFilter.DssXmlAuditEnabledUsers);
        LicensedUsers disableLicensedUsers = audit.getLicensedUsers(licenseType, EnumDSSXMLAuditUserFilter.DssXmlAuditDisabledUsers);

        //5. 파싱 객체 생성
        List<UserVO> enableUsers = new ArrayList<>();
        List<UserVO> disableUsers = new ArrayList<>();

        //6. 활성화 사용자 파싱
        for(int i=0; i<enableLicensedUsers.size(); i++){
            try {
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
            }catch (WebObjectsException woe){
                log.error("라이센스 리스트 조회 중 에러 발생 [Error msg] :" + woe.getMessage());
                throw new CustomException(ResultCode.MSTR_ETC_ERROR);
            }
        }

        //7. 비활성화 사용자 파싱
        for(int i=0; i<disableLicensedUsers.size(); i++){
            try{
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
             }catch (WebObjectsException woe){
                log.error("라이센스 리스트 조회 중 에러 발생 [Error msg] :" + woe.getMessage());
                throw new CustomException(ResultCode.MSTR_ETC_ERROR);
             }
        }

        //8. 라이센스 권한(파싱)
        LicenseVO licenseInfo = new LicenseVO();
        for(int i=0; i<namedUserLicenses.length; i++){
            if(licenseType == namedUserLicenses[i].getLicenseType()){
                licenseInfo.setLicenseNm(namedUserLicenses[i].getName());
                licenseInfo.setLicenseType(namedUserLicenses[i].getLicenseType());
            }
        }

        //9. 응답 값 설정
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

    /**
     * 보안역할 권한 할당
     * @Method Name   : savePrivileges
     * @Date / Author : 2023.12.01  이도현
     * @param privilegeList 권한 객체 리스트
     * @return 성공 유무
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    public ResVO savePrivileges(PrivilegeAssignVO privilegeList){

        WebObjectInfo woi = null;
        WebSecurityRole role = null;
        WebPrivilegeCategories categories = null;
        try {
            //1. 보안역할 조회
            woi = objectSource.getObject(privilegeList.getRoleId(), EnumDSSXMLObjectTypes.DssXmlTypeSecurityRole);

            //1-1. 채워넣기
            woi.populate();

            //1-2. 보안역할 객체로 파싱
            role = (WebSecurityRole) woi;
            role.populate();

        }catch (WebObjectsException woe){
            log.error("보안역할 권한 할당 중 에러 발생 [Error msg] :" + woe.getMessage());
            throw new CustomException(ResultCode.MSTR_ETC_ERROR);
        }catch (IllegalArgumentException iae){
            log.error("보안역할 권한 할당 중 보안역할 조회 에러 발생 [Error msg] :" + iae.getMessage());
            throw new CustomException(ResultCode.INVALID_ROLE_ID);
        }

        //2. 해당 보안역할의 권한을 뽑아내기 위해 권한목록 객체 생성
        try {
            categories = objectSource.getUserServicesSource().getPrivilegeCategories(role);


            //3. 할당해야할 권한 리스트만큼 반복하여 권한 할당
            for(int i=0; i<privilegeList.getAddedPrivileges().size(); i++){
                categories.getItemByType(privilegeList.getAddedPrivileges().get(i).getCategoryType()).
                        getItemByPrivilegeType(privilegeList.getAddedPrivileges().get(i).getPrivilegeType()).grant();
            }

            //4. 회수해야할 권한 리스트만큼 반복하여 권한 회수
            for(int i=0; i<privilegeList.getRemovedPrivileges().size(); i++){
                categories.getItemByType(privilegeList.getRemovedPrivileges().get(i).getCategoryType()).
                        getItemByPrivilegeType(privilegeList.getRemovedPrivileges().get(i).getPrivilegeType()).revoke();
            }

            //5. 저장
            objectSource.save(role);

        }catch (WebObjectsException woe){
            log.error("보안역할 권한 할당, 회수 중 에러 발생 [Error msg] :" + woe.getMessage());
            throw new CustomException(ResultCode.MSTR_ETC_ERROR);
        }

        return new ResVO(ResultCode.SUCCESS);
    }
}
