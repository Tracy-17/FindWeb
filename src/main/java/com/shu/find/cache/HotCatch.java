package com.shu.find.cache;

import com.shu.find.dto.ContentDTO;
import com.shu.find.dto.HotTagDTO;
import com.shu.find.dto.UserDTO;
import com.shu.find.model.Content;
import com.shu.find.model.User;
import lombok.Data;
import org.springframework.beans.BeanUtils;
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
    private List<ContentDTO> hotContents = new ArrayList<>();
    private List<UserDTO> hotUsers = new ArrayList<>();

    //连接配置文件:
    @Value("${tag.max}")
    private Integer tagMax;
    @Value("${content.max}")
    private Integer contentMax;
    @Value("${user.max}")
    private Integer userMax;

    //热门tag
    public void updateHotTags(Map<String, Integer> tags) {
        PriorityQueue<HotTagDTO> priorityQueue = new PriorityQueue<>(tagMax);
        tags.forEach((name, priority) -> {
            HotTagDTO hotTagDTO = new HotTagDTO();
            hotTagDTO.setName(name);
            hotTagDTO.setPriority(priority);
            if (priorityQueue.size() < tagMax) {
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
        hotTags = sortedTags;
    }

    //热门内容
    public void updateHotContents(Map<Content, Integer> contents) {
        PriorityQueue<ContentDTO> priorityQueue = new PriorityQueue<>(contentMax);
        contents.forEach((content, priority) -> {
            ContentDTO contentDTO = new ContentDTO();
            BeanUtils.copyProperties(content,contentDTO);
            contentDTO.setPriority(priority);
            if (priorityQueue.size() < contentMax) {
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
        List<ContentDTO> sortedContents = new ArrayList<>();
        ContentDTO poll = priorityQueue.poll();
        while (poll != null) {
            sortedContents.add(0, poll);
            poll = priorityQueue.poll();
        }
        //每次循环重新赋值
        hotContents = sortedContents;
    }

    //热门用户
    public void updateHotUsers(Map<User, Integer> users) {
        PriorityQueue<UserDTO> priorityQueue = new PriorityQueue<>(userMax);
        users.forEach((user, priority) -> {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);
            userDTO.setPriority(priority);
            if (priorityQueue.size() < userMax) {
                priorityQueue.add(userDTO);
            } else {
                //最小元素：
                UserDTO minHot = priorityQueue.peek();
                if (userDTO.compareTo(minHot) > 0) {
                    //当前热度>最小元素热度
                    priorityQueue.poll();//取出最小值
                    priorityQueue.add(userDTO);
                }
            }
        });
        List<UserDTO> sortedUsers = new ArrayList<>();
        UserDTO poll = priorityQueue.poll();
        while (poll != null) {
            sortedUsers.add(0, poll);
            poll = priorityQueue.poll();
        }
        //每次循环重新赋值
        hotUsers = sortedUsers;
    }
}
