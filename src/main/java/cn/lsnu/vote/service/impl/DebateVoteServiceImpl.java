package cn.lsnu.vote.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.lsnu.vote.common.Constants;
import cn.lsnu.vote.exception.CustomerException;
import cn.lsnu.vote.mapper.DebateGroupMapper;
import cn.lsnu.vote.mapper.DebateVoteMapper;
import cn.lsnu.vote.mapper.DebateVoteUserMapper;
import cn.lsnu.vote.model.domain.DebateGroup;
import cn.lsnu.vote.model.domain.DebateVote;
import cn.lsnu.vote.model.domain.DebateVoteUser;
import cn.lsnu.vote.model.dto.DebateVoteDTO;
import cn.lsnu.vote.service.DebateVoteService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
* @author LindaMan
* @description 针对表【debate_vote(辩论投票)】的数据库操作Service实现
* @createDate 2023-04-04 10:40:09
*/
@Service
@RequiredArgsConstructor
public class DebateVoteServiceImpl extends ServiceImpl<DebateVoteMapper, DebateVote>
    implements DebateVoteService{

    private final DebateVoteMapper debateVoteMapper;
    private final DebateGroupMapper debateGroupMapper;
    private final DebateVoteUserMapper debateVoteUserMapper;


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
        DebateVote debateVote = debateVoteMapper.selectById(id);
        if (BeanUtil.isEmpty(debateVote))
            throw new CustomerException(Constants.ERROR_SYSTEM,"暂无投票信息");
        if (debateVote.getStatus() == 1){
            debateVote.setStatus(0);
        }else {
            debateVote.setStatus(1);
        }
        try {
            debateVoteMapper.updateById(debateVote);
        } catch (Exception e) {
            throw new CustomerException(Constants.ERROR_SYSTEM,"操作失败，请稍后再试");
        }
        return "操作成功";
    }

    /**
     * 查询投票状态
     * @param id debateVoteId
     * @return 提示信息
     */
    @Override
    public Integer searchDebateVoteStatus(Long id) {
        if (BeanUtil.isEmpty(id))
            throw new CustomerException(Constants.ERROR_PARAM,"参数不合法");
        DebateVote debateVote = debateVoteMapper.selectById(id);
        if (BeanUtil.isEmpty(debateVote)){
            throw new CustomerException(Constants.ERROR_PARAM,"暂无该投票信息");
        }

        return debateVote.getStatus();
    }

    /**
     * 根据场次查询所有投票信息
     * @param voteParentVersion 投票场次
     * @return List<DebateVoteDTO>
     */
    @Override
    public List<DebateVoteDTO> searchDebateVoteDTOListByParentVersion(Integer voteParentVersion) {
        // 校验参数是否合法
        if (ObjectUtil.isNull(voteParentVersion))
            throw new CustomerException(Constants.ERROR_PARAM,"参数不合法");

        // 封装查询条件
        LambdaQueryWrapper<DebateVote> eq = Wrappers.lambdaQuery(DebateVote.class).eq(DebateVote::getVoteParentVersion, voteParentVersion);

        // 查询
        List<DebateVote> debateVoteList = Optional.ofNullable(debateVoteMapper.selectList(eq)).orElse(new ArrayList<>());

        // 转换DTO
        List<DebateVoteDTO> debateVoteDTOList = debateVoteList.stream().map(v -> getDebateVoteDTO(v)).collect(Collectors.toList());

        return debateVoteDTOList;
    }

    /**
     * 根据id删除投票纪录
     * @param debateVoteId 辩论投票id
     * @return 提示信息
     */
    @Transactional
    @Override
    public String removeByDebateVoteId(Long debateVoteId) {
        // 判断参数是否合法
        if (ObjectUtil.isNull(debateVoteId))
            throw new CustomerException(Constants.ERROR_PARAM,"参数不合法");

        try {
            // 根据id删除debate_vote表的记录
            debateVoteMapper.deleteById(debateVoteId);

            // 根据debateVoteId删除debate_vote_user表中的记录
            LambdaQueryWrapper<DebateVoteUser> queryWrapper = Wrappers.lambdaQuery(DebateVoteUser.class)
                    .eq(DebateVoteUser::getDebateVoteId, debateVoteId);
            debateVoteUserMapper.delete(queryWrapper);

        } catch (Exception e) {
            throw new CustomerException(Constants.ERROR_SYSTEM,"操作失败，请稍后再试");
        }
        // 返回提示信息
        return "删除成功";
    }

    private DebateVoteDTO getDebateVoteDTO(DebateVote debateVote) {
        // 封装DTO
        // 封装debateGroupList
        DebateVoteDTO debateVoteDTO = new DebateVoteDTO();
        BeanUtil.copyProperties(debateVote,debateVoteDTO);
        List<Long> debateGroupIds = Optional.ofNullable(Arrays.asList(debateVote.getDebateRight(), debateVote.getDebateLeft()))
                .orElse(new ArrayList<>());
        List<DebateGroup> debateGroupList = debateGroupIds.stream().map(groupId -> debateGroupMapper.selectById(groupId)).collect(Collectors.toList());
        debateVoteDTO.setDebateGroupList(debateGroupList);

        // 封装DebateVoteUserRight
        Long debateVoteId = debateVote.getId();
        LambdaQueryWrapper<DebateVoteUser> rightWrapper = Wrappers.lambdaQuery(DebateVoteUser.class)
                .eq(DebateVoteUser::getDebateVoteId, debateVoteId)
                .eq(DebateVoteUser::getVoteParentVersion, debateVote.getVoteParentVersion())
                .eq(DebateVoteUser::getVoteChildrenVersion, debateVote.getVoteChildrenVersion())
                .eq(DebateVoteUser::getGroupId, debateVote.getDebateRight());
        List<DebateVoteUser> debateVoteUserRightList = Optional.ofNullable(debateVoteUserMapper.selectList(rightWrapper))
                .orElse(new ArrayList<>());

        // 封装DebateVoteUserRLeft
        LambdaQueryWrapper<DebateVoteUser> leftWrapper = Wrappers.lambdaQuery(DebateVoteUser.class)
                .eq(DebateVoteUser::getDebateVoteId, debateVoteId)
                .eq(DebateVoteUser::getVoteParentVersion, debateVote.getVoteParentVersion())
                .eq(DebateVoteUser::getVoteChildrenVersion, debateVote.getVoteChildrenVersion())
                .eq(DebateVoteUser::getGroupId, debateVote.getDebateLeft());
        List<DebateVoteUser> debateVoteUserLeftList = Optional.ofNullable(debateVoteUserMapper.selectList(leftWrapper))
                .orElse(new ArrayList<>());

        debateVoteDTO.setDebateVoteUserRight(debateVoteUserRightList);
        debateVoteDTO.setDebateVoteUserLeft(debateVoteUserLeftList);

        return debateVoteDTO;
    }


    /**
     * 根据voteParentVersion,voteChildrenVersion查询DebateVoteDTO
     * @param voteParentVersion 辩论场次
     * @param voteChildrenVersion 场次的轮次
     * @return DebateVoteDTO
     */
    @Override
    public DebateVoteDTO searchDebateVoteDTOByVersion(Long voteParentVersion, Long voteChildrenVersion) {
        // 判断参数
        if (ObjectUtil.isNull(voteParentVersion) || ObjectUtil.isNull(voteChildrenVersion))
            throw new CustomerException(Constants.ERROR_PARAM,"参数不合法");

        // 封装查询条件
        LambdaQueryWrapper<DebateVote> queryWrapper = Wrappers.lambdaQuery(DebateVote.class)
                .eq(DebateVote::getVoteParentVersion, voteParentVersion)
                .eq(DebateVote::getVoteChildrenVersion, voteChildrenVersion);

        // 查询debateVote
        DebateVote debateVote = debateVoteMapper.selectOne(queryWrapper);

        // 转换DTO
        DebateVoteDTO debateVoteDTO = getDebateVoteDTO(debateVote);

        // 返回结果信息
        return debateVoteDTO;
    }

    /**
     * 查询所有投票记录
     * @return List<DebateVoteDTO> 辩论投票记录列表
     */
    @Override
    public List<DebateVoteDTO> searchDebateVoteDTOList() {
        List<DebateVote> debateVotes = Optional.ofNullable(debateVoteMapper.selectList(null)).orElse(new ArrayList<>());
        List<DebateVoteDTO> debateVoteDTOList = debateVotes.stream().map(debateVote -> getDebateVoteDTO(debateVote)).collect(Collectors.toList());
        debateVoteDTOList = Optional.ofNullable(debateVoteDTOList).orElse(new ArrayList<>());
        return debateVoteDTOList;
    }

    /**
     * 根据id查询辩论投票
     * @param id
     * @return DebateVote
     */
    @Override
    public DebateVoteDTO searchDebateVoteById(Long id) {
        // 检验id是否有效
        if (ObjectUtil.isNull(id))
            throw new CustomerException(Constants.ERROR_PARAM,"id不合法");

        // 根据id查询debateVote
        DebateVote debateVote = Optional.ofNullable(debateVoteMapper.selectById(id)).orElse(new DebateVote());

        DebateVoteDTO debateVoteDTO = getDebateVoteDTO(debateVote);

        return debateVoteDTO;
    }


    /**
     * 保存辩论投票记录
     * @param debateVote 辩论投票实体
     * @return 辩论投票id
     */
    @Transactional
    @Override
    public Long saveDebateVote(DebateVote debateVote) {
        // 对参数进行检验
        if (ObjectUtil.isNull(debateVote))
            throw new CustomerException(Constants.ERROR_PARAM,"无效参数");

        try {
            // 对debateVote进行判断是否有id
            if (ObjectUtil.isNotNull(debateVote.getId())){
                // 有id，修改数据
                debateVoteMapper.updateById(debateVote);
            }else {
                // 无id，新增数据
                debateVoteMapper.insert(debateVote);
            }
        } catch (Exception e) {
            throw new CustomerException(Constants.ERROR_SYSTEM,"保存失败，请稍后尝试");
        }

        // 返回当前debateVote的id
        return debateVote.getId();
    }
}




