package com.insdiide.ibip.domain.template.service;

import com.insdiide.ibip.domain.prompt.vo.ObjectVO;
import com.insdiide.ibip.domain.report.vo.ReportVO;
import com.insdiide.ibip.domain.template.ReqTemplateVO;
import com.insdiide.ibip.domain.template.TemplateVO;
import com.insdiide.ibip.domain.template.mapper.TemplateMapper;
import com.insdiide.ibip.global.exception.CustomException;
import com.insdiide.ibip.global.exception.code.ResultCode;
import com.insdiide.ibip.global.vo.ResVO;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.util.CustomObjectInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.insdiide.ibip.global.utils.ComUtils.isNullOrEmpty;

@Log4j2
@Service
public class TemplateService {
    @Autowired
    private TemplateMapper templateMapper;

    public ResVO checkTemplate(String templateName){
        if(isNullOrEmpty(templateName)){
            //없을 때
            log.info("템플릿명을 입력하지 않았습니다.");
            throw new CustomException(ResultCode.NO_TEMPLATE_NAME);
        }
        else{
            //있을 때
            int templateCnt = templateMapper.checkTemplate(templateName);
            System.out.println(templateCnt);
            if(templateCnt > 0) {
                throw new CustomException(ResultCode.EXISTS_TEMPLATE_NAME);
            }
        }
        return new ResVO(ResultCode.SUCCESS);
    }

    @Transactional
    public void createTemplate(ReqTemplateVO templateInfo){
        ResVO response = this.checkTemplate(templateInfo.getTemplateName());
        if("S00".equals(response.getCode())){
            if(templateInfo.getRemark().length() > 200){
                throw new CustomException(ResultCode.INVALID_REMARK);
            }
            else{
                TemplateVO template = TemplateVO
                        .builder()
                        .reportId(templateInfo.getReportInfo().getReportId())
                        .docType(templateInfo.getReportInfo().getDocumentType())
                        .userId(templateInfo.getUserInfo().getUserId())
                        .userNm(templateInfo.getUserInfo().getUserName())
                        .tmpNm(templateInfo.getTemplateName())
                        .remark(templateInfo.getRemark())
                        .build();

                int templateCount = templateMapper.insertTemplate(template);
                System.out.println(templateCount);
                if(templateCount > 0){
                    for(int i =0; i<templateInfo.getReportInfo().getPrompts().size(); i++){
                        templateInfo.getReportInfo().getPrompts().get(i).setTmpId(template.getTmpId());
                    }
                    int promptCount = templateMapper.insertPrompts(templateInfo.getReportInfo().getPrompts());
                    System.out.println(promptCount);
                    if(promptCount == templateInfo.getReportInfo().getPrompts().size()){
                        int entityCount =0;
                        for(int j=0; j<templateInfo.getReportInfo().getPrompts().size(); j++){
                            for(int k=0; k<templateInfo.getReportInfo().getPrompts().get(j).getEntity().size(); k++){
                                templateInfo.getReportInfo().getPrompts().get(j).getEntity().get(k).setTmpId(template.getTmpId());
                            }
                            entityCount += templateMapper.insertEntity(templateInfo.getReportInfo().getPrompts().get(j).getEntity());
                        }
                        System.out.println(entityCount);
                    }
                }
            }
        }
    }

    public List<TemplateVO> selectTemplate(String reportId, String userId){
        System.out.println(reportId);
        System.out.println(userId);
        List<TemplateVO> templates = templateMapper.selectTemplate(reportId, userId);
        System.out.println(templates);
        return templates;
    }

    public List<ObjectVO> getTemplate(String templateId){
        System.out.println(templateId);
        System.out.println("왔?");
        List<ObjectVO> entities = templateMapper.getTemplate(templateId);
        System.out.println(entities);
        return entities;
    }
}
