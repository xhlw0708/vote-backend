package cn.lsnu.vote.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lsnu.vote.model.domain.User;
import cn.lsnu.vote.service.UserService;
import cn.lsnu.vote.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author LindaMan
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2023-04-04 10:40:09
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




