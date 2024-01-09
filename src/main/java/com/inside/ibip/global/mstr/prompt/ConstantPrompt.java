package com.inside.ibip.global.mstr.prompt;

import com.inside.ibip.domain.guest.prompt.vo.PromptVO;
import com.microstrategy.web.objects.WebConstantPrompt;
import com.microstrategy.web.objects.WebObjectsException;
import com.microstrategy.web.objects.WebPrompt;

public class ConstantPrompt {

    public PromptVO getConstantPromptInfo(PromptVO prompt, WebPrompt webPrompt) throws WebObjectsException {

        // 값 프롬프트 객체 생성
        WebConstantPrompt constantPrompt = (WebConstantPrompt) webPrompt;
        prompt.setPromptId(constantPrompt.getID());
        prompt.setPromptNm(constantPrompt.getMeaning());
        prompt.setTitle(constantPrompt.getTitle());
        prompt.setMinValue(constantPrompt.getMin());
        prompt.setMaxValue(constantPrompt.getMax());
        prompt.setPromptType("value");
        prompt.setPt(String.valueOf(constantPrompt.getDSSPromptType()));

        // 프롬프트 채우기
        constantPrompt.populate();

        System.out.println(constantPrompt.getID());
        System.out.println(constantPrompt.getMeaning());
        System.out.println(constantPrompt.getTitle());
        System.out.println(constantPrompt.getMin());
        System.out.println(constantPrompt.getMax());
        System.out.println(constantPrompt.getDefaultAnswer());
        System.out.println(constantPrompt.getAnswer());
        System.out.println(constantPrompt.getShortAnswerXML(true));
        System.out.println(constantPrompt.getPromptType());
        System.out.println(constantPrompt.getDataType());
        System.out.println(constantPrompt.getDSSPromptType());

        // 기본 값 정보 파싱
        if(constantPrompt.hasDefaultAnswer()){
            prompt.setVal(constantPrompt.getDefaultAnswer());
        }

        return prompt;
    }
}
