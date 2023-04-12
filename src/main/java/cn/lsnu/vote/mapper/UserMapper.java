package cn.lsnu.vote.mapper;

import cn.lsnu.vote.model.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author LindaMan
* @description 针对表【user(用户表)】的数据库操作Mapper
* @createDate 2023-04-04 10:40:09
* @Entity cn.lsnu.vote.model.domain.User
*/
public interface UserMapper extends BaseMapper<User> {

    int removeGroupIdByUserId(List<Long> userIds);

    int removeGroupIdById(Long id);
}




