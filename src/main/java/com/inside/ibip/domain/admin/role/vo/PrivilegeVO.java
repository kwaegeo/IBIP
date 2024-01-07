package com.inside.ibip.domain.admin.role.vo;

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

    private int privilegeType;

    private int categoryType;

    //트리 구조를 위한 매개변수 설정

    private String text;

    private String type;

    private String assignYn;

    private StateVO state;
}
