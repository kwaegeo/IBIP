package com.insdiide.ibip.domain.admin.notice.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeVO {

    private String noticeId;

    private String title;

    private String content;

    private String writer;

    private String regTs;

    private String modTs;

    private String delTs;

    private String popupYn;

    private String useYn;
}
