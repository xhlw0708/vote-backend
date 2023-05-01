package cn.lsnu.vote.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.lsnu.vote.common.Constants;
import cn.lsnu.vote.exception.CustomerException;
import cn.lsnu.vote.mapper.DebateGroupMapper;
import cn.lsnu.vote.mapper.DebaterVoteMapper;
import cn.lsnu.vote.mapper.DebaterVoteUserMapper;
import cn.lsnu.vote.mapper.UserMapper;
import cn.lsnu.vote.model.UserVote;
import cn.lsnu.vote.model.domain.*;
import cn.lsnu.vote.model.dto.DebaterVoteDTO;
import cn.lsnu.vote.model.dto.UserDTO;
import cn.lsnu.vote.service.DebaterVoteService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
* @author LindaMan
* @description 针对表【debater_vote(辩手投票)】的数据库操作Service实现
* @createDate 2023-04-04 10:40:09
*/
@Service
@RequiredArgsConstructor
public class DebaterVoteServiceImpl extends ServiceImpl<DebaterVoteMapper, DebaterVote>
    implements DebaterVoteService{

    private final DebaterVoteMapper debaterVoteMapper;
    private final UserMapper userMapper;
    private final DebateGroupMapper debateGroupMapper;
    private final DebaterVoteUserMapper debaterVoteUserMapper;


    /**
     * 改变状态
     * @param id 辩论id
     * @return 提示信息
     */
    @Transactional
    @Override
    public String changeStatus(Long id) {
        if (BeanUtil.isEmpty(id))
            throw new CustomerException(Constants.ERROR_PARAM,"参数不合法");
        DebaterVote debaterVote = debaterVoteMapper.selectById(id);
        if (BeanUtil.isEmpty(debaterVote))
            throw new CustomerException(Constants.ERROR_SYSTEM,"暂无投票信息");
        if (debaterVote.getStatus() == 1){
            debaterVote.setStatus(0);
        }else {
            debaterVote.setStatus(1);
        }
        try {
            debaterVoteMapper.updateById(debaterVote);
        } catch (Exception e) {
            throw new CustomerException(Constants.ERROR_SYSTEM,"操作失败，请稍后再试");
        }
        return "操作成功";
    }

    /**
     * 根据voteParentVersion,voteChildrenVersion查询DebateVoteDTO
     * @param voteParentVersion 第几场辩论
     * @param voteChildrenVersion 第几场辩论的第几轮投票
     * @return DebaterVoteDTO
     */
    @Override
    public DebaterVoteDTO searchDebaterVoteDTOByVersion(Long voteParentVersion, Long voteChildrenVersion) {
        if (ObjectUtil.isNull(voteParentVersion) || ObjectUtil.isNull(voteChildrenVersion))
            throw new CustomerException(Constants.ERROR_PARAM,"参数不合法");

        // 封装查询条件
        LambdaQueryWrapper<DebaterVote> queryWrapper = Wrappers.lambdaQuery(DebaterVote.class)
                .eq(DebaterVote::getVoteParentVersion, voteParentVersion)
                .eq(DebaterVote::getVoteChildrenVersion, voteChildrenVersion);

        // 查询debaterVote
        DebaterVote debaterVote = debaterVoteMapper.selectOne(queryWrapper);

        // 转换DTO
        DebaterVoteDTO dto = getDebaterVoteDTO(debaterVote);

        // 返回结果信息
        return dto;
    }

    /**
     * 根据id删除投票纪录
     * @param debaterVoteId 辩手投票id
     * @return 提示信息
     */
    @Transactional
    @Override
    public String removeByDebaterVoteId(Long debaterVoteId) {
        // 判断参数是否合法
        if (ObjectUtil.isNull(debaterVoteId))
            throw new CustomerException(Constants.ERROR_PARAM,"参数不合法");

        try {
            // 根据id删除debater_vote表的记录
            debaterVoteMapper.deleteById(debaterVoteId);

            // 根据debaterVoteId删除debater_vote_user表中的记录
            LambdaQueryWrapper<DebaterVoteUser> queryWrapper = Wrappers.lambdaQuery(DebaterVoteUser.class)
                    .eq(DebaterVoteUser::getDebaterVoteId, debaterVoteId);
            debaterVoteUserMapper.delete(queryWrapper);

        } catch (Exception e) {
            throw new CustomerException(Constants.ERROR_SYSTEM,"操作失败，请稍后再试");
        }
        // 返回提示信息
        return "删除成功";
    }

    /**
     * 根据id查询辩手投票
     * @param id 辩手投票id
     * @return DebaterVoteDTO
     */
    @Override
    public DebaterVoteDTO searchDebaterVoteById(Long id) {
        // 检验id是否有效
        if (ObjectUtil.isNull(id))
            throw new CustomerException(Constants.ERROR_PARAM,"id不合法");

        // 根据id查询debateVote
        DebaterVote debaterVote = Optional.ofNullable(debaterVoteMapper.selectById(id)).orElse(new DebaterVote());

        // 转换DTO
        DebaterVoteDTO dto = getDebaterVoteDTO(debaterVote);

        return dto;
    }

    // 转换debaterVoteDTO
    private DebaterVoteDTO getDebaterVoteDTO(DebaterVote debaterVote) {
        // 转换DTO
        DebaterVoteDTO dto = new DebaterVoteDTO();
        BeanUtil.copyProperties(debaterVote,dto);
        // 根据debate_right，debate_left查询所有辩手
        LambdaQueryWrapper<User> rightGroupUserWrapper = Wrappers.lambdaQuery(User.class)
                .eq(User::getGroupId, debaterVote.getDebateRight());
        LambdaQueryWrapper<User> leftGroupUserWrapper = Wrappers.lambdaQuery(User.class)
                .eq(User::getGroupId, debaterVote.getDebateLeft());
        List<User> rightUserList = userMapper.selectList(rightGroupUserWrapper); // 正方用户列表
        List<User> leftUserList = userMapper.selectList(leftGroupUserWrapper); // 反方列表
        List<User> allUserList = Stream.of(rightUserList, leftUserList) // 全部用户
                .flatMap(v -> v.stream()).collect(Collectors.toList());

        // 计算出allUserList中每个用户的票数以及他们的小组
        List<UserVote> userVoteList = allUserList.stream().map(user -> {
            // 根据辩手id查询出票数
            LambdaQueryWrapper<DebaterVoteUser> debaterVoteUserWrapper = Wrappers.lambdaQuery(DebaterVoteUser.class)
                    .eq(DebaterVoteUser::getVoteParentVersion, debaterVote.getVoteParentVersion())
                    .eq(DebaterVoteUser::getVoteChildrenVersion, debaterVote.getVoteChildrenVersion())
                    .eq(DebaterVoteUser::getDebaterVoteId, debaterVote.getId())
                    .eq(DebaterVoteUser::getDebaterId, user.getId());
            Long vote = debaterVoteUserMapper.selectCount(debaterVoteUserWrapper);
            DebateGroup debateGroup = debateGroupMapper.selectById(user.getGroupId());
            UserVote userVote = new UserVote();
            userVote.setName(user.getName());
            userVote.setVote(vote);
            userVote.setGroupName(debateGroup.getName());
            return userVote;
        }).collect(Collectors.toList());

        // 封装userVoteList
        dto.setUserVoteList(userVoteList);

        // 封装debateRightUserList
        List<UserDTO> rightUserDTOList = rightUserList.stream().map(user -> getUserDTO(user)).collect(Collectors.toList());
        dto.setDebateRightUserList(rightUserDTOList);

        // 封装debateLeftUserList
        List<UserDTO> leftUserDTOList = leftUserList.stream().map(user -> getUserDTO(user)).collect(Collectors.toList());
        dto.setDebateLeftUserList(leftUserDTOList);
        return dto;
    }

    // 转换userDTO
    private UserDTO getUserDTO(User user) {
        // 转换DTO
        UserDTO dto = new UserDTO();
        BeanUtil.copyProperties(user, dto);
        //封装debateGroup
        DebateGroup debateGroup = debateGroupMapper.selectById(user.getGroupId());
        dto.setDebateGroup(debateGroup);
        return dto;
    }

    /**
     * 保存辩手投票记录
     * @param debaterVote 辩手投票记录
     * @return 辩手投票记录id
     */
    @Transactional
    @Override
    public Long saveDebaterVote(DebaterVote debaterVote) {
        // 对参数进行检验
        if (ObjectUtil.isNull(debaterVote))
            throw new CustomerException(Constants.ERROR_PARAM,"无效参数");

        try {
            // 对debaterVote进行判断是否有id
            if (ObjectUtil.isNotNull(debaterVote.getId())){
                // 有id，修改数据
                debaterVoteMapper.updateById(debaterVote);
            }else {
                // 无id，新增数据
                debaterVoteMapper.insert(debaterVote);
            }
        } catch (Exception e) {
            throw new CustomerException(Constants.ERROR_SYSTEM,"保存失败，请稍后尝试");
        }

        // 返回当前debateVote的id
        return debaterVote.getId();
    }
}




