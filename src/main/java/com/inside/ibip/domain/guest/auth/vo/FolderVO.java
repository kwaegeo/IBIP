package com.inside.ibip.domain.guest.auth.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FolderVO {

    private String name;

    private String id;

    private int tp;
}
