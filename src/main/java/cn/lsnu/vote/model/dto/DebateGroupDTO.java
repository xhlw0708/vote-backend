package cn.lsnu.vote.model.dto;

import cn.lsnu.vote.model.domain.DebateGroup;
import cn.lsnu.vote.model.domain.User;
import lombok.Data;

import java.util.List;

@Data
public class DebateGroupDTO extends DebateGroup {

    // 当前小组下所拥有的用户
    private List<User> groupUserList;
}
