package com.shu.find.service;

import com.shu.find.dto.ContentDTO;
import com.shu.find.dto.PaginationDTO;
import com.shu.find.enums.ContentTypeEnum;
import com.shu.find.exception.CustomizeErrorCode;
import com.shu.find.exception.CustomizeException;
import com.shu.find.mapper.ContentExtMapper;
import com.shu.find.mapper.ContentMapper;
import com.shu.find.mapper.UserMapper;
import com.shu.find.model.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author ShiQi
 * @Date 2020/4/23 18:05
 */
@Service
public class ContentService {
    @Resource
    private ContentMapper contentMapper;
    //为优化多线程操作，自己写的方法
    @Resource
    private ContentExtMapper contentExtMapper;
    @Resource
    private UserMapper userMapper;
    @Autowired
    private CollectionService collectionService;
    @Value("${content.time}")
    private Long timeBefore;
    @Value("${focus.contentCapacity}")
    private Integer contentCapacity;
    private Long nowTime = System.currentTimeMillis();

    //展示在首页的内容列表（时间倒序）
    public PaginationDTO<ContentDTO> list(String search, String tag) {
        //查找：
        if (StringUtils.isNotBlank(search)) {
            String[] tags = StringUtils.split(search, " ");
            //按空格拆分，拼上|，传递至数据库查找
            search = Arrays.stream(tags).collect(Collectors.joining("|"));
        }
        return pageContent(contentExtMapper.selectBySearch(search, tag), ContentTypeEnum.QUESTION.getType());
    }

    //文章列表（近一月收藏最高排序）
    public PaginationDTO<ContentDTO> listArticle() {
        ContentExample contentExample = new ContentExample();
        contentExample.createCriteria().andGmtCreateBetween(nowTime - timeBefore, nowTime);
        //收藏排名
        contentExample.setOrderByClause("coll_count desc");
        //description又没了。。。
        return pageContent(contentMapper.selectByExampleWithBLOBs(contentExample, new RowBounds(0, 20)), ContentTypeEnum.ARTICLE.getType());
    }

    //提取pagination
    public PaginationDTO<ContentDTO> pageContent(List<Content> contents, Integer type) {
        PaginationDTO<ContentDTO> paginationDTO = new PaginationDTO<>();
        List<ContentDTO> contentDTOS = new ArrayList<>();
        for (Content content : contents) {
            if (content.getType() == type) {
                User user = userMapper.selectByPrimaryKey(content.getCreator());
                ContentDTO contentDTO = new ContentDTO();
                //BeanUtils.copyProperties:对象之间属性的赋值
                BeanUtils.copyProperties(content, contentDTO);
                contentDTO.setUser(user);
                contentDTOS.add(contentDTO);
            }
        }
        paginationDTO.setData(contentDTOS);
        return paginationDTO;
    }

    //查询某人的发布
    public List<ContentDTO> listByCreator(User creator) {
        List<ContentDTO> contentDTOS = new ArrayList<>();
        ContentExample contentExample = new ContentExample();
        contentExample.createCriteria()
                .andCreatorEqualTo(creator.getId());
        //倒序显示：
        contentExample.setOrderByClause("gmt_create desc");
        //不知原因，查出来没有description
//        List<Content> contents = contentMapper.selectByExample(contentExample);
        List<Content> contents = contentMapper.selectByExampleWithBLOBs(contentExample, new RowBounds(0, 20));
        for (Content Content : contents) {
            ContentDTO contentDTO = new ContentDTO();
            //BeanUtils.copyProperties:对象之间属性的赋值
            BeanUtils.copyProperties(Content, contentDTO);
            contentDTO.setUser(creator);
            contentDTOS.add(contentDTO);
        }
        return contentDTOS;
    }

    //关注的人的发布
    public List<ContentDTO> followContent(List<User> followers) {
        List<ContentDTO> contentDTOS = new ArrayList<>(contentCapacity);
        for (User follower : followers) {
            List<ContentDTO> followerContents = listByCreator(follower);
            for (ContentDTO content : followerContents) {
                if (nowTime - content.getGmtCreate() < timeBefore) {
                    contentDTOS.add(content);
                }
            }
        }
        return contentDTOS;
    }

