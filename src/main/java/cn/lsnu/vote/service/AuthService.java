package cn.lsnu.vote.service;

import cn.lsnu.vote.model.domain.User;
import cn.lsnu.vote.model.wechat.WeChatRequestParam;

public interface AuthService {

    // 微信登录验证
    User weChatAuth(WeChatRequestParam requestParam);
}
