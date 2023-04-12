package cn.lsnu.vote.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.lsnu.vote.common.Constants;
import cn.lsnu.vote.common.Result;
import cn.lsnu.vote.exception.CustomerException;
import cn.lsnu.vote.model.domain.User;
import cn.lsnu.vote.model.wechat.WeChatRequestParam;
import cn.lsnu.vote.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;



    // 微信登录授权
    @PostMapping("code2Session")
    public Result<User> code2Session(@RequestBody WeChatRequestParam requestParam){
        if (BeanUtil.isEmpty(requestParam))
            throw new CustomerException(Constants.ERROR_PARAM,"参数不合法");
        return Result.success(authService.weChatAuth(requestParam));
    }
}
