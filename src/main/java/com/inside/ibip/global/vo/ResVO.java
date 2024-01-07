package com.inside.ibip.global.vo;

import com.inside.ibip.global.exception.code.ResultCode;
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
