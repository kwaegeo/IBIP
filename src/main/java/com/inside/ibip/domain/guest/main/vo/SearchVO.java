package com.inside.ibip.domain.guest.main.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * @FileName     : SearchVO.java
 * @Date         : 2023.12.01
 * @Author       : 이도현
 * @Description  : 검색 정보 VO
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
public class SearchVO {

    /** 검색 결과 리스트 **/
    private List<SearchResultVO> searchList;

}
