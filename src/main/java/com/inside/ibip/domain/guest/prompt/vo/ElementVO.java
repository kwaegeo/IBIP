package com.inside.ibip.domain.guest.prompt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @FileName     : ElementVO.java
 * @Date         : 2023.12.01
 * @Author       : 이도현
 * @Description  : 구성요소 VO
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
public class ElementVO {

    /** 구성요소 Id **/
    private String elementId;

    /** 구성요소 명 **/
    private String elementNm;

    /** 기본 선택 Yn **/
    private String defaultAnswerYn = "N";

    /** 템플릿 기능 tmpId **/
    private String tmpId;

    /** 템플릿 기능 promptId **/

    private String promptId;
}

