package cn.lsnu.vote.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.lsnu.vote.common.Constants;
import cn.lsnu.vote.exception.CustomerException;
import cn.lsnu.vote.mapper.DebateGroupMapper;
import cn.lsnu.vote.mapper.DebateVoteMapper;
import cn.lsnu.vote.model.domain.DebateGroup;
import cn.lsnu.vote.model.domain.DebateVote;
import cn.lsnu.vote.model.dto.DebateVoteDTO;
import cn.lsnu.vote.service.DebateVoteService;
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

    private DebateVoteDTO getDebateVoteDTO(DebateVote debateVote) {
        // 封装DTO
        DebateVoteDTO debateVoteDTO = new DebateVoteDTO();
        BeanUtil.copyProperties(debateVote,debateVoteDTO);
        List<Long> debateGroupIds = Arrays.asList(debateVote.getDebateRight(), debateVote.getDebateLeft());
        List<DebateGroup> debateGroupList = debateGroupIds.stream().map(groupId -> debateGroupMapper.selectById(groupId)).collect(Collectors.toList());
        debateVoteDTO.setDebateGroupList(debateGroupList);
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




