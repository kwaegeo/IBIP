package com.inside.ibip.global.test.mapper;

import com.inside.ibip.global.test.vo.TestVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestMapper {

    public TestVO getTest();
}
