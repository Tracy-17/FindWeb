package com.shu.find.dto;

import lombok.Data;

/**
 * Author:ShiQi
 * Date:2019/12/19-21:48
 */
@Data
public class FileDTO {
    //图片上传示例：http://editor.md.ipandao.com/examples/image-upload.html
    private int success;
    private String message;
    private String url;
}
