package com.insdiide.ibip.domain.template.service;

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

                int resultCount = templateMapper.insertTemplate(template);
                System.out.println(resultCount);
            }
        }
    }

}
