package com.shu.find.cache;

import com.shu.find.dto.HotTagDTO;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Author:ShiQi
 * Date:2020/5/5-10:50
 * "top n"
 */
@Component
@Data
public class HotTagCache {
    private List<String> hots = new ArrayList<>();

    //连接配置文件:
    @Value("${tag.maxTag}")
    private Integer max;

    //排序
    public void updateTags(Map<String, Integer> tags) {
        PriorityQueue<HotTagDTO> priorityQueue = new PriorityQueue<>(max);

        tags.forEach((name, priority) -> {
            HotTagDTO hotTagDTO = new HotTagDTO();
            hotTagDTO.setName(name);
            hotTagDTO.setPriority(priority);
            if (priorityQueue.size() < max) {
                priorityQueue.add(hotTagDTO);
            } else {
                //最小元素：
                HotTagDTO minHot = priorityQueue.peek();
                if (hotTagDTO.compareTo(minHot) > 0) {
                    //当前热度>最小元素热度
                    priorityQueue.poll();//取出最小值
                    priorityQueue.add(hotTagDTO);
                }
            }
        });
        List<String> sortedTags = new ArrayList<>();
        HotTagDTO poll = priorityQueue.poll();
        while (poll != null) {
            sortedTags.add(0, poll.getName());
            poll = priorityQueue.poll();
        }
        //每次循环重新赋值
        hots=sortedTags;
    }
}
