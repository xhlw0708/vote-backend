package cn.lsnu.vote.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lsnu.vote.model.domain.DebaterVoteUser;
import cn.lsnu.vote.service.DebaterVoteUserService;
import cn.lsnu.vote.mapper.DebaterVoteUserMapper;
import org.springframework.stereotype.Service;

/**
* @author LindaMan
* @description 针对表【debater_vote_user(辩手-用户投票记录表)】的数据库操作Service实现
* @createDate 2023-04-04 10:40:09
*/
@Service
public class DebaterVoteUserServiceImpl extends ServiceImpl<DebaterVoteUserMapper, DebaterVoteUser>
    implements DebaterVoteUserService{

}




