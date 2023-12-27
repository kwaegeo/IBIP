package com.insdiide.ibip.domain.admin.role.vo;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleVO {
    private String roleId;

    private String roleNm;

    private String owner;

    private String modification;

    private String description;
}
