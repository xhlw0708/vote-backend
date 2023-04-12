package cn.lsnu.vote.service;

import cn.lsnu.vote.model.domain.DebaterVote;
import cn.lsnu.vote.model.dto.DebaterVoteDTO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author LindaMan
* @description 针对表【debater_vote(辩手投票)】的数据库操作Service
* @createDate 2023-04-04 10:40:09
*/
public interface DebaterVoteService extends IService<DebaterVote> {

    // 保存辩手投票记录
    Long saveDebaterVote(DebaterVote debaterVote);

    // 根据id查询辩手投票
    DebaterVoteDTO searchDebaterVoteById(Long id);

    // 根据id删除投票纪录
    String removeByDebaterVoteId(Long debaterVoteId);

    // 根据voteParentVersion,voteChildrenVersion查询DebateVoteDTO
    DebaterVoteDTO searchDebaterVoteDTOByVersion(Long voteParentVersion, Long voteChildrenVersion);
}
