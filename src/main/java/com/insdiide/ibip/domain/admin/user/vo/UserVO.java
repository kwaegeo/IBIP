package com.insdiide.ibip.domain.admin.user.vo;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserVO {
    private boolean enableYn;

    private String userId;

    private String loginID;

    private String userNm;

    private String assignYn;

    private String owner;

    private String modification;

    private String description;
}
