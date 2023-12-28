package com.insdiide.ibip.domain.admin.user.vo;

import com.insdiide.ibip.domain.admin.group.vo.GroupVO;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserVO {

    private boolean enableStatus;

    private String userId;

    private String loginID;

    private String userNm;

    private String assignYn;

    private String owner;

    private String modification;

    private String description;

    private List<GroupVO> parentsGroups;

    private String assignmentType;

    private String password1;

    private String password2;


}
