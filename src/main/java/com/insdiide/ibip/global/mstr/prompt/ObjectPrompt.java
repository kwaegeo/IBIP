package com.insdiide.ibip.global.mstr.prompt;

import com.insdiide.ibip.domain.prompt.vo.PromptVO;
import com.microstrategy.web.objects.*;

public class ObjectPrompt {

    public PromptVO getObjectPromptInfo(PromptVO prompt, WebPrompt webPrompt) throws WebObjectsException {

        // 개체 프롬프트 객체 생성
        WebObjectsPrompt objectPrompt = (WebObjectsPrompt) webPrompt;
        prompt.setPromptId(objectPrompt.getID());
        prompt.setPromptName(objectPrompt.getMeaning());
        prompt.setTitle(objectPrompt.getTitle());
        prompt.setMinValue(objectPrompt.getMin());
        prompt.setMaxValue(objectPrompt.getMax());
        prompt.setPromptType("object");
        prompt.setPt(String.valueOf(objectPrompt.getDSSPromptType()));

        //프롬프트 채우기
        objectPrompt.populate();

        //오브젝트 객체 생성;
        System.out.println(objectPrompt.getMeaning());
        System.out.println(objectPrompt.getTitle());


        return new PromptVO();
    }

}
