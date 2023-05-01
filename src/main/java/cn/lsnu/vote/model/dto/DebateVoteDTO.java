package cn.lsnu.vote.model.dto;

import cn.lsnu.vote.model.domain.DebateGroup;
import cn.lsnu.vote.model.domain.DebateVote;
import cn.lsnu.vote.model.domain.DebateVoteUser;
import lombok.Data;

import java.util.List;

@Data
public class DebateVoteDTO extends DebateVote {

    // 辩论小组
    private List<DebateGroup> debateGroupList;

    // 该投票记录的右方票数信息
    private List<DebateVoteUser> DebateVoteUserRight;

    // 该投票记录的左方票数信息
    private List<DebateVoteUser> DebateVoteUserLeft;

}
