package com.insdiide.ibip.domain.prompt.vo2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PromptsVO {

    private List<PromptVO> prompts;

    private boolean promptExist;
}
