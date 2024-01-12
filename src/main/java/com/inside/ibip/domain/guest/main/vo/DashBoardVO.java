package com.inside.ibip.domain.guest.main.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @FileName     : DashBoardVO.java
 * @Date         : 2023.12.01
 * @Author       : 이도현
 * @Description  : 대시보드 VO
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
public class DashBoardVO {

    private String code;

    private String msg;

    private String dashboardURL;
}
