package com.shu.find.dto;

import lombok.Data;

/**
 * Author:ShiQi
 * Date:2019/12/6-23:32
 * 授权对象。dto：数据传输模型
 */
@Data
public class AccessTokenDTO {
    private String clientId;
    private String clientSecret;
    private String code;
    private String redirectUri;
    private String state;

}
