package cn.lsnu.vote.model.wechat;

import lombok.Data;

@Data
public class WeChatRequestParam {
    private String openid;
    private String code;
}
