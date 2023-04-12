package cn.lsnu.vote.service;

import cn.lsnu.vote.model.domain.User;
import cn.lsnu.vote.model.dto.DebateDTO;
import cn.lsnu.vote.model.dto.UserDTO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author LindaMan
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2023-04-04 10:40:09
*/
public interface UserService extends IService<User> {

    // 将用户从当前小组剔除
    String removeGroupById(Long userId);

    // 保存用户信息
    UserDTO saveUser(User user);

    // 查询所有用户列表
    List<UserDTO> searchUserDTOList();

    // 根据id删除用户
    String deleteById(Long id);

    // 根据id查询用户信息
    UserDTO searchById(Long id);

    // 根据用户id查询用户创建的投票
    DebateDTO searchDebateList(Long id);

    // 根据ids批量删除
    String deleteBatch(List<Long> ids);
}
