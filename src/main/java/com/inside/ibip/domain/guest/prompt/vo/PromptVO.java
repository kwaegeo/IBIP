package com.inside.ibip.domain.guest.prompt.vo;

import com.inside.ibip.domain.guest.prompt.vo2.AttributeVO;
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

    private String title; //프롬프트 설명

    private String promptId; //프롬프트 ID

    private String promptNm; //프롬프트 명

    private String promptDesc; //프롬프트 설명 (ck, rd, sl) 이 값을 통해 표현되는 방식이 달라짐

    private String promptType; //프롬프트 타입

    private String minValue; //최소 허용 값

    private String maxValue; //최대 허용 값

    private String pt; // DSSPromptType (XML에 들어가는 Type)

    private String tagType; // 프롬프트 설명에 들어간 InputTag의 종류 (타입)

    private String tmpId; // 템플릿 ID (DB 용)

    private String attrId; // 애트리뷰트 혹은 ID (DB용)

    /**
     * 구성요소 프롬프트 전용 (애트리뷰트 정보)
     * **/

    private AttributeVO attr; // 구성요소 프롬프트 전용 (애트리뷰트)

    /**
     * 값 프롬프트 전용 (값 정보)
     * **/
    private String val; // 값 프롬프트 전용 (데이터)

    /**
     * 개체 프롬프트 전용 (개체 정보)
     * **/
    private List<ObjectVO> entity; // 개체 프롬프트 전용 (개체 정보)
}
