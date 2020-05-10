package com.shu.find.controller;
import com.shu.find.dto.ResultDTO;
import com.shu.find.exception.CustomizeErrorCode;
import com.shu.find.model.Myfile;
import com.shu.find.service.MyfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class FileController {
    //连接配置文件:
    @Value("${images.path}")
    private String imagePath;
    @Value("${images.url}")
    private String imageUrl;
    @Autowired
    private MyfileService myfileService;

    private String url;
    @ResponseBody
    @RequestMapping(value = "/uploadFile", produces = "application/json;charset=UTF-8")
    public Object uploadFile(@RequestParam("fileName") MultipartFile file,
                             Model model,
                             HttpServletRequest request) {
        //判断文件是否为空
        if (file.isEmpty()) {
            return ResultDTO.errorOf(CustomizeErrorCode.FILE_IS_EMPTY);
        }
        // 获取文件名
//        String fileName = ;
        //获取文件后缀名
        String name = file.getOriginalFilename();
        String suffix = "." + name.substring(name.lastIndexOf(".") + 1);
        System.out.println("文件名：" + name + ",后缀：" + suffix);
        String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + suffix;
        String path = imagePath + fileName;
        //文件绝对路径
        System.out.print("保存文件绝对路径" + path + "\n");
        //创建文件路径
        File dest = new File(path);
        //判断文件是否已经存在
//        if (dest.exists()) { return "文件已经存在"; }
        //判断文件父目录是否存在
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdir();
        }
        try {
            //上传文件
            file.transferTo(dest); //保存文件
            System.out.print("保存文件路径" + path + "\n");
            //url="http://你自己的域名/项目名/images/"+fileName;//正式项目
            url = imageUrl + fileName;//本地运行项目
            Myfile myfile = new Myfile();
            myfile.setName(fileName);
            myfile.setPath(path);
            myfile.setUrl(url);
            myfileService.insertUrl(myfile);
        } catch (IOException e) {
            return ResultDTO.errorOf(CustomizeErrorCode.IO_EXCEPTION);
        }
        System.out.println("上传成功,文件url==" + url);
        //写入session
        request.getSession().setAttribute("avatar", url);
        return "上传成功，请返回上一页继续填写注册信息";
    }
    //查询
    /*@RequestMapping("/chaxun")
    public String huizhiDuanxin(Model model){
        System.out.print("查询视频"+"\n");
        List<Myfile> myfiles= myfileService.selectFile();
        System.out.print("查询到的视频数量=="+myfiles.size()+"\n");
        model.addAttribute("Shipins", myfiles);
        return "/filelist";
    }*/
//    @RequestMapping("/shangchuan")
//
//    public String shangchuan(){
//        System.out.print("================请求路径===跳转index页面====="+"\n");
//        return "/index";
//
//    }
}