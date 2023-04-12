package cn.lsnu.vote.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import cn.lsnu.vote.common.RedisConstants;
import cn.lsnu.vote.mapper.UserMapper;
import cn.lsnu.vote.model.domain.User;
import cn.lsnu.vote.model.wechat.WeChatLoginUser;
import cn.lsnu.vote.model.wechat.WeChatRequestParam;
import cn.lsnu.vote.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final RedisTemplate redisTemplate;
    private final UserMapper userMapper;
    @Value("${app.appid}")
    private String appid;
    @Value("${app.secret}")
    private String secret;


    /**
     * 微信登录授权
     * @param requestParam 登录所需参数
     * @return user 用户信息
     */
    @Transactional
    @Override
    public User weChatAuth(WeChatRequestParam requestParam) {
        User user = new User();
        String openid = requestParam.getOpenid();
        String code = requestParam.getCode();

        if (StrUtil.isNotBlank(openid)){
            // 判断redis中是否存在user
            String username = (String) redisTemplate.opsForHash().get(openid, "username");
            if (openid.equals(username) && StrUtil.isNotBlank(username)){
                // redis中有数据
                Long id = (Long) redisTemplate.opsForHash().get(RedisConstants.USER_KEY + openid, "id");
                user.setId(id);
                user.setUsername(username);
                return user;
            }
        }

        // 发送https请求
        // secret: 91933c3a22369f8f1fb6ade8fb66e160
        String resJson = HttpRequest.get("https://api.weixin.qq.com/sns/jscode2session" +
                        "?appid=" + appid +
                        "&secret=" + secret +
                        "&js_code=" + code +
                        "&grant_type=authorization_code")
                .execute().body();
        WeChatLoginUser weChatLoginUser = JSONUtil.toBean(resJson, WeChatLoginUser.class);
        // 检查是否返回
        if (BeanUtil.isNotEmpty(weChatLoginUser) && StrUtil.isNotBlank(weChatLoginUser.getOpenid())){
            // 授权成功
            // 将登录用户信息持久化
            user.setUsername(weChatLoginUser.getOpenid());
            user.setPassword("123");
            user.setName("访客");
            user.setPhone("12345679820");
            user.setGroupId(0L);
            userMapper.insert(user);

            // 存到redis中
            HashMap<String,Object> hashMap = new HashMap<>();
            BeanUtil.beanToMap(user, hashMap, false, false);
            redisTemplate.opsForHash().putAll(RedisConstants.USER_KEY + weChatLoginUser.getOpenid(),hashMap);
        }
        return user;
    }
}
