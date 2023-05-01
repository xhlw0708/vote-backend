package cn.lsnu.vote.service;

import cn.lsnu.vote.model.domain.DebateVote;
import cn.lsnu.vote.model.dto.DebateVoteDTO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author LindaMan
* @description 针对表【debate_vote(辩论投票)】的数据库操作Service
* @createDate 2023-04-04 10:40:09
*/
public interface DebateVoteService extends IService<DebateVote> {

    // 保存辩论投票
    Long saveDebateVote(DebateVote debateVote);

    // 根据id查询辩论投票
    DebateVoteDTO searchDebateVoteById(Long id);

    // 查询所有投票记录
    List<DebateVoteDTO> searchDebateVoteDTOList();

    // 根据voteParentVersion,voteChildrenVersion查询DebateVoteDTO
    DebateVoteDTO searchDebateVoteDTOByVersion(Long voteParentVersion, Long voteChildrenVersion);

    // 根据id删除投票纪录
    String removeByDebateVoteId(Long debateVoteId);

    // 根据场次查询所有投票信息
    List<DebateVoteDTO> searchDebateVoteDTOListByParentVersion(Integer voteParentVersion);

    // 查询投票状态
    Integer searchDebateVoteStatus(Long id);

    // 改变状态
    String changeStatus(Long id);
}
