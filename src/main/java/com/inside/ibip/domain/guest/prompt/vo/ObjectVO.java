package com.inside.ibip.domain.guest.prompt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @FileName     : ObjectVO.java
 * @Date         : 2023.12.01
 * @Author       : 이도현
 * @Description  : 개체  프롬프트 전용 VO
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
public class ObjectVO {

    /** 개체 ID **/
    private String entityId; //개체 ID

    /** 개체명 **/
    private String entityNm; //개체명

    /** 개체 설명 **/
    private String entityDesc; //개체 설명

    /** 개체 타입 (애트리뷰트, 메트릭) **/
    private int entityType; //개체 타입 (애트리뷰트, 메트릭)

    /** 기본값 yn **/
    private String defaultAnswerYn = "N"; //기본값 yn

    /** 프롬프트 Id **/
    private String promptId;

    /** 템플릿 Id **/
    private String tmpId;
}
