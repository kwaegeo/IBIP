package com.insdiide.ibip.domain.folder.vo;

import com.insdiide.ibip.domain.login.vo.FolderVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TopItemVO {
    List<FolderVO> subFolderItems;
}
