package com.shu.find.mapper;

import com.shu.find.model.Myfile;
import com.shu.find.model.MyfileExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MyfileMapper {
    long countByExample(MyfileExample example);

    int deleteByExample(MyfileExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Myfile record);

    int insertSelective(Myfile record);

    List<Myfile> selectByExample(MyfileExample example);

    Myfile selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Myfile record, @Param("example") MyfileExample example);

    int updateByExample(@Param("record") Myfile record, @Param("example") MyfileExample example);

    int updateByPrimaryKeySelective(Myfile record);

    int updateByPrimaryKey(Myfile record);
}