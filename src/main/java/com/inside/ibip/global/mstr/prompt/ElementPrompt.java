package com.inside.ibip.global.mstr.prompt;

import com.inside.ibip.domain.guest.prompt.vo.PromptVO;
import com.inside.ibip.domain.guest.prompt.vo.AttributeVO;
import com.inside.ibip.domain.guest.prompt.vo.ElementVO;
import com.inside.ibip.global.exception.CustomException;
import com.inside.ibip.global.exception.code.ResultCode;
import com.microstrategy.web.objects.*;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;

/**
 * @FileName     : ElementPrompt.java
 * @Date         : 2023.12.01
 * @Author       : 이도현
 * @Description  : 구성요소 프롬프트 파싱 클래스
 * @History
 * =======================================================
 *   DATE			AUTHOR			NOTE
 * =======================================================
 *   2023.12.01     이도현         최초작성
 *
 */
@Log4j2
public class ElementPrompt {

    /**
     * 구성 프롬프트 파싱 함수
     * @Method Name   : getElementPromptInfo
     * @Date / Author : 2023.12.01  이도현
     * @param prompt 프롬프트 VO 객체
     * @param webPrompt WebPrompt 객체
     * @return 구성 프롬프트 객체
     * @History
     * 2023.12.01	최초생성
     */
    public PromptVO getElementPromptInfo(PromptVO prompt, WebPrompt webPrompt){

        //1. 구성요소 프롬프트 객체 생성
        WebElementsPrompt elementsPrompt = (WebElementsPrompt) webPrompt;
        prompt.setPromptId(elementsPrompt.getID());
        prompt.setPromptNm(elementsPrompt.getMeaning());
        prompt.setTitle(elementsPrompt.getTitle());
        prompt.setMinValue(elementsPrompt.getMin());
        prompt.setMaxValue(elementsPrompt.getMax());
        prompt.setPromptType("element");
        prompt.setPt(String.valueOf(elementsPrompt.getDSSPromptType()));
        prompt.setTagType(elementsPrompt.getDescription());

        //1-1. 프롬프트 채우기
        try {
            elementsPrompt.populate();
        }catch (WebObjectsException woe){
            log.error("구성요소 프롬프트 불러오는 도중 오류 발생 [Error msg]: "+ woe.getMessage());
            throw new CustomException(ResultCode.INVALID_PROMPT);
        }

        //2. 애트리뷰트 객체 생성
        WebAttribute attr = elementsPrompt.getOrigin();
        AttributeVO attrInfo = new AttributeVO();

        //2-1. 애트리뷰트 정보 파싱
        attrInfo.setAttrId(attr.getID());
        attrInfo.setAttrNm(attr.getName());
        attrInfo.setAttrType(attr.getType());

        //2-2. 애트리뷰트의 요소 리스트 객체 생성
        WebElements promptElements = null;
        try {
            promptElements = elementsPrompt.getOrigin().getElementSource().getElements();
        }catch (WebObjectsException woe){
            log.error("구성요소 프롬프트의 요소 파싱 중 오류 발생 [Error msg]: "+ woe.getMessage());
            throw new CustomException(ResultCode.INVALID_PROMPT);
        }

        //2-3. 구성요소 리스트 객체 생성
        List<ElementVO> elements = new ArrayList<>();

        //2-4. 애트리뷰트의 구성요소 수만큼 반복하여 파싱
        for (int i=0; i<promptElements.size(); i++){
            WebElement promptElement = promptElements.get(i);
            ElementVO element = new ElementVO();
            element.setElementId(promptElement.getElementID());
            element.setElementNm(promptElement.getDisplayName());
            elements.add(element);
        }

        //3. 구성요소 프롬프트의 기본 값 세팅
        if(elementsPrompt.hasDefaultAnswer()){
            WebElements defaultAnswer = elementsPrompt.getDefaultAnswer(); //기본값 요소 리스트 객체 생성
            for(int j=0; j<defaultAnswer.size(); j++){
                for(int k=0; k<elements.size(); k++){
                    if(defaultAnswer.get(j).getElementID().equals(elements.get(k).getElementId())){ //기본값 요소의 ID를 비교하여 같으면 기본값 적용
                        elements.get(k).setDefaultAnswerYn("Y");
                    }
                }
            }
        }

        //4. 애트리뷰트 객체에 저장 후 응답
        attrInfo.setElements(elements);
        prompt.setAttr(attrInfo);

        return prompt;
    }

}
