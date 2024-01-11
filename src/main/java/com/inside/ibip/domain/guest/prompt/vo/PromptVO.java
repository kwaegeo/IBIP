package com.inside.ibip.domain.guest.prompt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.util.List;

/**
 * @FileName     : PromptVO.java
 * @Date         : 2023.12.01
 * @Author       : 이도현
 * @Description  : 프롬프트 VO
 * @History
 * =======================================================
 *   DATE			AUTHOR			NOTE
 * =======================================================
 *   2023.12.01     이도현         최초작성
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PromptVO {
    /** 프롬프트 설명 **/
    private String title;

    /** 프롬프트 ID **/
    private String promptId;

    /** 프롬프트 명 **/
    private String promptNm;

    /** 프롬프트 설명 **/
    private String promptDesc;

    /** 프롬프트 타입 **/
    private String promptType;

    /** 최소 허용 값 **/
    private String minValue;

    /** 최대 허용 값 **/
    private String maxValue;

    /** DSSPromptType (XML에 들어가는 Type) **/
    private String pt;

    /** 프롬프트 설명에 들어간 InputTag의 종류 (타입) [checkbox, selectbox, radio) **/
    private String tagType;

    /** 템플릿 ID (DB 용) **/
    private String tmpId;

    /** 프롬프트 서브 타입 **/
    private String promptSubType;

    /** 애트리뷰트 혹은 entity ID (DB용) **/
    private String attrId;

    /** 구성요소 프롬프트 전용 (애트리뷰트 정보) **/
    private AttributeVO attr;

    /** 값 프롬프트 전용 (값 정보) **/
    private String val;

    /** 개체 프롬프트 전용 (개체 정보) **/
    private List<ObjectVO> entity;
}
