package cn.lsnu.vote.model.dto;

import cn.lsnu.vote.model.UserVote;
import cn.lsnu.vote.model.domain.DebaterVote;
import lombok.Data;

import java.util.List;

@Data
public class DebaterVoteDTO extends DebaterVote {

    // 右方辩论小组中的用户列表
    private List<UserDTO> debateRightUserList;

    // 左方辩论小组中的用户列表
    private List<UserDTO> debateLeftUserList;

    // 辩论两组的辩手
    private List<UserVote> userVoteList;
}
