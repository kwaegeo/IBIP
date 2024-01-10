package com.inside.ibip.global.mstr.prompt;

import com.inside.ibip.domain.guest.prompt.vo.PromptVO;
import com.inside.ibip.global.exception.CustomException;
import com.inside.ibip.global.exception.code.ResultCode;
import com.microstrategy.web.objects.WebConstantPrompt;
import com.microstrategy.web.objects.WebObjectsException;
import com.microstrategy.web.objects.WebPrompt;
import lombok.extern.log4j.Log4j2;

/**
 * @FileName     : ConstantPrompt.java
 * @Date         : 2023.12.01
 * @Author       : 이도현
 * @Description  : 값 프롬프트 파싱 클래스
 * @History
 * =======================================================
 *   DATE			AUTHOR			NOTE
 * =======================================================
 *   2023.12.01     이도현         최초작성
 *
 */
@Log4j2
public class ConstantPrompt {

    /**
     * 값 프롬프트 파싱 함수
     * @Method Name   : getConstantPromptInfo
     * @Date / Author : 2023.12.01  이도현
     * @param prompt 프롬프트 VO 객체
     * @param webPrompt WebPrompt 객체
     * @return 값 프롬프트 객체
     * @History
     * 2023.12.01	최초생성
     */
    public PromptVO getConstantPromptInfo(PromptVO prompt, WebPrompt webPrompt){

        //1. 값 프롬프트 객체 생성
        WebConstantPrompt constantPrompt = (WebConstantPrompt) webPrompt;
        prompt.setPromptId(constantPrompt.getID());
        prompt.setPromptNm(constantPrompt.getMeaning());
        prompt.setTitle(constantPrompt.getTitle());
        prompt.setMinValue(constantPrompt.getMin());
        prompt.setMaxValue(constantPrompt.getMax());
        prompt.setPromptType("value");
        prompt.setPt(String.valueOf(constantPrompt.getDSSPromptType()));

        //1-1. 프롬프트 채우기
        try {
            constantPrompt.populate();
        }catch (WebObjectsException woe){
            log.error("값 프롬프트 불러오는 도중 오류 발생 [Error msg]: "+ woe.getMessage());
            throw new CustomException(ResultCode.INVALID_PROMPT);
        }

        //2. 프롬프트의 기본 값 정보 파싱
        if(constantPrompt.hasDefaultAnswer()){
            prompt.setVal(constantPrompt.getDefaultAnswer());
        }

        return prompt;
    }
}
