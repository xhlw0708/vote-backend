package cn.lsnu.vote.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.lsnu.vote.common.Constants;
import cn.lsnu.vote.common.Result;
import cn.lsnu.vote.exception.CustomerException;
import cn.lsnu.vote.model.domain.DebateVote;
import cn.lsnu.vote.model.dto.DebateVoteDTO;
import cn.lsnu.vote.service.DebateVoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("debateVotes")
@RequiredArgsConstructor
@Slf4j
public class DebateVoteController {

    private final DebateVoteService debateVoteService;

    // 改变状态
    @GetMapping("changeStatus/{id}")
    public Result<String> changeStatus(@PathVariable Long id){
        if (BeanUtil.isEmpty(id))
            throw new CustomerException(Constants.ERROR_PARAM,"参数不合法");
        return Result.success(debateVoteService.changeStatus(id));
    }


    // 查询投票状态
    @GetMapping("status/{id}")
    public Result<Integer> debateVoteStatus(@PathVariable Long id){
        if (BeanUtil.isEmpty(id))
            throw new CustomerException(Constants.ERROR_PARAM,"参数不合法");
        return Result.success(debateVoteService.searchDebateVoteStatus(id));
    }

    // 根据场次查询所有投票信息
    @GetMapping("debateVoteList/{voteParentVersion}")
    public Result<List<DebateVoteDTO>> debateVoteDTOListByParentVersion(@PathVariable("voteParentVersion") Integer voteParentVersion){
        if (ObjectUtil.isNull(voteParentVersion))
            throw new CustomerException(Constants.ERROR_PARAM,"参数不合法");
        return Result.success(debateVoteService.searchDebateVoteDTOListByParentVersion(voteParentVersion));
    }


    // 根据id删除投票纪录
    @DeleteMapping("/{id}")
    public Result<String> deleteDebateVoteById(@PathVariable("id") Long id){
        if (ObjectUtil.isNull(id))
            throw new CustomerException(Constants.ERROR_PARAM,"参数不合法");
        return Result.success(debateVoteService.removeByDebateVoteId(id));
    }


    // 根据voteParentVersion,voteChildrenVersion查询DebateVoteDTO
    @GetMapping("/{voteParentVersion}/{voteChildrenVersion}")
    public Result<DebateVoteDTO> debateVoteDTOByVersion(
            @PathVariable("voteParentVersion") Long voteParentVersion, @PathVariable("voteChildrenVersion") Long voteChildrenVersion){
        if (ObjectUtil.isNull(voteParentVersion) || ObjectUtil.isNull(voteChildrenVersion))
            throw new CustomerException(Constants.ERROR_PARAM,"参数不合法");
        return Result.success(debateVoteService.searchDebateVoteDTOByVersion(voteParentVersion, voteChildrenVersion));
    }


    // 查询所有投票记录
    @GetMapping
    public Result<List<DebateVoteDTO>> debateVoteDTOList(){
        return Result.success(debateVoteService.searchDebateVoteDTOList());
    }

    // 根据id查询辩论投票
    @GetMapping("/{id}")
    public Result<DebateVoteDTO> getDebateVoteById(@PathVariable("id") Long id){
        return Result.success(debateVoteService.searchDebateVoteById(id));
    }

    // 保存辩论投票记录
    @PostMapping
    public Result<Long> saveDebateVote(@RequestBody DebateVote debateVote){
        if (ObjectUtil.isNull(debateVote))
            throw new CustomerException(Constants.ERROR_PARAM,"无效参数");
        return Result.success(debateVoteService.saveDebateVote(debateVote));
    }

}
