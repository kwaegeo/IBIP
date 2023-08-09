package com.insdiide.ibip.domain.login.vo;

import com.microstrategy.web.objects.WebObjectInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ResponseVO {

    private String code;

    private String msg;

    List<WebObjectInfo> folderList;

}
