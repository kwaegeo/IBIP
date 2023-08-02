package com.insdiide.ibip.domain.login.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseVO {

    private String code;

    private String msg;

}
