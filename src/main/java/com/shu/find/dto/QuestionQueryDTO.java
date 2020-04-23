package com.shu.find.dto;

import lombok.Data;

/**
 * Author:ShiQi
 * Date:2019/12/20-0:16
 */
@Data
public class QuestionQueryDTO {
    private String search;
    private Integer page;
    private Integer size;
}
