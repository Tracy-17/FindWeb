package com.shu.find.dto;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

/**
 * Author:ShiQi
 * Date:2020/5/5-16:41
 */
@Data
public class HotTagDTO implements Comparable{
    private String name;
    private Integer priority;

    @Override
    public int compareTo(@NotNull Object o) {
       /* 插入的最新元素>=当前元素时，替换掉当前元素的位置；
        不是>=时，寻找其父级元素进行比较替换*/
        return this.getPriority()-((HotTagDTO)o).getPriority();
    }
}
