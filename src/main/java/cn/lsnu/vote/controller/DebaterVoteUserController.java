package cn.lsnu.vote.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.lsnu.vote.common.Constants;
import cn.lsnu.vote.common.Result;
import cn.lsnu.vote.exception.CustomerException;
import cn.lsnu.vote.model.domain.DebaterVoteUser;
import cn.lsnu.vote.service.DebaterVoteUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("debaterVoteUsers")
@RequiredArgsConstructor
public class DebaterVoteUserController {

    private final DebaterVoteUserService debaterVoteUserService;


    // 保存debaterVoteUser
    @PostMapping
    public Result<DebaterVoteUser> saveDebateVoteUsers(@RequestBody DebaterVoteUser debaterVoteUser){
        if (ObjectUtil.isNull(debaterVoteUser))
            throw new CustomerException(Constants.ERROR_PARAM,"参数不合法");
        return Result.success(debaterVoteUserService.saveDebaterVoteUser(debaterVoteUser));
    }
}
