package com.insdiide.ibip.global.vo;

import com.insdiide.ibip.global.exception.code.ResultCode;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResVO {
    private String code;

    private String msg;


    public ResVO(ResultCode resultCode){
        this.code = resultCode.getCode();
        this.msg = resultCode.getMsg();
    }

}
