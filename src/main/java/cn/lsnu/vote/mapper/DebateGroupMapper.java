package cn.lsnu.vote.mapper;

import cn.lsnu.vote.model.domain.DebateGroup;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author LindaMan
* @description 针对表【group(小组表)】的数据库操作Mapper
* @createDate 2023-04-04 10:40:09
* @Entity cn.lsnu.vote.model.domain.Group
*/
public interface DebateGroupMapper extends BaseMapper<DebateGroup> {

    List<DebateGroup> selectGroupList();
}




