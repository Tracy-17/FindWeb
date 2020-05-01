package com.shu.find.controller;

import com.shu.find.cache.TagCache;
import com.shu.find.dto.ContentDTO;
import com.shu.find.model.Content;
import com.shu.find.model.User;
import com.shu.find.service.ContentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author ShiQi
 * @Date 2020/4/23 19:52
 */
@Controller
public class PublishController {

    @Resource
    private ContentService contentService;

    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id") Integer id,
                       Model model) {
        ContentDTO question = contentService.getById(id);
        model.addAttribute("title", question.getTitle());
        model.addAttribute("description", question.getDescription());
        model.addAttribute("tag", question.getTag());
        model.addAttribute("id", question.getId());
        model.addAttribute("tags", TagCache.get());
        return "publish";
    }

    @GetMapping("/publish")//get方法：渲染页面
    public String publish(Model model) {
        //标签库
        model.addAttribute("tags", TagCache.get());
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "id", required = false) Integer id,
            @RequestParam(value = "isArticle", required = false) Boolean isArticle,
            HttpServletRequest request,
            //model可以把数据推送到前端页面
            Model model) {
        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("tag", tag);
        model.addAttribute("tags", TagCache.get());
        //逻辑校验（建议写在前端）
        if (title == null || title == "") {
            model.addAttribute("error", "标题不能为空！");
            return "publish";
        }
        if (description == null || description == "") {
            model.addAttribute("error", "问题描述不能为空！");
            return "publish";
        }
        if (tag == null || tag == "") {
            model.addAttribute("error", "标签不能为空！");
            return "publish";
        }

        String invalid = TagCache.filterInvalid(tag);
        if (StringUtils.isNotBlank(invalid)) {
            model.addAttribute("error", "输入非法标签：" + invalid);
            return "publish";
        }
        //获取当前页面用户信息
        //验证登录：
        User user = (User) request.getSession().getAttribute("user");
        //未登录跳转：
        if (user == null) {
            model.addAttribute("error", "用户未登录");
            return "/publish";
        }
        //提问数据进库
        Content content = new Content();
        content.setTitle(title);
        content.setDescription(description);
        content.setTag(tag);
        content.setId(id);
        if(isArticle!=null){
            content.setType(1);
        }else{
            content.setType(0);
        }
        content.setCreator(user.getId());

        //存在风险：非法修改
        contentService.createOrUpdate(content);
        return "redirect:/index";
    }
}

