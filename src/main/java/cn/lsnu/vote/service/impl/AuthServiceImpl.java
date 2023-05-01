package cn.lsnu.vote.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import cn.lsnu.vote.common.Constants;
import cn.lsnu.vote.common.RedisConstants;
import cn.lsnu.vote.exception.CustomerException;
import cn.lsnu.vote.mapper.UserMapper;
import cn.lsnu.vote.model.domain.User;
import cn.lsnu.vote.model.wechat.WeChatLoginUser;
import cn.lsnu.vote.model.wechat.WeChatRequestParam;
import cn.lsnu.vote.service.AuthService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
     * 退出登录
     * @param openId
     * @return 提示信息
     */
    @Override
    public String logout(String openid) {
        // 清除redis缓存
        try {
            redisTemplate.delete(RedisConstants.USER_KEY + openid);
        } catch (Exception e) {
            throw new CustomerException(Constants.ERROR_SYSTEM,"退出失败，请稍后再试");
        }

        return "退出成功";
    }

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
            String username = (String) redisTemplate.opsForHash()
                    .get(RedisConstants.USER_KEY + openid, "username");
            if (openid.equals(username) && StrUtil.isNotBlank(username)){
                // redis中有数据
                String tmpid = redisTemplate.opsForHash().get(RedisConstants.USER_KEY + openid, "id").toString();
                user.setId(Long.parseLong(tmpid));
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

            // 查询数据库中是否存在用户
            LambdaQueryWrapper<User> eq = Wrappers.lambdaQuery(User.class).eq(User::getUsername, weChatLoginUser.getOpenid());
            User dbUser = userMapper.selectOne(eq);
            if (BeanUtil.isNotEmpty(dbUser)){
                return dbUser;
            }

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
