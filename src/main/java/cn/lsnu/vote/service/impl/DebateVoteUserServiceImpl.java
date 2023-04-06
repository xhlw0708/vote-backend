package cn.lsnu.vote.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.lsnu.vote.common.Constants;
import cn.lsnu.vote.exception.CustomerException;
import cn.lsnu.vote.mapper.DebateVoteUserMapper;
import cn.lsnu.vote.model.domain.DebateVoteUser;
import cn.lsnu.vote.service.DebateVoteUserService;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LindaMan
 * @description 针对表【debate_vote_user(辩论-用户投票记录表)】的数据库操作Service实现
 * @createDate 2023-04-04 10:40:09
 */
@Service
@RequiredArgsConstructor
public class DebateVoteUserServiceImpl extends ServiceImpl<DebateVoteUserMapper, DebateVoteUser>
        implements DebateVoteUserService {

    private final DebateVoteUserMapper debateVoteUserMapper;

    /**
     * 保存debateVoteUser
     *
     * @param debateVoteUser 实体对象
     * @return groupId
     */
    @Override
    public DebateVoteUser saveDebateVoteUser(DebateVoteUser debateVoteUser) {
        // 判断参数是否合法
        if (ObjectUtil.isNull(debateVoteUser))
            throw new CustomerException(Constants.ERROR_PARAM, "无效参数");

        // 获取当前轮次
        Integer voteParentVersion = debateVoteUser.getVoteParentVersion();
        Integer voteChildrenVersion = debateVoteUser.getVoteChildrenVersion();


        // 判断参数有无id
        // 对debateVote进行判断是否有id
        if (ObjectUtil.isNotNull(debateVoteUser.getId())) {
            // 有id，表示当前用户是已经具备投票的条件的
            // 判断userId是否存在
            List<DebateVoteUser> debateVoteUserList = debateVoteUserMapper.selectList(Wrappers.lambdaQuery(DebateVoteUser.class)
                    .eq(DebateVoteUser::getUserId, debateVoteUser.getUserId()));
            if (debateVoteUserList != null && debateVoteUserList.size() > 0) {
                // 存在，修改数据
                LambdaUpdateWrapper<DebateVoteUser> updateWrapper = Wrappers.lambdaUpdate(DebateVoteUser.class)
                        .eq(DebateVoteUser::getDebateVoteId, debateVoteUser.getDebateVoteId())
                        .eq(DebateVoteUser::getUserId, debateVoteUser.getUserId());
                debateVoteUserMapper.update(debateVoteUser, updateWrapper);
            } else {
                // 不存在，新增数据
                insertDebateVoteUser(debateVoteUser, voteParentVersion, voteChildrenVersion);
            }
        } else {
            // 无id，新增数据
            insertDebateVoteUser(debateVoteUser, voteParentVersion, voteChildrenVersion);
        }
        // 返回当前debateVote的id
        return debateVoteUser;
    }

    private void insertDebateVoteUser(DebateVoteUser debateVoteUser, Integer voteParentVersion, Integer voteChildrenVersion) {
        // 判断是否应该新增 判断本轮是第几小轮
        if (voteChildrenVersion != 1) {
            // 不是第一小轮
            // 判断当前用户是否在第一小轮投过票了
            LambdaUpdateWrapper<DebateVoteUser> wrapper = Wrappers.lambdaUpdate(DebateVoteUser.class)
                    .eq(DebateVoteUser::getUserId, debateVoteUser.getUserId())
                    .eq(DebateVoteUser::getVoteParentVersion, voteParentVersion)
                    .eq(DebateVoteUser::getVoteChildrenVersion, 1);
            DebateVoteUser one = debateVoteUserMapper.selectOne(wrapper);
            if (ObjectUtil.isNull(one)) {
                // 没有，抛出异常
                throw new CustomerException(Constants.ERROR_OTHER, "没有资格进行投票");
            }
        }
        debateVoteUserMapper.insert(debateVoteUser);
    }
}




