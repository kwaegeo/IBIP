package com.insdiide.ibip.domain.main.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoVO {

    private String userId;

    private String userName;

    private String adminYn = "N";  //관리자 권한


}