    //我的收藏
    public List<ContentDTO> listMyCollection(Integer userId) {
        List<Collection> collections = collectionService.findCollByUserId(userId);
        List<ContentDTO> contentDTOS = new ArrayList<>();
        for (Collection collection : collections) {
            Content content = contentMapper.selectByPrimaryKey(collection.getQuestionId());
            User user = userMapper.selectByPrimaryKey(content.getCreator());
            ContentDTO contentDTO = new ContentDTO();
            //BeanUtils.copyProperties:对象之间属性的赋值
            BeanUtils.copyProperties(content, contentDTO);
            contentDTO.setUser(user);
            contentDTO.setCollTime(collection.getGmtCreate());
            contentDTOS.add(contentDTO);
        }
        return contentDTOS;
    }

    //内容详情页
    public ContentDTO getById(Integer id) {
        Content content = contentMapper.selectByPrimaryKey(id);
        //异常处理
        if (content == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        ContentDTO contentDTO = new ContentDTO();
        BeanUtils.copyProperties(content, contentDTO);
        //获取user对象
        User user = userMapper.selectByPrimaryKey(content.getCreator());
        contentDTO.setUser(user);
        return contentDTO;
    }

    //添加、修改
    public void createOrUpdate(Content content) {
        if (content.getId() == null) {
            //创建提问
            content.setGmtCreate(nowTime);
            content.setGmtModify(nowTime);
            content.setViewCount(0);
            content.setCommentCount(0);
            content.setLikeCount(0);
            content.setCollCount(0);
            contentMapper.insert(content);
        } else {
            //更新
            Content updateContent = new Content();

            updateContent.setGmtModify(nowTime);
            updateContent.setTitle(content.getTitle());
            updateContent.setDescription(content.getDescription());
            updateContent.setTag(content.getTag());

            ContentExample example = new ContentExample();
            example.createCriteria()
                    .andIdEqualTo(content.getId());
            //设置操作权限
            if(contentMapper.selectByPrimaryKey(content.getId()).getCreator()!=content.getCreator()){
                throw new CustomizeException(CustomizeErrorCode.NO_PERMISSION);
            }
            /*updateByExampleSelective是更新一条数据中的某些属性，而不是更新整条数据。
              updateByExample需要将表的条件全部给出，也就是要给出一个对象*/
            int updated = contentMapper.updateByExampleSelective(updateContent, example);
            //异常处理
            if (updated != 1) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    //增加浏览量
    public void incView(Integer id) {
    /*    Content Content = questionMapper.selectByPrimaryKey(id);
        Content updateQuestion = new Content();
        //多人同时访问会出问题
        updateQuestion.setViewCount(Content.getViewCount()+1);*/

        Content content = new Content();
        content.setId(id);
        //每次递增一个步长
        content.setViewCount(1);
        contentExtMapper.incView(content);
    }

    //相关问题
    public List<ContentDTO> selectRelated(ContentDTO queryDTO) {
        if (StringUtils.isBlank(queryDTO.getTag())) {
            return new ArrayList<>();
        }
        String[] tags = StringUtils.split(queryDTO.getTag(), "[,\\，]");
        //???
        String regexpTag = Arrays.stream(tags).collect(Collectors.joining("|"));
        Content content = new Content();
        content.setId(queryDTO.getId());
        content.setTag(regexpTag);

        List<Content> contents = contentExtMapper.selectRelated(content);
        List<ContentDTO> contentDTOS = contents.stream().map(q -> {
            ContentDTO contentDTO = new ContentDTO();
            BeanUtils.copyProperties(q, contentDTO);
            return contentDTO;
        }).collect(Collectors.toList());

        return contentDTOS;
    }

    //删除内容
    public void deleteById(Integer id,Integer userId) {
        ContentDTO contentDTO=getById(id);
        //设置操作权限
        if(userId!=contentDTO.getCreator()){
            throw new CustomizeException(CustomizeErrorCode.NO_PERMISSION);
        }
        int result=contentMapper.deleteByPrimaryKey(id);
        if (result != 1) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
    }
}
