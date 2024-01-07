package com.inside.ibip.domain.guest.template;

import com.inside.ibip.domain.guest.main.vo.UserInfoVO;
import com.inside.ibip.domain.guest.report.vo.ReportVO;
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
