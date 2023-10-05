package com.insdiide.ibip.global.utils;

import com.insdiide.ibip.domain.prompt.vo2.ElementVO;
import com.insdiide.ibip.domain.report.vo.ReportVO;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import java.io.StringWriter;

public class UrlUtils {

    public static String makeXML(ReportVO reportInfo){

        return "";
    }

    public static String generateXML(ReportVO reportVO) {
        StringWriter sw = new StringWriter();
        try {
            XMLOutputFactory xof = XMLOutputFactory.newFactory();
            XMLStreamWriter xsw = xof.createXMLStreamWriter(sw);

            // xsw.writeStartDocument("1.0"); // 이 부분을 주석 처리하여 선언을 제외
            xsw.writeStartElement("rsl");

            xsw.writeStartElement("pa");
            xsw.writeAttribute("pt", "7");
            xsw.writeAttribute("pin", "0");
            xsw.writeAttribute("did", reportVO.getPrompts().get(0).getPromptId());
            xsw.writeAttribute("tp", "10");

            xsw.writeStartElement("mi");

            xsw.writeStartElement("es");


            xsw.writeStartElement("at");
            xsw.writeAttribute("did", reportVO.getPrompts().get(0).getData().get(0).getAttr().getAttrId());
            xsw.writeAttribute("tp", "12");
            xsw.writeEndElement(); // at

            for (ElementVO element : reportVO.getPrompts().get(0).getData().get(0).getAttr().getElements()) {
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

            xsw.writeEndElement(); // rsl

            xsw.flush();
            xsw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sw.toString();
    }

    public static String getReportURL(ReportVO reportInfo, String promptXML, String usrSmgr){

        // Return session
        StringBuilder urlSB = new StringBuilder();
        urlSB.append("http").append("://").append("192.168.70.245:8090"); //Web Server name and port
        urlSB.append("/MicroStrategy/servlet/mstrWeb");
        urlSB.append("?server=").append("192.168.70.245"); //I Server name
        // urlSB.append("&port=0");
        urlSB.append("&project=").append("MicroStrategy+Tutorial"); // Project name
        urlSB.append("&evt=").append(4001);
        urlSB.append("&reportID=").append(reportInfo.getReportId()); //Report ID
        urlSB.append("&currentViewMedia=").append(1);
        urlSB.append("&src=mstrWeb").append("NoHeaderNoFooterNoPath.").append(4001);
        urlSB.append("&usrSmgr=").append(usrSmgr);
        urlSB.append("&promptsAnswerXML=").append(promptXML);

        //        urlSB.append("&hiddensections=dockTop,dockLeft"); 조회 모드일 경우에 해당 옵션 추가

        System.out.println(urlSB.toString());

        return urlSB.toString();
    }
}
