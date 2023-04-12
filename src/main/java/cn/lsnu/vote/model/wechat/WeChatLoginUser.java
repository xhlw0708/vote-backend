package cn.lsnu.vote.model.wechat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeChatLoginUser {

    // 会话密钥
    private String session_key;

    // 用户唯一标识
    private String openid;

    // 用户在开放平台的唯一标识符，若当前小程序已绑定到微信开放平台帐号下会返回
    private String unionid;
}
