package cn.lsnu.vote.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.lsnu.vote.common.Constants;
import cn.lsnu.vote.exception.CustomerException;
import cn.lsnu.vote.mapper.DebaterVoteMapper;
import cn.lsnu.vote.mapper.DebaterVoteUserMapper;
import cn.lsnu.vote.model.domain.DebaterVote;
import cn.lsnu.vote.model.domain.DebaterVoteUser;
import cn.lsnu.vote.service.DebaterVoteUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final DebaterVoteMapper debaterVoteMapper;


    /**
     * 保存debaterVoteUser
     * @param debateVoterUser 辩手用户投票信息
     * @return DebateVoteUser
     */
    @Transactional
    @Override
    public DebaterVoteUser saveDebaterVoteUser(DebaterVoteUser debaterVoteUser) {
        // 判断参数是否合法
        if (ObjectUtil.isNull(debaterVoteUser))
            throw new CustomerException(Constants.ERROR_PARAM, "无效参数");

        // 判断投票状态
        Long debaterVoteId = debaterVoteUser.getDebaterVoteId();
        DebaterVote debaterVote = debaterVoteMapper.selectById(debaterVoteId);
        if (BeanUtil.isEmpty(debaterVote))
            throw new CustomerException(Constants.ERROR_SYSTEM,"暂无投票信息");
        Integer status = debaterVote.getStatus();
        debaterVoteUser.setDebaterVoteStatus(status);
        if (status == 0)
            return debaterVoteUser;


        // 获取当前轮次
        Integer voteParentVersion = debaterVoteUser.getVoteParentVersion();
        Integer voteChildrenVersion = debaterVoteUser.getVoteChildrenVersion();

        // 判断当前用户是否在当前场次和轮次投过票了
        LambdaQueryWrapper<DebaterVoteUser> eq = Wrappers.lambdaQuery(DebaterVoteUser.class)
                .eq(DebaterVoteUser::getDebaterVoteId, debaterVoteUser.getDebaterVoteId())
                .eq(DebaterVoteUser::getUserId, debaterVoteUser.getUserId())
                .eq(DebaterVoteUser::getVoteParentVersion, debaterVoteUser.getVoteParentVersion())
                .eq(DebaterVoteUser::getVoteChildrenVersion, debaterVoteUser.getVoteChildrenVersion());
        DebaterVoteUser debateVoteUser1 = debaterVoteUserMapper.selectOne(eq);
        if (BeanUtil.isNotEmpty(debateVoteUser1)){
            // 不为空，表示已经投过票了，只需更新即可
            LambdaUpdateWrapper<DebaterVoteUser> updateWrapper = Wrappers.lambdaUpdate(DebaterVoteUser.class)
                    .eq(DebaterVoteUser::getDebaterVoteId, debaterVoteUser.getDebaterVoteId())
                    .eq(DebaterVoteUser::getUserId, debaterVoteUser.getUserId())
                    .eq(DebaterVoteUser::getVoteParentVersion, debaterVoteUser.getVoteParentVersion())
                    .eq(DebaterVoteUser::getVoteChildrenVersion, debaterVoteUser.getVoteChildrenVersion());
            debaterVoteUserMapper.update(debaterVoteUser,updateWrapper);
        }else {
            // 不存在，新增数据
            insertDebateVoteUser(debaterVoteUser, voteParentVersion, voteChildrenVersion);
        }
        // 返回当前debateVote的id
        return debaterVoteUser;
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




