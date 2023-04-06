package cn.lsnu.vote.controller;

import cn.lsnu.vote.common.Result;
import cn.lsnu.vote.model.domain.DebateGroup;
import cn.lsnu.vote.service.DebateGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("groups")
@RequiredArgsConstructor
public class GroupController {

    private final DebateGroupService groupService;

    // 查询所有小组列表
    @GetMapping
    public Result<List<DebateGroup>> groups(){
        return Result.success(groupService.searchGroupList());
    }
}
