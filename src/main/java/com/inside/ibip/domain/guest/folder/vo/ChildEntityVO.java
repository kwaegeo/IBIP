package com.inside.ibip.domain.guest.folder.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChildEntityVO {
    List<EntityVO> childList;
}
