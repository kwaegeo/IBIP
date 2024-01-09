package com.inside.ibip.domain.guest.main.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchResultVO {

    private String resultId;

    private String resultName;

    private int resultType;

    private String resultOwner;

    private String creationTime;

    private String resultPath;

}
