package cn.lsnu.vote.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class DebateDTO {

    // 用户创建的辩论投票列表
    private List<DebateVoteDTO> debateVoteDTOList;

    // 用户创建的辩手投票列表
    private List<DebaterVoteDTO> debaterVoteDTOList;
}
