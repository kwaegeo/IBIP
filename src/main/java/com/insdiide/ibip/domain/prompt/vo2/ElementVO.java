package com.insdiide.ibip.domain.prompt.vo2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ElementVO {

    private String elementId;

    private String elementNm;

    private String defaultAnswerYn = "N";

}
