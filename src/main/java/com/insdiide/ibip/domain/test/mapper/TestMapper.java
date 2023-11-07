package com.insdiide.ibip.domain.test.mapper;

import com.insdiide.ibip.domain.test.vo.TestVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestMapper {

    public TestVO getTest();
}
