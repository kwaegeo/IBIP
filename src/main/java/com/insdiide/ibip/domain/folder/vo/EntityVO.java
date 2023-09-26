package com.insdiide.ibip.domain.folder.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EntityVO {

    private String id;

    private String text;

    private String parent;

    private int type;

}
