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

/**
 * @FileName     : NoticeService.java
 * @Date         : 2023.12.01
 * @Author       : 이도현
 * @Description  : 공지사항 Service, 공지사항 리스트 조회, 정보 조회, 생성, 수정, 삭제
 * @History
 * =======================================================
 *   DATE			AUTHOR			NOTE
 * =======================================================
 *   2023.12.01     이도현         최초작성
 *
 */
@Log4j2
@Service
public class NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private MstrObject mstrObject;

    /**
     * 공지사항 리스트를 조회
     * @Method Name   : getNoticeList
     * @Date / Author : 2023.12.01  이도현
     * @return 공지사항 객체 리스트
     * @History
     * 2023.12.01	최초생성
     */
    public List<NoticeVO> getNoticeList() {
        List<NoticeVO> noticeList = noticeMapper.getNoticeList();
        return noticeList;
    }

    /**
     * 공지사항 수정
     * @Method Name   : modifyNotice
     * @Date / Author : 2023.12.01  이도현
     * @param noticeInfo notice 정보 객체
     * @return 성공 유무
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    public ResVO modifyNotice(NoticeVO noticeInfo){

        int resultCnt = noticeMapper.modifyNotice(noticeInfo);

        if(!(resultCnt > 0)){
            throw new CustomException(ResultCode.DB_ETC_ERROR);
        }
        return new ResVO(ResultCode.SUCCESS);
    }

    /**
     * 공지사항 정보 조회
     * @Method Name   : getNoticeInfo
     * @Date / Author : 2023.12.01  이도현
     * @param noticeId 조회할 공지사항의 Id
     * @return 그룹 객체
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    public NoticeVO getNoticeInfo(String noticeId){

        NoticeVO noticeInfo = noticeMapper.getNotice(noticeId);
        return noticeInfo;
    }

    public NoticeVO getNoticePopup(String noticeId){
        NoticeVO noticeInfo = noticeMapper.getNoticePopup(noticeId);
        return noticeInfo;
    }

    /**
     * 공지사항 팝업 리스트 조회
     * @Method Name   : getNoticePopupList
     * @Date / Author : 2023.12.01  이도현
     * @return 성공 유무
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    public List<String> getNoticePopupList(){
        List<String> noticePopupList = noticeMapper.getNoticePopupList();
        return noticePopupList;
    }

    /**
     * 공지사항 생성
     * @Method Name   : addNotice
     * @Date / Author : 2023.12.01  이도현
     * @param noticeInfo group 정보 객체
     * @param userId 사용자 Id
     * @return 성공 유무
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    public ResVO addNotice(NoticeVO noticeInfo, String userId){

        //1. 제목 유효성 검사
        if(isNullOrEmpty(noticeInfo.getTitle())){
            throw new CustomException(ResultCode.INVALID_TITLE);
        }
        //2. 본문 유효성 검사
        else if(isNullOrEmpty(noticeInfo.getContent())){
            throw new CustomException(ResultCode.INVALID_CONTENT);
        }

        //3. 사용자 이름 추가
        noticeInfo.setWriter(mstrObject.getUserLoginName(userId));

        //4. 공지사항 생성
        int resultCnt = noticeMapper.insertNotice(noticeInfo);

        if(!(resultCnt > 0)){
            throw new CustomException(ResultCode.DB_ETC_ERROR);
        }
        return new ResVO(ResultCode.SUCCESS);
    }

    /**
     * 공지사항 삭제
     * @Method Name   : delNotice
     * @Date / Author : 2023.12.01  이도현
     * @param noticeId 삭제할 공지사항 정보
     * @return 성공 유무
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    public ResVO deleteNotice(String noticeId){
        int resultCnt = noticeMapper.deleteNotice(noticeId);

        if(!(resultCnt > 0)){
            throw new CustomException(ResultCode.DB_ETC_ERROR);
        }
        return new ResVO(ResultCode.SUCCESS);
    }
}
