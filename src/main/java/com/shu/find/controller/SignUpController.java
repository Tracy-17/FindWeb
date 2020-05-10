package com.shu.find.controller;

import com.shu.find.cache.TagCache;
import com.shu.find.dto.ResultDTO;
import com.shu.find.exception.CustomizeErrorCode;
import com.shu.find.model.Myfile;
import com.shu.find.model.User;
import com.shu.find.service.UserService;
import com.shu.find.service.MyfileService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Author ShiQi
 * @Date 2020/4/23 20:31
 */
@Controller
public class SignUpController {
    @Autowired
    private UserService userService;
    @Autowired
    private MyfileService myfileService;
    //连接配置文件:
    @Value("${images.path}")
    private String imagePath;
    @Value("${images.url}")
    private String imageUrl;

    //get方法：渲染页面
    @GetMapping("/signUp")
    public String signUp(Model model) {
        return "/signUp";
    }

    @PostMapping("/signUp")
    public String doSignUp(@RequestParam(name = "account") String account,
                           @RequestParam(name = "password") String password,
                           @RequestParam(name = "name") String name,
                           @RequestParam(name = "bio") String bio,
                           Model model,
                           HttpServletRequest request) {
        model.addAttribute("account", account);
        model.addAttribute("password", password);
        model.addAttribute("name", name);
        model.addAttribute("bio", bio);
        //逻辑校验（建议写在前端）
        if (account == null || account == "") {
            model.addAttribute("error", "用户名不能为空");
            return "signUp";
        }
        if (password == null || password == "") {
            model.addAttribute("error", "密码不能为空");
            return "signUp";
        }
        if (name == null || name == "") {
            model.addAttribute("error", "昵称不能为空");
            return "signUp";
        }
        if (bio.length() > 255) {
            model.addAttribute("error", "简介太长啦");
            return "signUp";
        }

        //查询一下是否存在此用户
        if (userService.isExist(account)) {
            //此用户名已存在
            model.addAttribute("error", "此用户名已存在！");
            return "/signUp";
        } else {
            User user = new User();
            user.setAccount(account);
            user.setPassword(password);
            user.setName(name);
            user.setBio(bio);
            if (request.getSession().getAttribute("avatar")==null) {
                //默认头像
                user.setAvatar("https://tvax3.sinaimg.cn/crop.0.0.1080.1080.180/7579c67dly8gelmmvuec2j20u00u00tm.jpg?KID");
            } else {
                user.setAvatar((String) request.getSession().getAttribute("avatar"));
            }
            userService.create(user);
            return "redirect:/index";
        }
    }

    private String url;
    @ResponseBody
    @RequestMapping(value = "/uploadAvatar", produces = "application/json;charset=UTF-8")
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
}