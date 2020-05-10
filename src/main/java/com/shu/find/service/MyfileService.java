package com.shu.find.service;

/**
 * Author:ShiQi
 * Date:2020/5/8-22:53
 */
import com.shu.find.mapper.MyfileMapper;
import com.shu.find.model.Myfile;
import com.shu.find.model.MyfileExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Component
public class MyfileService {

    @Autowired
    private MyfileMapper myfileMapper;

    //插入
    public void insertUrl(Myfile myfile){
         myfileMapper.insert(myfile);
    }
    //查询
    public List<Myfile> selectFile(){
        List<Myfile> myfiles= myfileMapper.selectByExample(new MyfileExample());
        return  myfiles;
    }
}
