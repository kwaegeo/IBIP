package com.inside.ibip.domain.guest.folder.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FolderVO {

    /** 폴더 명 **/
    private String name;

    /** 폴더 ID **/
    private String id;

    /** 폴더 타입 **/
    private int tp;
}
