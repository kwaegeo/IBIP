package com.inside.ibip.domain.guest.prompt.vo2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AttributeVO {

    private String attrId;

    private String attrNm;

    private int attrType;

    private List<ElementVO> elements;

}
