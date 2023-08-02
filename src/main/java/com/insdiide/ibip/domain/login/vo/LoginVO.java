package com.insdiide.ibip.domain.login.vo;

import com.insdiide.ibip.global.annotation.CustomNotBlank;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class LoginVO {

    @CustomNotBlank(message = "필수값이야 id", code= "404")
    private String id;

    @CustomNotBlank(message = "필수값이야 pwd", code="500")
    private String pwd;
}
