package com.inside.ibip.global.utils;

import com.inside.ibip.domain.guest.prompt.vo.ObjectVO;
import com.inside.ibip.domain.guest.prompt.vo.ElementVO;
import com.inside.ibip.domain.guest.report.vo.ReportVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import java.io.StringWriter;

/**
 * @FileName     : UrlUtils.java
 * @Date         : 2023.12.01
 * @Author       : 이도현
 * @Description  : PromptXML 조합 및 생성, ReportURL 조합 및 생성
 * @History
 * =======================================================
 *   DATE			AUTHOR			NOTE
 * =======================================================
 *   2023.12.01     이도현         최초작성
 *
 */
@Component
public class UrlUtils {

    @Value("${mstrServerName}")
    private String serverName;

    @Value("${mstrProjectName}")
    private String mstrProjectName;

    @Value("${mstrWebPort}")
    private String mstrWebPort;

    /**
     * 리포트의 PromptXML을 조합하고 생성하는 함수
     * @Method Name   : generatePromptXML
     * @Date / Author : 2023.12.01  이도현
     * @param reportInfo 리포트 정보 및 프롬프트 정보
     * @History
     * 2023.12.01	최초생성
     */
    public String generatePromptXML(ReportVO reportInfo) {

        StringWriter sw = new StringWriter();
        String middleTag = "";
        try {
            XMLOutputFactory xof = XMLOutputFactory.newFactory();
            XMLStreamWriter xsw = xof.createXMLStreamWriter(sw);

            xsw.writeStartElement("rsl");

            for(int i=0; i< reportInfo.getPrompts().size(); i++){
                xsw.writeStartElement("pa");
                xsw.writeAttribute("pt", reportInfo.getPrompts().get(i).getPt());
                xsw.writeAttribute("pin", "0");
                xsw.writeAttribute("did", reportInfo.getPrompts().get(i).getPromptId());
                xsw.writeAttribute("tp", "10");

                if("value".equals(reportInfo.getPrompts().get(i).getPromptType())){
                    xsw.writeCharacters(reportInfo.getPrompts().get(i).getVal());
                    xsw.writeEndElement(); // pa
                }

                else if("element".equals(reportInfo.getPrompts().get(i).getPromptType())){
                    xsw.writeStartElement("mi");
                    xsw.writeStartElement("es");
                    xsw.writeStartElement("at");

                    xsw.writeAttribute("did", reportInfo.getPrompts().get(i).getAttr().getAttrId());
                    xsw.writeAttribute("tp", "12");
                    xsw.writeEndElement(); // at
                    for (ElementVO element : reportInfo.getPrompts().get(i).getAttr().getElements()) {
                        xsw.writeStartElement("e");
                        xsw.writeAttribute("emt", "1");
                        xsw.writeAttribute("ei", element.getElementId());
                        xsw.writeAttribute("art", "1");
                        xsw.writeAttribute("disp_n", element.getElementNm());
                        xsw.writeEndElement(); // e
                    }

                    xsw.writeEndElement(); // es

                    xsw.writeEndElement(); // mi

                    xsw.writeEndElement(); // pa
                }


                else if("object".equals(reportInfo.getPrompts().get(i).getPromptType())){
                    xsw.writeStartElement("mi");
                    xsw.writeStartElement("fct");
                    xsw.writeAttribute("qsr", "0");
                    xsw.writeAttribute("fcn", "0");
                    xsw.writeAttribute("sto", "1");
                    xsw.writeAttribute("pfc", "0");
                    for(ObjectVO entity : reportInfo.getPrompts().get(i).getEntity()){
                        if(entity.getEntityType() == 12){
                            middleTag = "at";
                        }else{
                            middleTag = "mt";
                        }
                        xsw.writeStartElement(middleTag);
                        xsw.writeAttribute("did", entity.getEntityId());
                        xsw.writeAttribute("tp", String.valueOf(entity.getEntityType()));
                        xsw.writeEndElement();
                    }

                    xsw.writeEndElement(); // fct

                    xsw.writeEndElement(); // mi

                    xsw.writeEndElement(); // pa
                }

            }
            xsw.writeEndElement(); // rsl
            xsw.flush();
            xsw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sw.toString();
    }

    /**
     * 리포트의 최종 URL 조회 하는 함수 (iframe src)
     * @Method Name   : getReportURL
     * @Date / Author : 2023.12.01  이도현
     * @param reportInfo 리포트 정보
     * @param promptXml PromptXML
     * @param usrSmgr 사용자 세션 정보
     * @return 리포트 URL (String)
     * @History
     * 2023.12.01	최초생성
     */
    public String getReportURL(ReportVO reportInfo, String promptXML, String usrSmgr){

        /** 이벤트 타입에 따른 옵션 설정 **/

        int evtType = 4001; // 리포트 단순 조회
        String idType = "reportID";
        if("D".equals(reportInfo.getDocumentType())){
            evtType = 2048001; // 다큐먼트 단순 조회
            idType = "documentID";
        }

        if("P".equals(reportInfo.getExportType())){
            evtType = 3062; // PDF 내보내기
        }else if("E".equals(reportInfo.getExportType())){
            evtType = 3067; // Excel 내보내기
        }

        // Return session
        StringBuilder urlSB = new StringBuilder();
        urlSB.append("http").append("://").append(serverName).append(":").append(mstrWebPort); //Web Server name and port
        urlSB.append("/MicroStrategy/servlet/mstrWeb");
        urlSB.append("?server=").append(serverName); //I Server name
         urlSB.append("&port=0");
        urlSB.append("&project=").append(mstrProjectName); // Project name
        urlSB.append("&evt=").append(evtType);
        urlSB.append("&"+idType+"=").append(reportInfo.getReportId()); //Report ID
        urlSB.append("&currentViewMedia=").append(1);
        urlSB.append("&src=mstrWeb").append("NoHeaderNoFooterNoPath.").append(evtType);
        urlSB.append("&usrSmgr=").append(usrSmgr);
        urlSB.append("&promptsAnswerXML=").append(promptXML);
        urlSB.append("&hiddensections=path");
        if("N".equals(reportInfo.getEditYn())){
            //조회 모드일 경우에 해당 옵션 추가
            urlSB.append(",dockTop,dockLeft");
        }

        System.out.println(urlSB.toString());

        return urlSB.toString();
    }

    /**
     * 사용내역 목록의 URL 조회 (로그인 계정)
     * @Method Name   : getHistoryURL
     * @Date / Author : 2023.12.01  이도현
     * @return 사용자 정보
     * @History
     * 2023.12.01	최초생성
     */
    public String getHistoryURL(String usrSmgr){

        StringBuilder urlSB = new StringBuilder();
        urlSB.append("http").append("://").append(serverName).append(":").append(mstrWebPort); //Web Server name and port
        urlSB.append("/MicroStrategy/servlet/mstrWeb");
        urlSB.append("?server=").append(serverName); //I Server name
        urlSB.append("&port=0");
        urlSB.append("&project=").append(mstrProjectName); // Project name
        urlSB.append("&evt=").append("3018");
        urlSB.append("&src=mstrWeb").append("NoHeaderNoFooterNoPath.").append("3018");
        urlSB.append("&usrSmgr=").append(usrSmgr);
        urlSB.append("&hiddensections=path,dockTop,dockLeft");

        System.out.println(urlSB.toString());

        return urlSB.toString();
    }

    /**
     * 내 구독물 URL 조회 (로그인 계정)
     * @Method Name   : getSubscriptionURL
     * @Date / Author : 2023.12.01  이도현
     * @return 사용자 정보
     * @History
     * 2023.12.01	최초생성
     */
    public String getSubscriptionURL(String usrSmgr){

        // Return session
        StringBuilder urlSB = new StringBuilder();
        urlSB.append("http").append("://").append(serverName).append(":").append(mstrWebPort); //Web Server name and port
        urlSB.append("/MicroStrategy/servlet/mstrWeb");
        urlSB.append("?server=").append(serverName); //I Server name
        urlSB.append("&port=0");
        urlSB.append("&project=").append(mstrProjectName); // Project name
        urlSB.append("&evt=").append("3031");
        urlSB.append("&src=mstrWeb").append("NoHeaderNoFooterNoPath.").append("3031");
        urlSB.append("&usrSmgr=").append(usrSmgr);
        urlSB.append("&hiddensections=path,dockTop,dockLeft");

        System.out.println(urlSB.toString());

        return urlSB.toString();
    }


    /**
     * 대시보드 URL 조회 (로그인 계정)
     * @Method Name   : getDashboardURL
     * @Date / Author : 2023.12.01  이도현
     * @return 대시보드 URL
     * @History
     * 2023.12.01	최초생성
     */
    public String getDashboardURL(ReportVO reportInfo, String usrSmgr){

        int evtType = 2048001; // 리포트 단순 조회
        String idType = "documentID";

        // Return session
        StringBuilder urlSB = new StringBuilder();
        urlSB.append("http").append("://").append(serverName).append(":").append(mstrWebPort); //Web Server name and port
        urlSB.append("/MicroStrategy/servlet/mstrWeb");
        urlSB.append("?server=").append(serverName); //I Server name
        urlSB.append("&port=0");
        urlSB.append("&project=").append(mstrProjectName); // Project name
        urlSB.append("&evt=").append(evtType);
        urlSB.append("&"+idType+"=").append(reportInfo.getReportId()); //Report ID
        urlSB.append("&currentViewMedia=").append(1);
        urlSB.append("&src=mstrWeb").append("NoHeaderNoFooterNoPath.").append(evtType);
        urlSB.append("&usrSmgr=").append(usrSmgr);
        urlSB.append("&hiddensections=path");
        if("N".equals(reportInfo.getEditYn())){
            //조회 모드일 경우에 해당 옵션 추가
            urlSB.append(",dockTop,dockLeft");
        }

        System.out.println(urlSB.toString());

        return urlSB.toString();
    }
}
