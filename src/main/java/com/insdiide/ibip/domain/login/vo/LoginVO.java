package com.insdiide.ibip.domain.login.vo;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class LoginVO {

    @NotBlank
    private String id;

    @NotBlank
    private String pwd;
}
