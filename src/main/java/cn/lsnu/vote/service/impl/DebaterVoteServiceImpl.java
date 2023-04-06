package cn.lsnu.vote.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lsnu.vote.model.domain.DebaterVote;
import cn.lsnu.vote.service.DebaterVoteService;
import cn.lsnu.vote.mapper.DebaterVoteMapper;
import org.springframework.stereotype.Service;

/**
* @author LindaMan
* @description 针对表【debater_vote(辩手投票)】的数据库操作Service实现
* @createDate 2023-04-04 10:40:09
*/
@Service
public class DebaterVoteServiceImpl extends ServiceImpl<DebaterVoteMapper, DebaterVote>
    implements DebaterVoteService{

}




