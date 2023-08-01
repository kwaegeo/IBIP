package com.insdiide.ibip.domain.login.vo;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class LoginVO {

    @NotBlank(message = "필수값이야 id")
    private String id;

    @NotBlank(message = "필수값이야 pwd")
    private String pwd;
}
