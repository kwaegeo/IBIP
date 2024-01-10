package com.inside.ibip.global.mstr.prompt;

import com.inside.ibip.domain.guest.prompt.vo.ObjectVO;
import com.inside.ibip.domain.guest.prompt.vo.PromptVO;
import com.inside.ibip.global.exception.CustomException;
import com.inside.ibip.global.exception.code.ResultCode;
import com.microstrategy.web.objects.*;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;

/**
 * @FileName     : ObjectPrompt.java
 * @Date         : 2023.12.01
 * @Author       : 이도현
 * @Description  : 개체 프롬프트 파싱 클래스
 * @History
 * =======================================================
 *   DATE			AUTHOR			NOTE
 * =======================================================
 *   2023.12.01     이도현         최초작성
 *
 */
@Log4j2
public class ObjectPrompt {

    /**
     * 개체 프롬프트 파싱 함수
     * @Method Name   : getObjectPromptInfo
     * @Date / Author : 2023.12.01  이도현
     * @param prompt 프롬프트 VO 객체
     * @param webPrompt WebPrompt 객체
     * @return 개체 프롬프트 객체
     * @History
     * 2023.12.01	최초생성
     */
    public PromptVO getObjectPromptInfo(PromptVO prompt, WebPrompt webPrompt){

        //1. 개체 프롬프트 객체 생성
        WebObjectsPrompt objectPrompt = (WebObjectsPrompt) webPrompt;
        prompt.setPromptId(objectPrompt.getID());
        prompt.setPromptNm(objectPrompt.getMeaning());
        prompt.setTitle(objectPrompt.getTitle());
        prompt.setMinValue(objectPrompt.getMin());
        prompt.setMaxValue(objectPrompt.getMax());
        prompt.setPromptType("object");
        prompt.setPt(String.valueOf(objectPrompt.getDSSPromptType()));
        prompt.setTagType(objectPrompt.getDescription());

        //1-1.프롬프트 채우기
        try {
            objectPrompt.populate();
        }catch (WebObjectsException woe){
            log.error("개체 프롬프트 불러오는 도중 오류 발생 [Error msg]: "+ woe.getMessage());
            throw new CustomException(ResultCode.INVALID_PROMPT);
        }

        //2. 개체 리스트 객체 생성
        WebFolder webFolder = objectPrompt.getSuggestedAnswers(true);
        List<ObjectVO> entities = new ArrayList<>();

        //2-1. 개체 수만큼 반복하여 파싱
        for(int i=0; i<webFolder.size(); i++){
            WebObjectInfo webObjectInfo = webFolder.get(i);
            ObjectVO entity = new ObjectVO();
            entity.setEntityId(webObjectInfo.getID());
            entity.setEntityNm(webObjectInfo.getDisplayName());
            entity.setEntityDesc(webObjectInfo.getDescription()); //있으면 표시
            entity.setEntityType(webObjectInfo.getType());
            entities.add(entity);
        }

        //3. 개체 프롬프트의 기본 값 세팅
        if(objectPrompt.hasDefaultAnswer()){
            WebFolder defaultAnswer = objectPrompt.getDefaultAnswer(); //기본값 개체 리스트 객체 생성
            for(int j=0; j<defaultAnswer.size(); j++){
                for(int k=0; k<entities.size(); k++){
                    if(defaultAnswer.get(j).getID().equals(entities.get(k).getEntityId())) { //기본값 개체의 ID를 비교하여 같으면 기본값 적용
                        entities.get(k).setDefaultAnswerYn("Y");
                    }
                }
            }
        }

        //4. 프롬프트 객체에 개체 저장 후 응답
        prompt.setEntity(entities);
        return prompt;
    }

}

