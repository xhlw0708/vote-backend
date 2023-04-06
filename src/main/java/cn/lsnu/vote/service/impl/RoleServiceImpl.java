package cn.lsnu.vote.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lsnu.vote.model.domain.Role;
import cn.lsnu.vote.service.RoleService;
import cn.lsnu.vote.mapper.RoleMapper;
import org.springframework.stereotype.Service;

/**
* @author LindaMan
* @description 针对表【role(角色表)】的数据库操作Service实现
* @createDate 2023-04-04 10:40:09
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService{

}




