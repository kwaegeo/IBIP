package com.insdiide.ibip.global.mstr;

import com.insdiide.ibip.domain.folder.vo.EntityVO;
import com.insdiide.ibip.domain.login.vo.FolderVO;
import com.insdiide.ibip.domain.main.vo.UserInfoVO;
import com.insdiide.ibip.domain.prompt.vo.PromptVO;
import com.insdiide.ibip.domain.prompt.vo2.PromptDataVO;
import com.insdiide.ibip.domain.report.vo.ReportVO;
import com.insdiide.ibip.global.mstr.prompt.ConstantPrompt;
import com.insdiide.ibip.global.mstr.prompt.ElementPrompt;
import com.insdiide.ibip.global.mstr.prompt.ObjectPrompt;
import com.microstrategy.web.objects.*;
import com.microstrategy.web.objects.admin.users.WebUser;
import com.microstrategy.webapi.EnumDSSXMLObjectTypes;
import com.microstrategy.webapi.EnumDSSXMLPrivilegeTypes;
import com.microstrategy.webapi.EnumDSSXMLStatus;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
    public FolderVO getfolderInfo(String folderId) throws WebObjectsException {
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
    public List<EntityVO> getSubList(String folderId, String parentId) throws WebObjectsException {
        List<EntityVO> subList = new ArrayList<>();
        WebFolder folder = (WebFolder) factory.getObjectSource().getObject(folderId, EnumDSSXMLObjectTypes.DssXmlTypeFolder);
        String currentParentId = parentId.equals("") ? "#" : parentId;
        folder.populate();
        if(currentParentId == "#"){
            subList.add(new EntityVO(folder.getID(), folder.getName(), currentParentId, folder.getType()));
        }

        for(int i=0; i<folder.getChildCount(); i++){
                subList.add(new EntityVO(folder.get(i).getID(), folder.get(i).getName(), folder.get(i).getParent().getID(), folder.get(i).getType()));

                if(folder.get(i).getType() == 8){
                    folder.get(i).populate();
                    if(folder.get(i).getChildUnits().size() > 0){
                        List<EntityVO> childSubList = getSubList(folder.get(i).getID(), folder.get(i).getParent().getID());
                        subList.addAll(childSubList);
                    }
                }
        }
        System.out.println(subList);
        return subList;
    }

    public String getUsrSmgr(){
        String usrSmgr = serverSession.saveState(0);
        return usrSmgr;
    }

    //리포트 정보 가져오기 (리포트 정보만)
    public ReportVO getReportInfo(String reportId) throws WebObjectsException {
        ReportVO reportInfo = new ReportVO();
        reportInfo.setReportId(reportId);

        WebObjectSource objectSource = this.serverSession.getFactory().getObjectSource();
//        WebObjectInfo report = objectSource.getObject(reportId, EnumDSSXMLObjectTypes.DssXmlTypeReportDefinition);

        //다큐먼트 일 경우의 처리 따로 해주기
        WebObjectInfo report = objectSource.getObject(reportId, EnumDSSXMLObjectTypes.DssXmlTypeDocumentDefinition);

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

        System.out.println("아오 졸려");
        System.out.println(reportPath);

        return reportInfo;
    }



    // 리포트 데이터 정보 가져오기 (프롬프트 데이터 포함) (유형에 맞게 나눠서)
    public ReportVO getReportDataInfo(String reportId, ReportVO reportInfo) throws WebObjectsException {

        //프롬프트들의 인스턴스 만들기
//        WebReportInstance webReportInstance = serverSession.getFactory().getReportSource().getNewInstance(reportId);
        /**
         * 다큐먼트 일 경우의 인스턴스 만들기
         *
         * **/
        WebDocumentInstance webReportInstance = serverSession.getFactory().getDocumentSource().getNewInstance(reportId);


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
             * TYPE = 3 계층 프롬프트
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


}
