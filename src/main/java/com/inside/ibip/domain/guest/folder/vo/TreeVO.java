package com.inside.ibip.domain.guest.folder.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @FileName     : TreeVO.java
 * @Date         : 2023.12.01
 * @Author       : 이도현
 * @Description  : 폴더의 트리구조를 파싱하는 VO
 * @History
 * =======================================================
 *   DATE			AUTHOR			NOTE
 * =======================================================
 *   2023.12.01     이도현         최초작성
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TreeVO {

    /** 요소 ID **/
    private String id;

    /** 요소명 **/
    private String text;

    /** 부모 노드 ID **/
    private String parent;

    /** 요소 타입 (폴더, 리포트, 다큐먼트) **/
    private int type;

    /** 자식 유무 **/
    private boolean children;

    public TreeVO(String id, String text, String parent, int type){
        this.id = id;
        this.text = text;
        this.parent = parent;
        this.type = type;
    }
}

