package com.insdiide.ibip.domain.template.mapper;

import com.insdiide.ibip.domain.prompt.vo.ObjectVO;
import com.insdiide.ibip.domain.prompt.vo.PromptVO;
import com.insdiide.ibip.domain.report.vo.ReportVO;
import com.insdiide.ibip.domain.template.TemplateVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TemplateMapper {

    public int checkTemplate(String templateName);

    public int insertTemplate(TemplateVO template);

    public int insertPrompts(List<PromptVO> prompt);

    public int insertEntity(List<ObjectVO> object);

    public List<TemplateVO> selectTemplate(String reportId, String userId);

    public List<ObjectVO> getTemplate(String templateId);
}
