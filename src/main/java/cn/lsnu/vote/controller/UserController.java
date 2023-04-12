package cn.lsnu.vote.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.lsnu.vote.common.Constants;
import cn.lsnu.vote.common.Result;
import cn.lsnu.vote.exception.CustomerException;
import cn.lsnu.vote.model.domain.User;
import cn.lsnu.vote.model.dto.DebateDTO;
import cn.lsnu.vote.model.dto.UserDTO;
import cn.lsnu.vote.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    // 批量删除
    @PostMapping("batch")
    public Result<String> deleteBatchByIds(@RequestBody List<Long> ids){
        if (ObjectUtil.isNull(ids) && ids.size() < 0)
            throw new CustomerException(Constants.ERROR_PARAM,"参数不合法");
        return Result.success(userService.deleteBatch(ids));
    }

    // 根据用户id查询用户创建的投票
    @GetMapping("debate/{id}")
    public Result<DebateDTO> getDebateList(@PathVariable("id") Long id){
        if (ObjectUtil.isNull(id))
            throw new CustomerException(Constants.ERROR_PARAM,"id不合法");
        return Result.success(userService.searchDebateList(id));
    }


    // 保存用户信息
    @PostMapping
    public Result<UserDTO> saveGroup(@RequestBody User user){
        if (ObjectUtil.isNull(user))
            throw new CustomerException(Constants.ERROR_PARAM,"参数不合法");
        return Result.success(userService.saveUser(user));
    }


    // 根据id删除用户
    @DeleteMapping("/{id}")
    public Result<String> deleteById(@PathVariable("id") Long id){
        if (ObjectUtil.isNull(id))
            throw new CustomerException(Constants.ERROR_PARAM,"参数不合法");
        return Result.success(userService.deleteById(id));
    }

    // 根据id查询用户信息
    @GetMapping("/{id}")
    public Result<UserDTO> getById(@PathVariable("id") Long id){
        if (ObjectUtil.isNull(id))
            throw new CustomerException(Constants.ERROR_PARAM,"参数不合法");
        return Result.success(userService.searchById(id));
    }


    // 查询所有用户(辩手)列表
    @GetMapping
    public Result<List<UserDTO>> groups(){
        return Result.success(userService.searchUserDTOList());
    }

    // 将用户从当前小组剔除
    @DeleteMapping("group/remove/{userId}")
    public Result<String> removeGroup(@PathVariable("userId") Long userId){
        if (ObjectUtil.isNull(userId))
            throw new CustomerException(Constants.ERROR_PARAM,"参数不合法");
        return Result.success(userService.removeGroupById(userId));
    }
}
