package cn.lsnu.vote.service;

import cn.lsnu.vote.model.domain.DebateGroup;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author LindaMan
* @description 针对表【group(小组表)】的数据库操作Service
* @createDate 2023-04-04 10:40:09
*/
public interface DebateGroupService extends IService<DebateGroup> {

    List<DebateGroup> searchGroupList();
}
