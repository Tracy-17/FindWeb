package com.shu.find.schedule;

import com.shu.find.cache.HotCatch;
import com.shu.find.mapper.ContentMapper;
import com.shu.find.mapper.UserMapper;
import com.shu.find.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
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
    private HotCatch hotCatch;
    @Autowired
    private ContentMapper contentMapper;
    @Autowired
    private UserMapper userMapper;

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
    @Value("${content.time}")
    private Long timeBefore;
    @Value("${content.view}")
    private Integer contentView;
    @Value("${content.like}")
    private Integer contentLike;
    @Value("${content.comment}")
    private Integer contentComment;
    @Value("${content.collection}")
    private Integer contentCollection;


    @Value("${user.chose}")
    private Integer userChose;
    @Value("${user.like}")
    private Integer userLike;

    Integer offset = 0;
    Integer limit = 20;
    RowBounds rowBounds = new RowBounds(offset, limit);
    long nowTime = System.currentTimeMillis();

    //？？？为什么不能连配置文件？
    //曲线救国，一天跑一次，手动更新
    @Async
    @Scheduled(fixedRate = 86400000)
//    @Scheduled(cron="0 0 13 ? * *")//每天13点执行
    public void hotTagSchedule() {
        List<Content> list = new ArrayList<>();
        Map<String, Integer> tagMap = new HashMap<>();
        while (offset == 0 || list.size() == limit) {
            /*0510 tag全没了。。。*/
            /*ContentExample contentExample = new ContentExample();
            contentExample.createCriteria().andGmtCreateBetween(nowTime - sevenDaysAgo, nowTime);*/
            list = contentMapper.selectByExampleWithBLOBs(new ContentExample(), rowBounds);
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
        log.info("热门标签 ", new Date());
        System.out.println("热门标签 "+ new Date());
    }
    @Async
    @Scheduled(fixedRate = 86400000)
    public void hotContentSchedule() {
        List<Content> list = new ArrayList<>();
        Map<Content, Integer> contentMap = new HashMap<>();
        while (offset == 0 || list.size() == limit) {
            ContentExample contentExample = new ContentExample();
            contentExample.createCriteria().andGmtCreateBetween(nowTime - timeBefore, nowTime);
            list = contentMapper.selectByExampleWithBLOBs(contentExample, rowBounds);
            for (Content content : list) {
                //优先级=1*浏览量+3*点赞数+5*回复数*7*收藏数
                Integer priority = content.getViewCount() * contentView + content.getLikeCount() * contentLike
                        + content.getCommentCount() * contentComment + content.getCollCount() * contentCollection;
                //优先级筛选
                if (priority > minPriority) {
                    contentMap.put(content, priority);
                }
            }
            offset += limit;
        }
        hotCatch.updateHotContents(contentMap);
        log.info("热门问题 ", new Date());
        System.out.println("热门问题 "+ new Date());
    }
    @Async
    @Scheduled(fixedRate = 86400000)
    public void hotUserSchedule() {
        List<User> users=new ArrayList<>();
        Map<User,Integer> userMap=new HashMap<>();
        while (offset == 0 || users.size() == limit) {
            UserExample userExample = new UserExample();
            //仅限近期登陆过的用户
            userExample.createCriteria().andGmtModifyBetween(nowTime - timeBefore, nowTime);
            users = userMapper.selectByExample(userExample);
            for (User user : users) {
                //优先级=91*被选中数+9*点赞数
                Integer priority = user.getChoseCount()*userChose+user.getLikeCount()*userLike;
                userMap.put(user,priority);
            }
            offset += limit;
        }
       /*  userMap.forEach(
                (k, v) -> {
                    System.out.print(k.getName() + " " + v + ",");
                }
        );*/
        hotCatch.updateHotUsers(userMap);
        log.info("活跃用户 ", new Date());
        System.out.println("活跃用户 "+ new Date());
    }
}
