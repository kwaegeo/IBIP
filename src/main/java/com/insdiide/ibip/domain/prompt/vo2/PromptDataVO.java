package com.insdiide.ibip.domain.prompt.vo2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PromptDataVO {

    private String eid = null;  // element/ object id
    private String eno = null; // attribute value
    private String name = null;  // element/ object name
    private String orgName = null;  // element/ original name
    private int type = -1;  // metric/ attribute/ element type
    private boolean selected = false;  // is selected
    private String desc = null; // description
    private String oid = null; // attribute object id
    private int pageCnt = 0;

    private String nm;

    private String desc1;
    private String title;
    private String defaultAnswer;
}
