package cn.lsnu.vote.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.lsnu.vote.common.Constants;
import cn.lsnu.vote.exception.CustomerException;
import cn.lsnu.vote.mapper.DebateGroupMapper;
import cn.lsnu.vote.mapper.UserMapper;
import cn.lsnu.vote.model.domain.DebateGroup;
import cn.lsnu.vote.model.domain.User;
import cn.lsnu.vote.model.dto.DebateGroupDTO;
import cn.lsnu.vote.service.DebateGroupService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
* @author LindaMan
* @description 针对表【group(小组表)】的数据库操作Service实现
* @createDate 2023-04-04 10:40:09
*/
@Service
@RequiredArgsConstructor
public class GroupServiceImpl extends ServiceImpl<DebateGroupMapper, DebateGroup>
    implements DebateGroupService {

    private final DebateGroupMapper groupMapper;
    private final UserMapper userMapper;

    /**
     * 保存小组信息
     * @param debateGroup 要保存小组信息
     * @return 小组信息
     */
    @Transactional
    @Override
    public String saveDebateGroup(DebateGroup debateGroup) {
        int count = 0;
        // 参数校验
        if (ObjectUtil.isNull(debateGroup))
            throw new CustomerException(Constants.ERROR_PARAM,"参数不合法");

        // 判断是否有id
        Long id = debateGroup.getId();
        try {
            if (ObjectUtil.isNotNull(id)){
                // 有id，修改
                count = groupMapper.updateById(debateGroup);
            }else{
                // 无id，新增
                count = groupMapper.insert(debateGroup);
            }
        } catch (Exception e) {
            throw new CustomerException(Constants.ERROR_SYSTEM,"操作失败，请稍后再试");
        }
        if (count <= 0)
            throw new CustomerException(Constants.ERROR_SYSTEM,"操作失败，请稍后再试");

        // 返回结果信息
        return "保存成功";
    }

    /**
     * 根据id删除小组
     * @param id 小组id
     * @return 提示信息
     */
    @Transactional
    @Override
    public String deleteById(Long id) {
        // 判断参数是否合法
        if (ObjectUtil.isNull(id))
            throw new CustomerException(Constants.ERROR_PARAM,"参数不合法");

        // 查询符合条件的用户
        LambdaQueryWrapper<User> userWrapper = Wrappers.lambdaQuery(User.class).eq(User::getGroupId, id);
        List<User> userList = userMapper.selectList(userWrapper);
        try {
            if (userList != null && userList.size() > 0){
                List<Long> ids = userList.stream().map(user -> user.getId()).collect(Collectors.toList());
                // 有用户在该小组
                // 消除用户的groupId
                userMapper.removeGroupIdByUserId(ids);
            }
            // 删除小组
            groupMapper.deleteById(id);
        } catch (Exception e) {
            throw new CustomerException(Constants.ERROR_SYSTEM,"删除失败，请稍后再试");
        }
        return "操作成功";
    }

    /**
     * 查询所有小组列表
     * @return
     */
    @Override
    public List<DebateGroup> searchGroupList() {
        return groupMapper.selectGroupList();
    }

    /**
     * 查询所有小组列表以及小组下面的用户
     * @return DebateGroupDTO
     */
    @Override
    public List<DebateGroupDTO> searchGroupDTOList() {
        List<DebateGroup> debateGroupList =Optional.ofNullable(groupMapper.selectGroupList())
                .orElse(new ArrayList<>());
        List<DebateGroupDTO> debateGroupDTOList = debateGroupList.stream().map(group -> getDebateGroupDTO(group))
                .collect(Collectors.toList());

        return debateGroupDTOList;
    }

    /**
     * 转换DTO
     * @param group 小组
     * @return 小组DTO
     */
    private DebateGroupDTO getDebateGroupDTO(DebateGroup group) {
        // 转换DTO
        DebateGroupDTO dto = new DebateGroupDTO();
        BeanUtil.copyProperties(group, dto);
        //封装groupUserList
        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery(User.class).eq(User::getGroupId, group.getId());
        List<User> userList = Optional.ofNullable(userMapper.selectList(queryWrapper))
                .orElse(new ArrayList<>());
        dto.setGroupUserList(userList);
        return dto;
    }
}




