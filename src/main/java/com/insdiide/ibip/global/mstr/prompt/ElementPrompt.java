package com.insdiide.ibip.global.mstr.prompt;

import com.insdiide.ibip.domain.prompt.vo.PromptDataVO;
import com.insdiide.ibip.domain.prompt.vo.PromptVO;
import com.insdiide.ibip.domain.prompt.vo2.AttributeVO;
import com.insdiide.ibip.domain.prompt.vo2.ElementVO;
import com.microstrategy.web.objects.*;

import java.util.ArrayList;
import java.util.List;

public class ElementPrompt {

    public PromptVO getElementPromptInfo(PromptVO prompt, WebPrompt webPrompt) throws WebObjectsException {

        // 구성요소 프롬프트 객체 생성
        WebElementsPrompt elementsPrompt = (WebElementsPrompt) webPrompt;
        prompt.setPromptId(elementsPrompt.getID());
        prompt.setPromptNm(elementsPrompt.getMeaning());
        prompt.setTitle(elementsPrompt.getTitle());
        prompt.setMinValue(elementsPrompt.getMin());
        prompt.setMaxValue(elementsPrompt.getMax());
        prompt.setPromptType("element");
        prompt.setPt(String.valueOf(elementsPrompt.getDSSPromptType()));
        prompt.setTagType(elementsPrompt.getDescription());

        // 프롬프트 채우기
        elementsPrompt.populate();

        // 애트리뷰트 객체 생성
        WebAttribute attr = elementsPrompt.getOrigin();
        AttributeVO attrInfo = new AttributeVO();

        // 애트리뷰트 정보 파싱
        attrInfo.setAttrId(attr.getID());
        attrInfo.setAttrNm(attr.getName());
        attrInfo.setAttrType(attr.getType());

        System.out.println("현재 기본 세팅된 PromptAnswerXML: " + elementsPrompt.getShortAnswerXML(true));


        // 애트리뷰트의 요소 리스트 객체 생성
        WebElements promptElements = elementsPrompt.getOrigin().getElementSource().getElements();
        List<ElementVO> elements = new ArrayList<>();

        // 요소 수만큼 반복
        for (int i=0; i<promptElements.size(); i++){
            WebElement promptElement = promptElements.get(i);
            ElementVO element = new ElementVO();
            element.setElementId(promptElement.getElementID());
            element.setElementNm(promptElement.getDisplayName());
            elements.add(element);
        }

        //기본 값 세팅
        if(elementsPrompt.hasDefaultAnswer()){ //답이 있을 경우
            WebElements defaultAnswer = elementsPrompt.getDefaultAnswer(); //기본값 요소 리스트 객체 생성
            for(int j=0; j<defaultAnswer.size(); j++){
                for(int k=0; k<elements.size(); k++){
                    if(defaultAnswer.get(j).getElementID().equals(elements.get(k).getElementId())){ //기본값 요소의 ID를 비교하여 같으면 기본값 적용
                        elements.get(k).setDefaultAnswerYn("Y");
                    }
                }
            }
        }

        // 애트리뷰트 객체에 저장
        attrInfo.setElements(elements);
        prompt.setAttr(attrInfo);

        return prompt;
    }

}
