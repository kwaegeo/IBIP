package com.inside.ibip.domain.guest.template.mapper;

import com.inside.ibip.domain.guest.prompt.vo.ObjectVO;
import com.inside.ibip.domain.guest.prompt.vo.PromptVO;
import com.inside.ibip.domain.guest.template.TemplateVO;
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
