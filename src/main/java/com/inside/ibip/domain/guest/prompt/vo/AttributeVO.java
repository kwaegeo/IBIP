package com.inside.ibip.domain.guest.prompt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @FileName     : AttributeVO.java
 * @Date         : 2023.12.01
 * @Author       : 이도현
 * @Description  : 구성요소 프롬프트 전용 애트리뷰트 VO
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
public class AttributeVO {

    /** 애트리뷰트 Id **/
    private String attrId;

    /** 애트리뷰트 명 **/
    private String attrNm;

    /** 애트리뷰트 타입 **/
    private int attrType;

    /** 구성요소 리스트 **/
    private List<ElementVO> elements;



}
