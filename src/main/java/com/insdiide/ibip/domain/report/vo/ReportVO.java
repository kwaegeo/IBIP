package com.insdiide.ibip.domain.report.vo;

import com.insdiide.ibip.domain.prompt.vo.PromptVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReportVO {

    private String reportId; //리포트 ID

    private String reportNm; //리포트 명

    private String reportPath; //리포트 경로

    private String promptExist; //프롬프트 유무

    private String templateExist; //템플릿 유무

    private List<PromptVO> prompts; //프롬프트 리스트

    private String editYn = "N"; //편집 모드 YN

    private String exportType; // 내보내기 타입

}
