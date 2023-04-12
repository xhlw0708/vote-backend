package cn.lsnu.vote.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.lsnu.vote.common.Constants;
import cn.lsnu.vote.common.Result;
import cn.lsnu.vote.exception.CustomerException;
import cn.lsnu.vote.model.domain.DebaterVote;
import cn.lsnu.vote.model.dto.DebaterVoteDTO;
import cn.lsnu.vote.service.DebaterVoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("debaterVotes")
@RequiredArgsConstructor
public class DebaterVoteController {
    private final DebaterVoteService debaterVoteService;


    // 根据voteParentVersion,voteChildrenVersion查询DebateVoteDTO
    @GetMapping("/{voteParentVersion}/{voteChildrenVersion}")
    public Result<DebaterVoteDTO> debateVoteDTOByVersion(
            @PathVariable("voteParentVersion") Long voteParentVersion, @PathVariable("voteChildrenVersion") Long voteChildrenVersion){
        if (ObjectUtil.isNull(voteParentVersion) || ObjectUtil.isNull(voteChildrenVersion))
            throw new CustomerException(Constants.ERROR_PARAM,"参数不合法");
        return Result.success(debaterVoteService.searchDebaterVoteDTOByVersion(voteParentVersion,voteChildrenVersion));
    }


    // 根据id删除投票纪录
    @DeleteMapping("/{id}")
    public Result<String> deleteDebateVoteById(@PathVariable("id") Long id){
        if (ObjectUtil.isNull(id))
            throw new CustomerException(Constants.ERROR_PARAM,"参数不合法");
        return Result.success(debaterVoteService.removeByDebaterVoteId(id));
    }

    // 查询所有投票记录
//    @GetMapping
//    public Result<List<DebateVoteDTO>> debateVoteDTOList(){
//        return Result.success(debateVoteService.searchDebateVoteDTOList());
//    }

    // 根据id查询辩手投票
    @GetMapping("/{id}")
    public Result<DebaterVoteDTO> getDebateVoteById(@PathVariable("id") Long id){
        if (ObjectUtil.isNull(id))
            throw new CustomerException(Constants.ERROR_PARAM,"id不合法");
        return Result.success(debaterVoteService.searchDebaterVoteById(id));
    }

    // 保存辩手投票记录
    @PostMapping
    public Result<Long> saveDebateVote(@RequestBody DebaterVote debaterVote){
        if (ObjectUtil.isNull(debaterVote))
            throw new CustomerException(Constants.ERROR_PARAM,"参数不合法");
        return Result.success(debaterVoteService.saveDebaterVote(debaterVote));
    }

}
