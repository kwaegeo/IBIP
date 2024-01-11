package com.inside.ibip.domain.guest.folder.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @FileName     : FolderVO.java
 * @Date         : 2023.12.01
 * @Author       : 이도현
 * @Description  : 폴더 VO
 * @History
 * =======================================================
 *   DATE			AUTHOR			NOTE
 * =======================================================
 *   2023.12.01     이도현         최초작성
 *
 */
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
