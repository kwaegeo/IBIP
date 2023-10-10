package com.insdiide.ibip.domain.prompt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ObjectVO {

    private String entityId; //개체 ID

    private String entityNm; //개체명

    private String entityDesc; //개체 설명

    private int entityType; //개체 타입 (애트리뷰트, 메트릭)

    private String defaultAnswerYn = "N"; //기본값 yn

}
