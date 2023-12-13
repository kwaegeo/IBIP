package com.insdiide.ibip.domain.admin.group.vo;

import com.insdiide.ibip.domain.admin.user.vo.UserVO;
import lombok.*;

import java.util.List;

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

    private List<UserVO> users;

    private List<GroupVO> childGroups;

    private String assignmentType;
}
