package cn.lsnu.vote.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 辩手-用户投票记录表
 * @TableName debater_vote_user
 */
@TableName(value ="debater_vote_user")
@Data
public class DebaterVoteUser implements Serializable {

    /**
     * 投票状态
     */
    @TableField(exist = false)
    private Integer debaterVoteStatus;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 辩手投票id
     */
    @TableField(value = "debater_vote_id")
    private Long debaterVoteId;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 辩手id-用户
     */
    @TableField(value = "debater_id")
    private Long debaterId;

    /**
     * 第几轮辩论赛
     */
    @TableField(value = "vote_parent_version")
    private Integer voteParentVersion;

    /**
     * 第几轮辩论赛下的每小轮
     */
    @TableField(value = "vote_children_version")
    private Integer voteChildrenVersion;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}