package com.inside.ibip.domain.guest.favorite.mapper;

import com.inside.ibip.domain.guest.favorite.vo.FavoriteVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FavoriteMapper {

    public int checkFavorite(FavoriteVO favoriteInfo);

    public int insertFavorite(FavoriteVO favoriteInfo);

    public List<FavoriteVO> getFavorite(String userId);

    public int selectFavorite(@Param("reportId")String reportId, @Param("userId")String userId);

    public int deleteFavorite(FavoriteVO favoriteInfo);
}
