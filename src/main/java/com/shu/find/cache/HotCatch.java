package com.shu.find.cache;

import com.shu.find.dto.ContentDTO;
import com.shu.find.dto.HotTagDTO;
import com.shu.find.model.Content;
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
public class HotCatch {
    private List<String> hotTags = new ArrayList<>();
    private List<Content> hotContents =new ArrayList<>();

    //连接配置文件:
    @Value("${tag.maxTag}")
    private Integer max;

    //排序
    public void updateHotTags(Map<String, Integer> tags) {
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
        hotTags =sortedTags;
    }
    public void updateHotContents(Map<Content, Integer> tags) {
        PriorityQueue<ContentDTO> priorityQueue = new PriorityQueue<>(max);
        tags.forEach((content, priority) -> {
            ContentDTO contentDTO = new ContentDTO();
            contentDTO.setContent(content);
            contentDTO.setPriority(priority);
            if (priorityQueue.size() < max) {
                priorityQueue.add(contentDTO);
            } else {
                //最小元素：
                ContentDTO minHot = priorityQueue.peek();
                if (contentDTO.compareTo(minHot) > 0) {
                    //当前热度>最小元素热度
                    priorityQueue.poll();//取出最小值
                    priorityQueue.add(contentDTO);
                }
            }
        });
        List<Content> sortedContents = new ArrayList<>();
        ContentDTO poll = priorityQueue.poll();
        while (poll != null) {
            sortedContents.add(0, poll.getContent());
            poll = priorityQueue.poll();
        }
        //每次循环重新赋值
        hotContents =sortedContents;
    }
}
