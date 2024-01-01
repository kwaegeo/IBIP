package com.insdiide.ibip.domain.admin.role.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrivilegeVO {

    private String privilegeNm;

    private String description;

    private String privilegeType;
}
