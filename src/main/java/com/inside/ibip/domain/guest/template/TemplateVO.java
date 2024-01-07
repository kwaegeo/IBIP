package com.inside.ibip.domain.guest.template;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
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
