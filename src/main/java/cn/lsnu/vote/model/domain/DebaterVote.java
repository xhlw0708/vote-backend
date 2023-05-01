package cn.lsnu.vote.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 辩手投票
 * @TableName debater_vote
 */
@TableName(value ="debater_vote")
@Data
public class DebaterVote implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 正辩-小组id
     */
    @TableField(value = "debate_right")
    private Long debateRight;

    /**
     * 反辩-小组id
     */
    @TableField(value = "debate_left")
    private Long debateLeft;

    /**
     * 剩余时间 - 分
     */
    @TableField(value = "remain_time")
    private Integer remainTime;

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
     * 状态
     */
    @TableField(value = "status")
    private Integer status;


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

    /**
     * 创建人-用户id
     */
    @TableField(value = "create_by")
    private Long createBy;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}