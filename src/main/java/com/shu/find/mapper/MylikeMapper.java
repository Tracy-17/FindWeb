package com.shu.find.mapper;

import com.shu.find.model.Mylike;
import com.shu.find.model.MylikeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MylikeMapper {
    long countByExample(MylikeExample example);

    int deleteByExample(MylikeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Mylike record);

    int insertSelective(Mylike record);

    List<Mylike> selectByExample(MylikeExample example);

    Mylike selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Mylike record, @Param("example") MylikeExample example);

    int updateByExample(@Param("record") Mylike record, @Param("example") MylikeExample example);

    int updateByPrimaryKeySelective(Mylike record);

    int updateByPrimaryKey(Mylike record);
}