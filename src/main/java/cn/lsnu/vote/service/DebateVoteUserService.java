package cn.lsnu.vote.service;

import cn.lsnu.vote.model.domain.DebateVoteUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author LindaMan
* @description 针对表【debate_vote_user(辩论-用户投票记录表)】的数据库操作Service
* @createDate 2023-04-04 10:40:09
*/
public interface DebateVoteUserService extends IService<DebateVoteUser> {

    // 保存debateVoteUser
    DebateVoteUser saveDebateVoteUser(DebateVoteUser debateVoteUser);
}
