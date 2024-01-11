package com.inside.ibip.domain.guest.report.vo;

import com.inside.ibip.domain.guest.prompt.vo.PromptVO;
import lombok.*;

import java.util.List;

/**
 * @FileName     : ReportVO.java
 * @Date         : 2023.12.01
 * @Author       : 이도현
 * @Description  : 리포트 VO
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
public class ReportVO {

    /** 리포트 ID **/
    private String reportId;

    /** 리포트 명 **/
    private String reportNm;

    /** 리포트 경로 **/
    private String reportPath;

    /** 프롬프트 유무 **/
    private String promptExist;

    /** 템플릿 유무 **/
    private String templateExist;

    /** 프롬프트 리스트 **/
    private List<PromptVO> prompts;

    /** 편집 모드 YN **/
    private String editYn = "N";

    /** 내보내기 타입 **/
    private String exportType;

    /** 문서 타입**/
    private String documentType;

    /** 값 프롬프트 전용 tmpId **/
    private String tmpId;
}
