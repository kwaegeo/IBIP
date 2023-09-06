package com.insdiide.ibip.domain.main.vo;

import com.insdiide.ibip.domain.login.vo.FolderVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SideBarItemVO {

    // 공유 리포트 하위 목록
    private List<FolderVO> shareFolderItems;

    // 내 리포트, 즐겨찾기
    private FolderVO myReport;

    // 즐겨찾기
    private FolderVO myFavorite;

}
