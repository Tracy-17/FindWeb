package com.shu.find.service;

import com.shu.find.dto.PaginationDTO;
import com.shu.find.dto.QuestionDTO;
import com.shu.find.exception.CustomizeErrorCode;
import com.shu.find.exception.CustomizeException;
import com.shu.find.mapper.QuestionExtMapper;
import com.shu.find.mapper.QuestionMapper;
import com.shu.find.mapper.UserMapper;
import com.shu.find.model.Question;
import com.shu.find.model.QuestionExample;
import com.shu.find.model.User;
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
public class QuestionService {
    @Resource
    private QuestionMapper questionMapper;
    //为优化多线程操作，自己写的方法
    @Resource
    private QuestionExtMapper questionExtMapper;
    @Resource
    private UserMapper userMapper;

        //展示在首页的问题列表
        public PaginationDTO<QuestionDTO> list(String search) {
            //查找：
            if (StringUtils.isNotBlank(search)) {
                String[] tags = StringUtils.split(search, " ");
                //按空格拆分，拼上|，传递至数据库查找
                search = Arrays.stream(tags).collect(Collectors.joining("|"));

            }
            PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO<>();

            List<Question> questions = questionExtMapper.selectBySearch(search);
            List<QuestionDTO> questionDTOS = new ArrayList<>();
            for (Question question : questions) {
                User user = userMapper.selectByPrimaryKey(question.getCreator());
                QuestionDTO questionDTO = new QuestionDTO();
                //BeanUtils.copyProperties:对象之间属性的赋值
                BeanUtils.copyProperties(question, questionDTO);
                questionDTO.setUser(user);
                questionDTOS.add(questionDTO);
            }
            paginationDTO.setData(questionDTOS);

            return paginationDTO;
        }

        //展示在个人页的问题列表
        public PaginationDTO<QuestionDTO> list(Integer userId) {
            PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO<QuestionDTO>();
            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria()
                    .andCreatorEqualTo(userId);
            QuestionExample example = new QuestionExample();
            example.createCriteria()
                    .andCreatorEqualTo(userId);
            List<Question> questions = questionMapper.selectByExample(example);
            List<QuestionDTO> questionDTOS = new ArrayList<>();
            for (Question question : questions) {
                User user = userMapper.selectByPrimaryKey(question.getCreator());
                QuestionDTO questionDTO = new QuestionDTO();
                //BeanUtils.copyProperties:对象之间属性的赋值
                BeanUtils.copyProperties(question, questionDTO);
                questionDTO.setUser(user);
                questionDTOS.add(questionDTO);
            }
            paginationDTO.setData(questionDTOS);

            return paginationDTO;
        }

        public QuestionDTO getById(Integer id) {
            Question question = questionMapper.selectByPrimaryKey(id);
            //异常处理
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            //获取user对象
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            questionDTO.setUser(user);
            return questionDTO;
        }

        public void createOrUpdate(Question question) {
            if (question.getId() == null) {
                //创建提问
                question.setGmtCreate(System.currentTimeMillis());
                question.setGmtModify(question.getGmtCreate());
                question.setViewCount(0);
                question.setCommentCount(0);
                question.setLikeCount(0);
                question.setCollCount(0);

                questionMapper.insert(question);
            } else {
                //更新
                question.setGmtModify(System.currentTimeMillis());
                Question updateQuestion = new Question();

                updateQuestion.setGmtModify(System.currentTimeMillis());
                updateQuestion.setTitle(question.getTitle());
                updateQuestion.setDescription(question.getDescription());
                updateQuestion.setTag(question.getTag());

                QuestionExample example = new QuestionExample();
                example.createCriteria()
                        .andIdEqualTo(question.getId());
                int updated = questionMapper.updateByExampleSelective(updateQuestion, example);
                //异常处理
                if (updated != 1) {
                    throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
                }
            }
        }

        public void incView(Integer id) {
    /*    Question question = questionMapper.selectByPrimaryKey(id);
        Question updateQuestion = new Question();
        //多人同时访问会出问题
        updateQuestion.setViewCount(question.getViewCount()+1);*/

            Question question = new Question();
            question.setId(id);
            //每次递增一个步长
            question.setViewCount(1);
            questionExtMapper.incView(question);
        }

        public List<QuestionDTO> selectRelated(QuestionDTO queryDTO) {
            if (StringUtils.isBlank(queryDTO.getTag())) {
                return new ArrayList<>();
            }
            String[] tags = StringUtils.split(queryDTO.getTag(), "[,\\，]");
            //???
            String regexpTag = Arrays.stream(tags).collect(Collectors.joining("|"));
            Question question = new Question();
            question.setId(queryDTO.getId());
            question.setTag(regexpTag);

            List<Question> questions = questionExtMapper.selectRelated(question);
            List<QuestionDTO> questionDTOS = questions.stream().map(q -> {
                QuestionDTO questionDTO = new QuestionDTO();
                BeanUtils.copyProperties(q, questionDTO);
                return questionDTO;
            }).collect(Collectors.toList());

            return questionDTOS;
        }
    }
