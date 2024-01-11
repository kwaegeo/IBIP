package com.inside.ibip.domain.guest.main.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @FileName     : UserInfoVO.java
 * @Date         : 2023.12.01
 * @Author       : 이도현
 * @Description  : 사용자 정보 VO
 * @History
 * =======================================================
 *   DATE			AUTHOR			NOTE
 * =======================================================
 *   2023.12.01     이도현         최초작성
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoVO {

    /** 사용자 Id **/
    private String userId;

    /** 사용자 명 **/
    private String userName;

    /** 관리자 권한 유무 **/
    private String adminYn = "N";  //관리자 권한


}
