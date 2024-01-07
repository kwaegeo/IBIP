package com.inside.ibip.global.test.service;

import com.inside.ibip.global.test.mapper.TestMapper;
import com.inside.ibip.global.test.vo.TestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    @Autowired
    private TestMapper testMapper;

    public void testGet(){
        TestVO test = testMapper.getTest();
        System.out.println(test);

    }
}
