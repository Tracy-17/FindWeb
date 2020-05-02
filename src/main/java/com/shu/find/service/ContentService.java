package com.shu.find.service;

import com.shu.find.dto.ContentDTO;
import com.shu.find.dto.PaginationDTO;
import com.shu.find.exception.CustomizeErrorCode;
import com.shu.find.exception.CustomizeException;
import com.shu.find.mapper.ContentExtMapper;
import com.shu.find.mapper.ContentMapper;
import com.shu.find.mapper.UserMapper;
import com.shu.find.model.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
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

    //展示在首页的内容列表
    public PaginationDTO<ContentDTO> list(String search,Integer type) {
        //查找：
        if (StringUtils.isNotBlank(search)) {
            String[] tags = StringUtils.split(search, " ");
            //按空格拆分，拼上|，传递至数据库查找
            search = Arrays.stream(tags).collect(Collectors.joining("|"));
        }
        PaginationDTO<ContentDTO> paginationDTO = new PaginationDTO<>();
        List<Content> contents = contentExtMapper.selectBySearch(search);
        List<ContentDTO> contentDTOS = new ArrayList<>();
        for (Content content : contents) {
            User user = userMapper.selectByPrimaryKey(content.getCreator());
            ContentDTO contentDTO = new ContentDTO();
            //BeanUtils.copyProperties:对象之间属性的赋值
            BeanUtils.copyProperties(content, contentDTO);
            contentDTO.setUser(user);
            if(search!=null){
                contentDTOS.add(contentDTO);
            }else if(contentDTO.getType()==type){
                contentDTOS.add(contentDTO);
            }
        }
        paginationDTO.setData(contentDTOS);
        return paginationDTO;
    }

    //展示在个人页的问题列表
    public PaginationDTO<ContentDTO> list(Integer userId) {
        PaginationDTO<ContentDTO> paginationDTO = new PaginationDTO<ContentDTO>();
        ContentExample contentExample = new ContentExample();
        contentExample.createCriteria()
                .andCreatorEqualTo(userId);
        ContentExample example = new ContentExample();
        example.createCriteria()
                .andCreatorEqualTo(userId);
        List<Content> contents = contentMapper.selectByExample(example);
        List<ContentDTO> contentDTOS = new ArrayList<>();
        for (Content Content : contents) {
            User user = userMapper.selectByPrimaryKey(Content.getCreator());
            ContentDTO contentDTO = new ContentDTO();
            //BeanUtils.copyProperties:对象之间属性的赋值
            BeanUtils.copyProperties(Content, contentDTO);
            contentDTO.setUser(user);
            contentDTOS.add(contentDTO);
        }
        paginationDTO.setData(contentDTOS);

        return paginationDTO;
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
            content.setGmtCreate(System.currentTimeMillis());
            content.setGmtModify(content.getGmtCreate());
            content.setViewCount(0);
            content.setCommentCount(0);
            content.setLikeCount(0);
            content.setCollCount(0);

            contentMapper.insert(content);
        } else {
            //更新
            content.setGmtModify(System.currentTimeMillis());
            Content updateContent = new Content();

            updateContent.setGmtModify(System.currentTimeMillis());
            updateContent.setTitle(content.getTitle());
            updateContent.setDescription(content.getDescription());
            updateContent.setTag(content.getTag());

            ContentExample example = new ContentExample();
            example.createCriteria()
                    .andIdEqualTo(content.getId());
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
}
