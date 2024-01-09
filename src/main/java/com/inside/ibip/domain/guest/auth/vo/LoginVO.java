package com.inside.ibip.domain.guest.auth.vo;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @FileName     : LoginVO.java
 * @Date         : 2023.12.01
 * @Author       : 이도현
 * @Description  : 로그인 VO
 * @History
 * =======================================================
 *   DATE			AUTHOR			NOTE
 * =======================================================
 *   2023.12.01     이도현         최초작성
 *
 */
@Data
@Builder
public class LoginVO {

    /** 로그인 id **/
    @NotBlank
    private String id;

    /** 로그인 pwd **/
    @NotBlank
    private String pwd;
}
