package com.inside.ibip.domain.guest.folder.vo;

import com.inside.ibip.domain.guest.auth.vo.FolderVO;
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
