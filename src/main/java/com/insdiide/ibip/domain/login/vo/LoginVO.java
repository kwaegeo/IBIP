package com.insdiide.ibip.domain.login.vo;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class LoginVO {

    @NotBlank(message = "id는 필수 입력 값입니다.")
    private String id;

    @NotBlank(message = "password는 필수 입력 값입니다.")
    private String pwd;
}
