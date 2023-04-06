package cn.lsnu.vote;

import cn.lsnu.vote.mapper.DebateGroupMapper;
import cn.lsnu.vote.mapper.DebateVoteMapper;
import cn.lsnu.vote.model.domain.DebateVote;
import cn.lsnu.vote.model.domain.User;
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
