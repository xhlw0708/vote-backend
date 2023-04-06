package cn.lsnu.vote.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lsnu.vote.model.domain.UserRole;
import cn.lsnu.vote.service.UserRoleService;
import cn.lsnu.vote.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;

/**
* @author LindaMan
* @description 针对表【user_role(用户-角色表)】的数据库操作Service实现
* @createDate 2023-04-04 10:40:09
*/
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
    implements UserRoleService{

}




