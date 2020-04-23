package com.shu.find.dto;

import lombok.Data;

import java.util.List;

/**
 * Author:ShiQi
 * Date:2019/12/17-20:52
 */
@Data
public class TagDTO {
    private String categoryName;
    private List<String> tags;
}
