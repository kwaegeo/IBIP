package com.inside.ibip.domain.guest.favorite.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteVO {

    private String userId;

    private String favoriteId;

    private String documentId;

    private String docType;
}
