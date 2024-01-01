package com.insdiide.ibip.domain.admin.license.vo;

import com.insdiide.ibip.domain.admin.user.vo.UserVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LicenseVO {

    private String licenseNm;

    private int licenseType;

    private String description;

    private int maxUsage;

    private int totalUsage;

    private int enableUsage;

    private int disableUsage;

    private List<UserVO> disableUsers;

    private List<UserVO> enableUsers;

}
