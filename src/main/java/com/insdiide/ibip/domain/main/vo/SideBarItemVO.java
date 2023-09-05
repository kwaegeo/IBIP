package com.insdiide.ibip.domain.main.vo;

import com.insdiide.ibip.domain.login.vo.FolderVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SideBarItemVO {

    // 공유 리포트 하위 목록
    private List<FolderVO> shareFolderItems;


    // 내 리포트, 사용내역 목록, 구독 목록, 즐겨찾기 등
    private List<FolderVO> myFolderItems;
}
