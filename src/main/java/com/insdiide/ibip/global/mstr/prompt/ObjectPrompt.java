package com.insdiide.ibip.global.mstr.prompt;

import com.insdiide.ibip.domain.folder.vo.EntityVO;
import com.insdiide.ibip.domain.prompt.vo.ObjectVO;
import com.insdiide.ibip.domain.prompt.vo.PromptDataVO;
import com.insdiide.ibip.domain.prompt.vo.PromptVO;
import com.microstrategy.web.objects.*;

import java.util.ArrayList;
import java.util.List;

public class ObjectPrompt {

    public PromptVO getObjectPromptInfo(PromptVO prompt, WebPrompt webPrompt) throws WebObjectsException {

        // 개체 프롬프트 객체 생성
        WebObjectsPrompt objectPrompt = (WebObjectsPrompt) webPrompt;
        prompt.setPromptId(objectPrompt.getID());
        prompt.setPromptNm(objectPrompt.getMeaning());
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
        System.out.println("현재 기본 세팅된 PromptAnswerXML: " +objectPrompt.getShortAnswerXML(true));

        // 개체 리스트 객체 생성
        WebFolder webFolder = objectPrompt.getSuggestedAnswers(true);
        List<ObjectVO> entities = new ArrayList<>();

        // 개체 수만큼 반복
        for(int i=0; i<webFolder.size(); i++){
            WebObjectInfo webObjectInfo = webFolder.get(i);
            ObjectVO entity = new ObjectVO();
            entity.setEntityId(webObjectInfo.getID());
            entity.setEntityNm(webObjectInfo.getDisplayName());
            entity.setEntityDesc(webObjectInfo.getDescription()); //있으면 표시
            entity.setEntityType(webObjectInfo.getType());
            entities.add(entity);

            System.out.println("==개체 형태는 다음과 같음==");
            System.out.println(webObjectInfo.getID());
            System.out.println(webObjectInfo.getDisplayName());
            System.out.println(webObjectInfo.getDescription());
            System.out.println(webObjectInfo.getType());
        }

        if(objectPrompt.hasDefaultAnswer()){ //답이 있을 경우
            WebFolder defaultAnswer = objectPrompt.getDefaultAnswer(); //기본값 개체 리스트 객체 생성
            for(int j=0; j<defaultAnswer.size(); j++){
                for(int k=0; k<entities.size(); k++){
                    if(defaultAnswer.get(j).getID().equals(entities.get(k).getEntityId())) { //기본값 개체의 ID를 비교하여 같으면 기본값 적용
                        entities.get(k).setDefaultAnswerYn("Y");
                    }
                }
            }
        }

        //프롬프트 객체에 저장
        prompt.setEntity(entities);
        return prompt;
    }

}

