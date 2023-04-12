package cn.lsnu.vote;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import cn.lsnu.vote.common.RedisConstants;
import cn.lsnu.vote.mapper.DebateGroupMapper;
import cn.lsnu.vote.mapper.DebateVoteMapper;
import cn.lsnu.vote.model.domain.DebateVote;
import cn.lsnu.vote.model.domain.User;
import cn.lsnu.vote.model.wechat.WeChatLoginUser;
import cn.lsnu.vote.service.DebateGroupService;
import cn.lsnu.vote.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;

@SpringBootTest
class VoteBackendApplicationTests {

    @Resource
    private UserService userService;
    @Resource
    private DebateGroupMapper groupMapper;
    @Resource
    private DebateGroupService groupService;
    @Resource
    private DebateVoteMapper debateVoteMapper;


    @Test
    void RedisConstantsTest(){
        System.out.println(RedisConstants.USER_KEY);
    }

    @Test
    void weixinLogin(){
        // 91933c3a22369f8f1fb6ade8fb66e160
        // openid：oSPjK4pfPfqeKttYWg7gPrc48ySk
        HttpResponse response = HttpRequest.get("https://api.weixin.qq.com/sns/jscode2session" +
                        "?appid=wxda9bed06c4087d2e" +
                        "&secret=91933c3a22369f8f1fb6ade8fb66e160" +
                        "&js_code=0f3yH2000HYpLP1Flx200dazHT0yH20M" +
                        "&grant_type=authorization_code")
                .execute();
        String body = response.body();
        WeChatLoginUser weChatLoginUser = JSONUtil.toBean(body, WeChatLoginUser.class);
        System.out.println(weChatLoginUser);
    }

    @Test
    void addUserTest(){
        String[] names = new String[]{"张三","李四","王五","蔡鲲","王二","李华"};
        String[] usernames = new String[]{"zhangsan","lisi","wangwu","ikun","wanger","lihua"};
        HashSet<User> set = new HashSet<>();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 3; j++) {
                User user = new User();
                user.setUsername(usernames[i] + j);
                user.setPassword("123");
                user.setName(names[i] + j);
                user.setPhone("15698532654");
                user.setGroupId((long) i);
                set.add(user);
            }
        }
        userService.saveBatch(set);

    }


    @Test
    void contextLoads() {
//        List<DebateGroup> list = groupService.list();
//        System.out.println(list);
        List<DebateVote> debateVotes = debateVoteMapper.selectList(null);
        System.out.println(debateVotes);
    }

}
