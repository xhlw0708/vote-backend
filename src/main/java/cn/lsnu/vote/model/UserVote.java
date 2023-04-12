package cn.lsnu.vote.model;

import lombok.Data;

@Data
public class UserVote {

    // 辩手名称
    private String name;

    // 辩手得票
    private Long vote;

    // 辩手所在小组
    private String groupName;

}
