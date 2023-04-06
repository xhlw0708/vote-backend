package cn.lsnu.vote.controller;

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

    // 查询所有投票记录
    @GetMapping
    public Result<List<DebateVoteDTO>> debateVoteDTOList(){
        return Result.success(debateVoteService.searchDebateVoteDTOList());
    }

    // 保存辩论投票记录
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
