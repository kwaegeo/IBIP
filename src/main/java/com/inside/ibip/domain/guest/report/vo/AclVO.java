package com.inside.ibip.domain.guest.report.vo;

import com.inside.ibip.domain.guest.prompt.vo.PromptVO;
import lombok.*;

import java.util.List;

/**
 * @FileName     : AclVO.java
 * @Date         : 2023.12.01
 * @Author       : 이도현
 * @Description  : 권한 VO
 * @History
 * =======================================================
 *   DATE			AUTHOR			NOTE
 * =======================================================
 *   2023.12.01     이도현         최초작성
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class AclVO {

    /** 리포트 ID **/
    private String userId;

    /** 리포트 명 **/
    private String userNm;

    /**권한 번호**/
    private int aclNum;

    /**권한 명**/
    private String aclNm;

}
