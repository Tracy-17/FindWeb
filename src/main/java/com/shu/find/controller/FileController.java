package com.shu.find.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.shu.find.dto.FileDTO;

import javax.servlet.http.HttpServletRequest;

/**
 * Author:ShiQi
 * Date:2019/12/19-21:45
 */
@Controller
public class FileController {
    @RequestMapping("/file/upload")
    @ResponseBody
    public FileDTO upload(HttpServletRequest request) {
        //强转
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        //前端寻找上传框的name
        MultipartFile file = multipartHttpServletRequest.getFile("editormd-image-file");
        //上传
/*
        DiskFileItemFactory factory = new DiskFileItemFactory();//加缓冲区。需要将接口变为实现类
        ServletFileUpload upload = new ServletFileUpload(factory);
        //控制上传文件的大小：（对象：ServletFileUpload，在解析文件之前）
        upload.setSizeMax(307200);//单位：字节B
        //通过parseRequest解析form中的所有请求字段，并保存到items集合中
        List<FileItem> items = null;
        try {
            items = upload.parseRequest((RequestContext) request);
        } catch (FileUploadException e) {
            e.printStackTrace();
        }
        //迭代器遍历，取出字段
        Iterator<FileItem> iter = items.iterator();
        while (iter.hasNext()) {
            FileItem item = iter.next();
            //无序，需要根据name属性判断字段
            //文件上传处理
            //文件名：(getName获取文件名)
            String fileName = item.getName();
            //获取文件内容并上传：
            //定义文件路径（指定上传位置）：upload
//                        String path="xxxx";//强烈耦合，建议使用动态地址
            String path = "/res/";
//                        String path = "F:\\File\\computerscience\\IdeaWorkSpace\\EldCare\\web\\images\\";
            File file = new File(path, fileName);//输出文件路径

            //上传：
            try {
                item.write(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //重命名：
            File newFile = new File(path + "\\nursePhoto" + id + "." + ext);
            File file1=file.getAbsoluteFile();
            System.out.println(file1);
            System.out.println(newFile);
            //如果存在同名文件，则删除
            if(newFile.exists()){
                newFile.delete();
            }
            return file1.renameTo(newFile);
            System.out.println("是否重命名成功：" + FileUtil.rename(file, newFile));*/

        FileDTO fileDTO = new FileDTO();
        fileDTO.setSuccess(1);
        fileDTO.setUrl("/res/" + file.getName());
        return fileDTO;
    }
}
