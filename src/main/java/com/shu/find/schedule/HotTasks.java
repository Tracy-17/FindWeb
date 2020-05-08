package com.shu.find.schedule;

import com.shu.find.cache.HotCatch;
import com.shu.find.mapper.ContentMapper;
import com.shu.find.model.Content;
import com.shu.find.model.ContentExample;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import java.util.*;


/**
 * Author:ShiQi
 * Date:2020/5/5-10:22
 * 热门话题:使用定时任务进行查询
 */
@Component
@Slf4j
public class HotTasks {

    @Autowired
    private ContentMapper contentMapper;
    @Autowired
    private HotCatch hotCatch;
    //连接配置文件:
    @Value("${fixedRate}")
    private Object fixedRate;
    @Value("${tag.view}")
    private Integer view;
    @Value("${tag.like}")
    private Integer like;
    @Value("${tag.comment}")
    private Integer comment;
    @Value("${tag.collection}")
    private Integer collection;
    @Value("${tag.content}")
    private Integer contentCount;
    @Value("${content.minPriority}")
    private Integer minPriority;

    //？？？为什么不能连配置文件？
    @Scheduled(fixedRate = 600000)//1min运行一次
//    @Scheduled(cron="0 0 5 * * *")//每天凌晨5点执行
    public void hotTagSchedule() {
        int offset = 0;
        int limit = 20;
        List<Content> list = new ArrayList<>();
        Map<String, Integer> tagMap = new HashMap<>();
        while (offset == 0 || list.size() == limit) {
            list = contentMapper.selectByExampleWithBLOBs(new ContentExample(), new RowBounds(offset, limit));
            for (Content content : list) {
                //是用中文逗号分开的。。。
                String[] tags = StringUtils.split(content.getTag(), "，");
                for (String tag : tags) {
                    Integer priority = tagMap.get(tag);
                    if (priority != null) {
                        //优先级=10*问题数+1*浏览量+3*点赞数+5*回复数*7*收藏数
                        tagMap.put(tag, priority + content.getViewCount() * view + content.getLikeCount() * like + content.getCommentCount() * comment
                                + content.getCollCount() * collection + contentCount);
                    } else {
                        tagMap.put(tag, content.getViewCount() * view + content.getLikeCount() * like + content.getCommentCount() * comment
                                + content.getCollCount() * collection + contentCount);
                    }
                }
            }
            offset += limit;
        }
        /*tagMap.forEach(
                (k, v) -> {
                    System.out.print(k + "-" + v + ",");
                }
        );*/
        hotCatch.updateHotTags(tagMap);
//        log.info("The time is now{}", new Date());
    }

    @Scheduled(fixedRate = 600000)//1min运行一次
    public void hotContentSchedule() {
        int offset = 0;
        int limit = 20;
        List<Content> list = new ArrayList<>();
        Map<Content, Integer> contentMap = new HashMap<>();
        while (offset == 0 || list.size() == limit) {
            ContentExample contentExample = new ContentExample();
            long nowTime = System.currentTimeMillis();
            long sevenDaysAgo = 7 * 24 * 60 * 60 * 1000;
            contentExample.createCriteria().andGmtCreateBetween(nowTime - sevenDaysAgo, nowTime);
            list = contentMapper.selectByExampleWithBLOBs(contentExample, new RowBounds(offset, limit));
            for (Content content : list) {
                //优先级=1*浏览量+3*点赞数+5*回复数*7*收藏数
                Integer priority=content.getViewCount() * view + content.getLikeCount() * like + content.getCommentCount() * comment
                        + content.getCollCount() * collection;
                //优先级筛选
                if(priority>minPriority) {
                    contentMap.put(content, priority);
                }
            }
            offset += limit;
        }
        hotCatch.updateHotContents(contentMap);
//        log.info("The time is now{}", new Date());
    }
}
