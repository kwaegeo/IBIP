package com.inside.ibip.domain.guest.template.service;

import com.inside.ibip.domain.guest.prompt.vo.ObjectVO;
import com.inside.ibip.domain.guest.template.ReqTemplateVO;
import com.inside.ibip.domain.guest.template.TemplateVO;
import com.inside.ibip.domain.guest.template.mapper.TemplateMapper;
import com.inside.ibip.global.exception.CustomException;
import com.inside.ibip.global.exception.code.ResultCode;
import com.inside.ibip.global.vo.ResVO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.inside.ibip.global.utils.ComUtils.isNullOrEmpty;

/**
 * @FileName     : TemplateService.java
 * @Date         : 2023.12.01
 * @Author       : 이도현
 * @Description  : 템플릿 Service, 템플릿 중복확인, 생성, 삭제
 * @History
 * =======================================================
 *   DATE			AUTHOR			NOTE
 * =======================================================
 *   2023.12.01     이도현         최초작성
 *
 */
@Log4j2
@Service
public class TemplateService {
    @Autowired
    private TemplateMapper templateMapper;

    /**
     * 템플릿 명 중복조회
     * @Method Name   : checkTemplate
     * @Date / Author : 2023.12.01  이도현
     * @param templateName 템플릿 명
     * @return 성공 유무 반환
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    public ResVO checkTemplate(String templateName){

        //1. Null 체크
        if(isNullOrEmpty(templateName)){
            log.info("템플릿명을 입력하지 않았습니다.");
            throw new CustomException(ResultCode.NO_TEMPLATE_NAME);
        }
        else{

            //2. DB를 통한 해당 템플릿명이 있는지 조회
            int templateCnt = 0;
            try {
                 templateCnt = templateMapper.checkTemplate(templateName);
            }catch (Exception e){
                log.error("템플릿 중복 조회 중 에러 발생 [Error msg]: " + e.getMessage());
                throw new CustomException(ResultCode.ETC_ERROR);
            }
            if(templateCnt > 0) {
                throw new CustomException(ResultCode.EXISTS_TEMPLATE_NAME);
            }
        }
        //3. 응답
        return new ResVO(ResultCode.SUCCESS);
    }

    /**
     * 템플릿 생성
     * @Method Name   : createTemplate
     * @Date / Author : 2023.12.01  이도현
     * @param templateInfo 템플릿 명
     * @return 성공 유무
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    @Transactional
    public void createTemplate(ReqTemplateVO templateInfo) {

        //1. 생성과정에서 1번 더 Check 함수 호출 (유효성 검사)
        ResVO result = this.checkTemplate(templateInfo.getTemplateName());
        if ("S00".equals(result.getCode())) {
            if (templateInfo.getRemark().length() > 200) {
                throw new CustomException(ResultCode.INVALID_REMARK);
            }

            //2. DB 저장을 위한 TemplateVO 객체 초기화
            else {
                try {
                    TemplateVO template = TemplateVO
                            .builder()
                            .reportId(templateInfo.getReportInfo().getReportId())
                            .docType(templateInfo.getReportInfo().getDocumentType())
                            .userId(templateInfo.getUserInfo().getUserId())
                            .userNm(templateInfo.getUserInfo().getUserName())
                            .tmpNm(templateInfo.getTemplateName())
                            .remark(templateInfo.getRemark())
                            .build();

                    //2-1. 템플릿 저장 (DB)
                    int templateCount = templateMapper.insertTemplate(template);

                    // 성공 시
                    if (templateCount > 0) {

                        //2-2. 템플릿의 프롬프트 정보를 파싱
                        for (int i = 0; i < templateInfo.getReportInfo().getPrompts().size(); i++) {
                            templateInfo.getReportInfo().getPrompts().get(i).setTmpId(template.getTmpId());
                        }

                        //2-3. 프롬프트 저장 (DB)
                        int promptCount = templateMapper.insertPrompts(templateInfo.getReportInfo().getPrompts());

                        //2-4. 프롬프트 저장 수와 리포트의 프롬프트 수가 같은지 확인
                        if (promptCount == templateInfo.getReportInfo().getPrompts().size()) {

                            int promptDataCount = 0;

                            //2-5. 프롬프트의 요소 (구성요소, 개체, 값) 나눠 저장
                            for (int j = 0; j < templateInfo.getReportInfo().getPrompts().size(); j++) {

                                //2-6 분기 처리 (값, 구성요소, 개체)

                                // 값 프롬프트
                                if("value".equals(templateInfo.getReportInfo().getPrompts().get(j).getPromptType())){
                                    templateInfo.getReportInfo().getPrompts().get(j).setTmpId(template.getTmpId());
                                    promptDataCount += templateMapper.insertValue(templateInfo.getReportInfo().getPrompts().get(j));

                                // 개체 프롬프트
                                }else if("object".equals(templateInfo.getReportInfo().getPrompts().get(j).getPromptType())){

                                    if(templateInfo.getReportInfo().getPrompts().get(j).getEntity().size() > 0 ) {
                                        for (int k = 0; k < templateInfo.getReportInfo().getPrompts().get(j).getEntity().size(); k++) {
                                            templateInfo.getReportInfo().getPrompts().get(j).getEntity().get(k).setTmpId(template.getTmpId());
                                        }
                                        promptDataCount += templateMapper.insertEntity(templateInfo.getReportInfo().getPrompts().get(j).getEntity());
                                    }

                                // 구성요소 프롬프트
                                }else if("element".equals(templateInfo.getReportInfo().getPrompts().get(j).getPromptType())){
                                    if(templateInfo.getReportInfo().getPrompts().get(j).getAttr().getElements().size()>0){
                                        for(int k=0; k < templateInfo.getReportInfo().getPrompts().get(j).getAttr().getElements().size(); k++){
                                            templateInfo.getReportInfo().getPrompts().get(j).getAttr().getElements().get(k).setTmpId(template.getTmpId());
                                            templateInfo.getReportInfo().getPrompts().get(j).getAttr().getElements().get(k).setPromptId(templateInfo.getReportInfo().getPrompts().get(j).getPromptId());
                                        }
                                        promptDataCount += templateMapper.insertElement(templateInfo.getReportInfo().getPrompts().get(j).getAttr().getElements());
                                    }
                                }


                            }
                        }
                    }
                } catch (Exception e) {
                    log.error("템플릿 생성 중 에러 발생 [Error msg]: " + e.getMessage());
                    throw new CustomException(ResultCode.ETC_ERROR);
                }
            }
        }
    }

    /**
     * 템플릿 리스트 조회
     * @Method Name   : getTemplateList
     * @Date / Author : 2023.12.01  이도현
     * @param reportId 리포트 Id
     * @param userId 사용자 Id
     * @return 템플릿 리스트
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    public List<TemplateVO> getTemplateList(String reportId, String userId){
        List<TemplateVO> templates = null;
        try {
           templates  = templateMapper.selectTemplate(reportId, userId);
        }catch (Exception e){
            log.error("템플릿 리스트 조회 중 에러 발생 [Error msg]: " + e.getMessage());
            throw new CustomException(ResultCode.ETC_ERROR);
        }
        return templates;
    }

    /**
     * 템플릿 정보 조회
     * @Method Name   : getTemplate
     * @Date / Author : 2023.12.01  이도현
     * @param templateId 템플릿 Id
     * @return 템플릿의 구성 정보
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    public List<ObjectVO> getTemplate(String templateId){

        List<ObjectVO> entities = null;
        try {
            entities = templateMapper.getTemplate(templateId);
        }catch (Exception e){
            log.error("템플릿 정보 조회 중 에러 발생 [Error msg]: " + e.getMessage());
            throw new CustomException(ResultCode.ETC_ERROR);
        }

        return entities;
    }
}
