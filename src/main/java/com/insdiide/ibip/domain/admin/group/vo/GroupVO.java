package com.insdiide.ibip.domain.admin.group.vo;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class GroupVO {

    private String groupId;

    private String groupNm;

    private int childCnt;

    private String description;

    private String creationTime;

    private String owner;
}
