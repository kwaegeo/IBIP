package com.insdiide.ibip.domain.template.mapper;

import com.insdiide.ibip.domain.prompt.vo.PromptVO;
import com.insdiide.ibip.domain.template.TemplateVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TemplateMapper {

    public int checkTemplate(String templateName);

    public int insertTemplate(TemplateVO template);

    public int insertPrompts(PromptVO prompt);

}
