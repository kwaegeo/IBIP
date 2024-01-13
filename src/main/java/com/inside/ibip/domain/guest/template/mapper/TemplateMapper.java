package com.inside.ibip.domain.guest.template.mapper;

import com.inside.ibip.domain.guest.prompt.vo.ElementVO;
import com.inside.ibip.domain.guest.prompt.vo.ObjectVO;
import com.inside.ibip.domain.guest.prompt.vo.PromptVO;
import com.inside.ibip.domain.guest.template.vo.TemplateVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TemplateMapper {

    public int checkTemplate(@Param("tmpNm")String templateName, @Param("reportId")String reportId);

    public int insertTemplate(TemplateVO template);

    public int insertPrompts(List<PromptVO> prompt);

    public int insertEntity(List<ObjectVO> object);

    public int insertElement(List<ElementVO> element);

    public int deleteTemplate(String templateId);

    public int insertValue(PromptVO prompt);

    public List<TemplateVO> selectTemplate(String reportId, String userId);

    public List<ObjectVO> getTemplate(String templateId);
}
