package com.inside.ibip.domain.guest.main.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @FileName     : SearchResultVO.java
 * @Date         : 2023.12.01
 * @Author       : 이도현
 * @Description  : 검색 결과 VO
 * @History
 * =======================================================
 *   DATE			AUTHOR			NOTE
 * =======================================================
 *   2023.12.01     이도현         최초작성
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchResultVO {

    /** 결과 요소 Id  **/
    private String resultId;

    /** 결과 요소 명 **/
    private String resultName;

    /** 결과 요소 타입 **/
    private int resultType;

    /** 결과 요소 소유자 **/
    private String resultOwner;

    /** 결과 요소 생성 일시 **/
    private String creationTime;

    /** 결과 요소 경로 **/
    private String resultPath;

}
