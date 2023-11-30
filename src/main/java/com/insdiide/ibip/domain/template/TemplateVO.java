package com.insdiide.ibip.domain.template;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TemplateVO {

    private String tmpId;

    private String reportId;

    private String docType;

    private String userId;

    private String userNm;

    private String regTs;

    private String delTs;

    private String useYn;

    private String tmpNm;

    private String remark;
}
