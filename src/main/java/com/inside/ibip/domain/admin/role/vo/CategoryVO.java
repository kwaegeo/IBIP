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
public class CategoryVO {

    private String categoryNm;

    private int categoryType;

    List<PrivilegeVO> children;


    //트리 구조를 위한 매개변수 설정

    private String text;

    private String type;
}
