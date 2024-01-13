package com.inside.ibip.domain.guest.favorite.service;

import com.inside.ibip.domain.guest.favorite.mapper.FavoriteMapper;
import com.inside.ibip.domain.guest.favorite.vo.FavoriteVO;
import com.inside.ibip.global.exception.CustomException;
import com.inside.ibip.global.exception.code.ResultCode;
import com.inside.ibip.global.vo.ResVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @FileName     : FavoriteService.java
 * @Date         : 2023.12.01
 * @Author       : 이도현
 * @Description  : 즐겨찾기 Controller, 즐겨찾기 등록 및 삭제 관리
 * @History
 * =======================================================
 *   DATE			AUTHOR			NOTE
 * =======================================================
 *   2023.12.01     이도현         최초작성
 *
 */
@Service
@Slf4j
public class FavoriteService {

    @Autowired
    private FavoriteMapper favoriteMapper;

    /**
     * 즐겨찾기에 추가
     * @Method Name   : addFavorite
     * @Date / Author : 2023.12.01  이도현
     * @param favoriteInfo 즐겨찾기에 필요한 정보 객체
     * @return 성공 유무
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    @Transactional
    public ResVO addFavorite(FavoriteVO favoriteInfo) {
        ResVO result = null;

        try {
            int checkCount = favoriteMapper.checkFavorite(favoriteInfo);
            if (checkCount > 0) {
                throw new CustomException(ResultCode.DUPLICATE_FAVORITE);
            } else {
                int resultCount = favoriteMapper.insertFavorite(favoriteInfo);
                if (resultCount == 1) {
                    result = new ResVO(ResultCode.SUCCESS);
                }
            }
        }catch (CustomException ce){
            throw ce;
        }catch (Exception e){
            log.error("즐겨찾기 등록 중 DB 에러 발생 [Error msg]: " + e.getMessage());
            throw new CustomException(ResultCode.DB_ETC_ERROR);
        }
        return result;
    }

    /**
     * 즐겨찾기에서 삭제
     * @Method Name   : addFavorite
     * @Date / Author : 2023.12.01  이도현
     * @param favoriteInfo 즐겨찾기에 필요한 정보 객체
     * @return 성공 유무
     * @History
     * 2023.12.01	최초생성
     *
     * @Description
     */
    @Transactional
    public ResVO deleteFavorite(FavoriteVO favoriteInfo) {
        ResVO result = null;

        try {
            int checkCount = favoriteMapper.checkFavorite(favoriteInfo);
            if (checkCount == 0) {
                throw new CustomException(ResultCode.INVALID_FAVORITE);
            } else {
                int resultCount = favoriteMapper.deleteFavorite(favoriteInfo);
                if (resultCount == 1) {
                    result = new ResVO(ResultCode.SUCCESS);
                }
            }
        }catch (CustomException ce){
            throw ce;
        }catch (Exception e){
            log.error("즐겨찾기 삭제 중 DB 에러 발생 [Error msg]: " + e.getMessage());
            throw new CustomException(ResultCode.DB_ETC_ERROR);
        }
        return result;
    }
}
