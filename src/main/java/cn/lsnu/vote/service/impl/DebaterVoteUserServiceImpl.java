package cn.lsnu.vote.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.lsnu.vote.common.Constants;
import cn.lsnu.vote.exception.CustomerException;
import cn.lsnu.vote.mapper.DebaterVoteUserMapper;
import cn.lsnu.vote.model.domain.DebaterVoteUser;
import cn.lsnu.vote.service.DebaterVoteUserService;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author LindaMan
* @description 针对表【debater_vote_user(辩手-用户投票记录表)】的数据库操作Service实现
* @createDate 2023-04-04 10:40:09
*/
@Service
@RequiredArgsConstructor
public class DebaterVoteUserServiceImpl extends ServiceImpl<DebaterVoteUserMapper, DebaterVoteUser>
    implements DebaterVoteUserService{

    private final DebaterVoteUserMapper debaterVoteUserMapper;


    /**
     * 保存debaterVoteUser
     * @param debateVoterUser 辩手用户投票信息
     * @return DebateVoteUser
     */
    @Transactional
    @Override
    public DebaterVoteUser saveDebaterVoteUser(DebaterVoteUser debateVoterUser) {
        // 判断参数是否合法
        if (ObjectUtil.isNull(debateVoterUser))
            throw new CustomerException(Constants.ERROR_PARAM, "无效参数");

        // 获取当前轮次
        Integer voteParentVersion = debateVoterUser.getVoteParentVersion();
        Integer voteChildrenVersion = debateVoterUser.getVoteChildrenVersion();


        // 判断参数有无id
        // 对debateVote进行判断是否有id
        if (ObjectUtil.isNotNull(debateVoterUser.getId())) {
            // 有id，表示当前用户是已经具备投票的条件的
            // 判断userId是否存在
            List<DebaterVoteUser> debaterVoteUserList = debaterVoteUserMapper.selectList(Wrappers.lambdaQuery(DebaterVoteUser.class)
                    .eq(DebaterVoteUser::getUserId, debateVoterUser.getUserId()));
            if (debaterVoteUserList != null && debaterVoteUserList.size() > 0) {
                // 存在，修改数据
                LambdaUpdateWrapper<DebaterVoteUser> updateWrapper = Wrappers.lambdaUpdate(DebaterVoteUser.class)
                        .eq(DebaterVoteUser::getDebaterVoteId, debateVoterUser.getDebaterVoteId())
                        .eq(DebaterVoteUser::getUserId, debateVoterUser.getUserId());
                debaterVoteUserMapper.update(debateVoterUser, updateWrapper);
            } else {
                // 不存在，新增数据
                insertDebateVoteUser(debateVoterUser, voteParentVersion, voteChildrenVersion);
            }
        } else {
            // 无id，新增数据
            insertDebateVoteUser(debateVoterUser, voteParentVersion, voteChildrenVersion);
        }
        // 返回当前debateVote的id
        return debateVoterUser;
    }

    private void insertDebateVoteUser(DebaterVoteUser debaterVoteUser, Integer voteParentVersion, Integer voteChildrenVersion) {
        // 判断是否应该新增 判断本轮是第几小轮
        if (voteChildrenVersion != 1) {
            // 不是第一小轮
            // 判断当前用户是否在第一小轮投过票了
            LambdaUpdateWrapper<DebaterVoteUser> wrapper = Wrappers.lambdaUpdate(DebaterVoteUser.class)
                    .eq(DebaterVoteUser::getUserId, debaterVoteUser.getUserId())
                    .eq(DebaterVoteUser::getVoteParentVersion, voteParentVersion)
                    .eq(DebaterVoteUser::getVoteChildrenVersion, 1);
            DebaterVoteUser one = debaterVoteUserMapper.selectOne(wrapper);
            if (ObjectUtil.isNull(one)) {
                // 没有，抛出异常
                throw new CustomerException(Constants.ERROR_OTHER, "没有资格进行投票");
            }
        }
        debaterVoteUserMapper.insert(debaterVoteUser);
    }
}




