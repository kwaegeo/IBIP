package com.inside.ibip.domain.admin.notice.service;

import com.inside.ibip.domain.admin.notice.mapper.NoticeMapper;
import com.inside.ibip.domain.admin.notice.vo.NoticeVO;
import com.inside.ibip.global.exception.CustomException;
import com.inside.ibip.global.exception.code.ResultCode;
import com.inside.ibip.global.mstr.MstrObject;
import com.inside.ibip.global.vo.ResVO;
import com.microstrategy.web.objects.WebObjectsException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.inside.ibip.global.utils.ComUtils.isNullOrEmpty;

@Log4j2
@Service
public class NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private MstrObject mstrObject;

    public List<NoticeVO> getNoticeList() {
        List<NoticeVO> noticeList = noticeMapper.getNoticeList();
        return noticeList;
    }

    public ResVO modifyNotice(NoticeVO noticeInfo){
        int resultCnt = noticeMapper.modifyNotice(noticeInfo);

        if(!(resultCnt > 0)){
            throw new CustomException(ResultCode.ETC_ERROR);
        }
        return new ResVO(ResultCode.SUCCESS);
    }

    public NoticeVO getNoticeInfo(String noticeId){
        NoticeVO noticeInfo = noticeMapper.getNotice(noticeId);
        return noticeInfo;
    }

    public NoticeVO getNoticePopup(String noticeId){
        NoticeVO noticeInfo = noticeMapper.getNoticePopup(noticeId);
        return noticeInfo;
    }

    public List<String> getNoticePopupList(){
        List<String> noticePopupList = noticeMapper.getNoticePopupList();
        return noticePopupList;
    }

    public ResVO addNotice(NoticeVO noticeInfo, String userId){
        if(isNullOrEmpty(noticeInfo.getTitle())){
            throw new CustomException(ResultCode.INVALID_TITLE);
        }
        else if(isNullOrEmpty(noticeInfo.getContent())){
            throw new CustomException(ResultCode.INVALID_CONTENT);
        }
        noticeInfo.setWriter(mstrObject.getUserLoginName(userId));

        int resultCnt = noticeMapper.insertNotice(noticeInfo);

        if(!(resultCnt > 0)){
            throw new CustomException(ResultCode.ETC_ERROR);
        }
        return new ResVO(ResultCode.SUCCESS);
    }

    public ResVO delNotice(String noticeId) throws WebObjectsException {
        int resultCnt = noticeMapper.deleteNotice(noticeId);

        if(!(resultCnt > 0)){
            throw new CustomException(ResultCode.ETC_ERROR);
        }
        return new ResVO(ResultCode.SUCCESS);
    }
}
