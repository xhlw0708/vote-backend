package cn.lsnu.vote.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.lsnu.vote.common.Constants;
import cn.lsnu.vote.common.Result;
import cn.lsnu.vote.exception.CustomerException;
import cn.lsnu.vote.model.domain.DebateGroup;
import cn.lsnu.vote.model.dto.DebateGroupDTO;
import cn.lsnu.vote.service.DebateGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("groups")
@RequiredArgsConstructor
public class GroupController {

    private final DebateGroupService groupService;


    // 保存小组信息
    @PostMapping
    public Result<String> saveGroup(@RequestBody DebateGroup debateGroup){
        if (ObjectUtil.isNull(debateGroup))
            throw new CustomerException(Constants.ERROR_PARAM,"参数不合法");
        return Result.success(groupService.saveDebateGroup(debateGroup));
    }


    // 根据id删除小组
    @DeleteMapping("/{id}")
    public Result<String> deleteById(@PathVariable("id") Long id){
        if (ObjectUtil.isNull(id))
            throw new CustomerException(Constants.ERROR_PARAM,"参数不合法");
        return Result.success(groupService.deleteById(id));
    }

    // 查询所有小组列表
    @GetMapping
    public Result<List<DebateGroup>> groups(){
        return Result.success(groupService.searchGroupList());
    }

    // 查询所有小组列表以及小组下面的用户
    @GetMapping("/dto")
    public Result<List<DebateGroupDTO>> groupDTOs(){
        return Result.success(groupService.searchGroupDTOList());
    }

}
