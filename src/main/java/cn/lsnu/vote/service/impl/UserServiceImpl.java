package cn.lsnu.vote.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.lsnu.vote.common.Constants;
import cn.lsnu.vote.exception.CustomerException;
import cn.lsnu.vote.mapper.*;
import cn.lsnu.vote.model.domain.*;
import cn.lsnu.vote.model.dto.DebateDTO;
import cn.lsnu.vote.model.dto.DebateVoteDTO;
import cn.lsnu.vote.model.dto.DebaterVoteDTO;
import cn.lsnu.vote.model.dto.UserDTO;
import cn.lsnu.vote.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2023-04-04 10:40:09
*/
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    private final UserMapper userMapper;
    private final DebateGroupMapper debateGroupMapper;
    private final DebateVoteMapper debateVoteMapper;
    private final DebaterVoteMapper debaterVoteMapper;
    private final DebateVoteUserMapper debateVoteUserMapper;


    /**
     * 根据ids批量删除
     * @param ids id集合
     * @return 提示信息
     */
    @Transactional
    @Override
    public String deleteBatch(List<Long> ids) {
        if (ObjectUtil.isNull(ids) && ids.size() < 0)
            throw new CustomerException(Constants.ERROR_PARAM,"参数不合法");

        try {
            userMapper.deleteBatchIds(ids);
        } catch (Exception e) {
            throw new CustomerException(Constants.ERROR_SYSTEM,"操作失败，请稍后尝试");
        }

        return "删除成功";
    }

    /**
     * 根据用户id查询用户创建的投票
     * @param id 用户id
     * @return DebateDTO
     */
    @Override
    public DebateDTO searchDebateList(Long id) {
        // 判断参数是否合法
        if (ObjectUtil.isNull(id))
            throw new CustomerException(Constants.ERROR_PARAM,"id不合法");

        DebateDTO debateDTO = new DebateDTO();
        // 封装debateVoteDTOList
        LambdaQueryWrapper<DebateVote> debateVoteWrapper = Wrappers.lambdaQuery(DebateVote.class).eq(DebateVote::getCreateBy, id);
        List<DebateVote> debateVoteList = debateVoteMapper.selectList(debateVoteWrapper);
        List<DebateVoteDTO> debateVoteDTOList = debateVoteList.stream().map(debateVote -> getDebateVoteDTO(debateVote))
                .collect(Collectors.toList());
        debateVoteList.stream().map(debateVote -> getDebateVoteDTO(debateVote))
                        .collect(Collectors.toList());
        debateDTO.setDebateVoteDTOList(debateVoteDTOList);

        // 封装debaterVoteDTOList
        LambdaQueryWrapper<DebaterVote> debaterVoteWrapper = Wrappers.lambdaQuery(DebaterVote.class).eq(DebaterVote::getCreateBy, id);
        List<DebaterVote> debaterVoteList = debaterVoteMapper.selectList(debaterVoteWrapper);
        List<DebaterVoteDTO> debaterVoteDTOList = debaterVoteList.stream().map(debaterVote -> getDebaterVoteDTO(debaterVote))
                .collect(Collectors.toList());
        debateDTO.setDebaterVoteDTOList(debaterVoteDTOList);

        return debateDTO;
    }

    private DebaterVoteDTO getDebaterVoteDTO(DebaterVote debaterVote) {
        // 转换DTO
        DebaterVoteDTO dto = new DebaterVoteDTO();
        BeanUtil.copyProperties(debaterVote,dto);
        // 封装debateRightUserList
        LambdaQueryWrapper<User> rightWrapper = Wrappers.lambdaQuery(User.class)
                .eq(User::getGroupId, debaterVote.getDebateRight());
        List<User> rightUserList = Optional.ofNullable(userMapper.selectList(rightWrapper))
                .orElse(new ArrayList<>());
        List<UserDTO> rightUserDTOList = rightUserList.stream().map(user -> getUserDTO(user)).collect(Collectors.toList());
        dto.setDebateRightUserList(rightUserDTOList);

        // 封装debateLeftUserList
        LambdaQueryWrapper<User> leftWrapper = Wrappers.lambdaQuery(User.class)
                .eq(User::getGroupId, debaterVote.getDebateLeft());
        List<User> leftUserList = Optional.ofNullable(userMapper.selectList(leftWrapper))
                .orElse(new ArrayList<>());
        List<UserDTO> leftUserDTOList = leftUserList.stream().map(user -> getUserDTO(user)).collect(Collectors.toList());
        dto.setDebateLeftUserList(leftUserDTOList);
        return dto;
    }

    private DebateVoteDTO getDebateVoteDTO(DebateVote debateVote) {
        // 封装DTO
        // 封装debateGroupList
        DebateVoteDTO debateVoteDTO = new DebateVoteDTO();
        BeanUtil.copyProperties(debateVote,debateVoteDTO);
        List<Long> debateGroupIds = Arrays.asList(debateVote.getDebateRight(), debateVote.getDebateLeft());
        List<DebateGroup> debateGroupList = debateGroupIds.stream().map(groupId -> debateGroupMapper.selectById(groupId)).collect(Collectors.toList());
        debateVoteDTO.setDebateGroupList(debateGroupList);

        // 封装DebateVoteUserRight
        Long debateVoteId = debateVote.getId();
        LambdaQueryWrapper<DebateVoteUser> rightWrapper = Wrappers.lambdaQuery(DebateVoteUser.class)
                .eq(DebateVoteUser::getDebateVoteId, debateVoteId)
                .eq(DebateVoteUser::getVoteParentVersion, debateVote.getVoteParentVersion())
                .eq(DebateVoteUser::getVoteChildrenVersion, debateVote.getVoteChildrenVersion())
                .eq(DebateVoteUser::getGroupId, debateVote.getDebateRight());
        List<DebateVoteUser> debateVoteUserRightList = Optional.ofNullable(debateVoteUserMapper.selectList(rightWrapper))
                .orElse(new ArrayList<>());

        // 封装DebateVoteUserRLeft
        LambdaQueryWrapper<DebateVoteUser> leftWrapper = Wrappers.lambdaQuery(DebateVoteUser.class)
                .eq(DebateVoteUser::getDebateVoteId, debateVoteId)
                .eq(DebateVoteUser::getVoteParentVersion, debateVote.getVoteParentVersion())
                .eq(DebateVoteUser::getVoteChildrenVersion, debateVote.getVoteChildrenVersion())
                .eq(DebateVoteUser::getGroupId, debateVote.getDebateLeft());
        List<DebateVoteUser> debateVoteUserLeftList = Optional.ofNullable(debateVoteUserMapper.selectList(leftWrapper))
                .orElse(new ArrayList<>());

        debateVoteDTO.setDebateVoteUserRight(debateVoteUserRightList);
        debateVoteDTO.setDebateVoteUserLeft(debateVoteUserLeftList);

        return debateVoteDTO;
    }

    /**
     * 根据id查询用户信息
     * @param id 用户id
     * @return userDTO
     */
    @Override
    public UserDTO searchById(Long id) {
        // 判断参数是否合法
        if (ObjectUtil.isNull(id))
            throw new CustomerException(Constants.ERROR_PARAM,"id不合法");

        User user = userMapper.selectById(id);
        // 判断用户是否存在
        if (ObjectUtil.isNull(user))
            throw new CustomerException(Constants.ERROR_SYSTEM,"用户不存在");

        UserDTO userDTO = getUserDTO(user);
        return userDTO;
    }

    /**
     * 根据id删除用户
     * @param id 用户id
     * @return 提示信息
     */
    @Transactional
    @Override
    public String deleteById(Long id) {
        // 判断参数是否合法
        if (ObjectUtil.isNull(id))
            throw new CustomerException(Constants.ERROR_PARAM,"参数不合法");

        try {
            // 删除用户
            userMapper.deleteById(id);
        } catch (Exception e) {
            throw new CustomerException(Constants.ERROR_SYSTEM,"删除失败，请稍后再试");
        }
        return "操作成功";
    }

    /**
     * 查询所有用户列表
     * @return List<UserDTO>
     */
    @Override
    public List<UserDTO> searchUserDTOList() {
        // 构造条件构造器
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery(User.class).ne(User::getGroupId, 0)
                .orderByAsc(User::getCreateTime);
        // 查询用户列表
        List<User> userList = Optional.ofNullable(userMapper.selectList(wrapper)).orElse(new ArrayList<>());

        // 封装DTO
        List<UserDTO> userDTOList = userList.stream().map(user -> getUserDTO(user)).collect(Collectors.toList());

        return userDTOList;
    }

    /**
     * 保存用户信息
     * @param user 需要保存的用户信息
     * @return UserDTO
     */
    @Transactional
    @Override
    public UserDTO saveUser(User user) {
        int count = 0;
        // 参数校验
        if (ObjectUtil.isNull(user))
            throw new CustomerException(Constants.ERROR_PARAM,"参数不合法");

        // 判断是否有id
        Long id = user.getId();
        try {
            if (ObjectUtil.isNotNull(id)){
                // 有id，修改
                count = userMapper.updateById(user);
            }else{
                // 无id，新增
                count = userMapper.insert(user);
            }
        } catch (Exception e) {
            throw new CustomerException(Constants.ERROR_SYSTEM,"操作失败，请稍后再试");
        }
        if (count <= 0)
            throw new CustomerException(Constants.ERROR_SYSTEM,"操作失败，请稍后再试");

        // 返回结果信息
        UserDTO userDTO = getUserDTO(user);

        return userDTO;
    }

    // 转换DTO
    private UserDTO getUserDTO(User user) {
        // 转换DTO
        UserDTO dto = new UserDTO();
        BeanUtil.copyProperties(user, dto);
        //封装debateGroup
        DebateGroup debateGroup = debateGroupMapper.selectById(user.getGroupId());
        dto.setDebateGroup(debateGroup);
        return dto;
    }

    /**
     * 将用户从当前小组剔除
     * @param userId 用户id
     * @return 提示信息
     */
    @Transactional
    @Override
    public String removeGroupById(Long userId) {
        // 判断参数是否合法
        if (ObjectUtil.isNull(userId))
            throw new CustomerException(Constants.ERROR_PARAM,"参数不合法");

        try {
            // 删除
            userMapper.removeGroupIdById(userId);
        } catch (Exception e) {
            throw new CustomerException(Constants.ERROR_SYSTEM,"删除失败，请稍后再试");
        }

        return "操作成功";
    }
}




