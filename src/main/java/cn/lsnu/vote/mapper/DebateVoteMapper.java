package cn.lsnu.vote.mapper;

import cn.lsnu.vote.model.GroupVote;
import cn.lsnu.vote.model.domain.DebateVote;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author LindaMan
* @description 针对表【debate_vote(辩论投票)】的数据库操作Mapper
* @createDate 2023-04-04 10:40:09
* @Entity cn.lsnu.vote.model.domain.DebateVote
*/
public interface DebateVoteMapper extends BaseMapper<DebateVote> {

    List<GroupVote> selectGroupVoteByVersion(@Param("voteParentVersion") int voteParentVersion,
                                             @Param("voteChildrenVersion") int voteChildrenVersion,
                                             @Param("debateVoteId") Long debateVoteId);

}




