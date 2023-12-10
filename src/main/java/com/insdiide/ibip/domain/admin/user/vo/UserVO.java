package com.insdiide.ibip.domain.admin.user.vo;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserVO {

    private String userId;

    private boolean enableYn;

    private String assignYn;

    private String loginID;

    private String userNm;

    private String owner;

    private String modification;

    private String description;
}
