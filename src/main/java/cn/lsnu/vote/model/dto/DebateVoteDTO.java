package cn.lsnu.vote.model.dto;

import cn.lsnu.vote.model.domain.DebateGroup;
import cn.lsnu.vote.model.domain.DebateVote;
import lombok.Data;

import java.util.List;

@Data
public class DebateVoteDTO extends DebateVote {

    // 辩论小组
    private List<DebateGroup> debateGroupList;
}
