package com.inside.ibip.domain.admin.role.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrivilegeAssignVO {

    private String roleId;

    private List<PrivilegeVO> addedPrivileges;

    private List<PrivilegeVO> removedPrivileges;

}
