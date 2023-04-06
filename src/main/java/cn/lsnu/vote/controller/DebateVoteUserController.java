package cn.lsnu.vote.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.lsnu.vote.common.Constants;
import cn.lsnu.vote.common.Result;
import cn.lsnu.vote.exception.CustomerException;
import cn.lsnu.vote.model.domain.DebateVoteUser;
import cn.lsnu.vote.service.DebateVoteUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("debateVoteUsers")
@RequiredArgsConstructor
@Slf4j
public class DebateVoteUserController {

    private final DebateVoteUserService debateVoteUserService;


    // 保存debateVoteUser
    @PostMapping
    public Result<DebateVoteUser> saveDebateVoteUsers(@RequestBody DebateVoteUser debateVoteUser){
        if (ObjectUtil.isNull(debateVoteUser))
            throw new CustomerException(Constants.ERROR_PARAM,"参数不合法");
        return Result.success(debateVoteUserService.saveDebateVoteUser(debateVoteUser));
    }
}
