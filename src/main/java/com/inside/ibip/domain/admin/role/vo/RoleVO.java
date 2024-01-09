package com.inside.ibip.domain.admin.role.vo;

import lombok.*;

import java.util.List;

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

    private List<CategoryVO> categories;
}
