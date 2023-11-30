package com.insdiide.ibip.domain.template;

import com.insdiide.ibip.domain.main.vo.UserInfoVO;
import com.insdiide.ibip.domain.report.vo.ReportVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReqTemplateVO {

    private ReportVO reportInfo;

    private UserInfoVO userInfo;

    private String templateName;

    private String remark;
}
