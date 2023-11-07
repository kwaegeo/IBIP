package com.insdiide.ibip.domain.test.service;

import com.insdiide.ibip.domain.test.mapper.TestMapper;
import com.insdiide.ibip.domain.test.vo.TestVO;
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
