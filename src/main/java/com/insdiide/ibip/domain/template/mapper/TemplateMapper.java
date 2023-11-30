package com.insdiide.ibip.domain.template.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TemplateMapper {

    public int checkTemplate(String templateName);

}
