package com.insdiide.ibip.domain.prompt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PromptVO {


    private List<PromptDataVO> data; //프롬포트 데이터 리스트

    private String title; //프롬프트 설명

    private String promptName; //프롬프트 명

    private String promptType; //프롬프트 타입

    private String promptId; //프롬프트 ID

    private String minValue; //최소 허용 값

    private String maxValue; //최대 허용 값

    /**
     * 구성요소 프롬프트의 데이터들
     * **/
    private String attrId; //애트리뷰트 ID

}
