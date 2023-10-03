package com.insdiide.ibip.domain.prompt.vo2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PromptVO {

    private String pid = null; // prompt id
    private String pnm = null; // prompt name
    private String ptp = null; // prompt type
    private String ctp = null; // custom component type
    private String aid = null; // attribute id
    private List<String> aidList = null; // attribute id list for hierarchy
    private String val = null; // value
    private boolean required = false; // is Required
    private String currentYear = null; // current year
    private String currentMonth = null; // current month
    private String currentDay = null; // current month
    private List<PromptDataVO> data = null; // data
    private int ptc = 0; // prompt count
    private String pmxc = null;	// prompt max count
    private String pmic = null;	// prompt min count
    private HashMap aidNmIdList = null;	// attribute id, name list for hierarchy
    private String hiObjId = null; // hierarchy object id
    private String defaultEl = null; //default check elements id
    private int totalPage = 0;
    private String desc = null;

//    private List<PromptDataVO> data; //프롬포트 데이터 리스트

    private String title; //프롬프트 설명

    private String promptName; //프롬프트 명

    private String promptType; //프롬프트 타입

    private String promptId; //프롬프트 ID
}
