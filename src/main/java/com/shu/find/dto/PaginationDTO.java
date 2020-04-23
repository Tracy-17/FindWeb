package com.shu.find.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author ShiQi
 * @Date 2020/4/23 19:36
 * 在首页显示通知信息
 */
@Data
public class PaginationDTO<T> {
//    适配展示任何类型
    private List<T> data;
}
