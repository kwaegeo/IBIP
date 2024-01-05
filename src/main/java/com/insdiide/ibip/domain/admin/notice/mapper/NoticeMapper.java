package com.insdiide.ibip.domain.admin.notice.mapper;

import com.insdiide.ibip.domain.admin.notice.vo.NoticeVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NoticeMapper {
    public List<NoticeVO> getNoticeList();

    public int insertNotice (NoticeVO noticeInfo);

    public NoticeVO getNotice(String noticeId);

    public NoticeVO getNoticePopup(String noticeId);

    public List<String> getNoticePopupList();

    public int modifyNotice (NoticeVO noticeInfo);

    public int deleteNotice (String noticeId);
}
