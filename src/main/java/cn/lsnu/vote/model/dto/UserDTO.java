package cn.lsnu.vote.model.dto;

import cn.lsnu.vote.model.domain.DebateGroup;
import cn.lsnu.vote.model.domain.User;
import lombok.Data;

@Data
public class UserDTO extends User {

    // 用户所在的小组信息
    private DebateGroup debateGroup;
}
