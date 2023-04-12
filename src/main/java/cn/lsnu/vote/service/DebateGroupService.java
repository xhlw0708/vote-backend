package cn.lsnu.vote.service;

import cn.lsnu.vote.model.domain.DebateGroup;
import cn.lsnu.vote.model.dto.DebateGroupDTO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author LindaMan
 * @description 针对表【group(小组表)】的数据库操作Service
 * @createDate 2023-04-04 10:40:09
 */
public interface DebateGroupService extends IService<DebateGroup> {

    // 查询所有小组列表
    List<DebateGroup> searchGroupList();

    // 根据id删除小组
    String deleteById(Long id);

    // 保存小组信息
    String saveDebateGroup(DebateGroup debateGroup);

    // 查询所有小组列表以及小组下面的用户
    List<DebateGroupDTO> searchGroupDTOList();
}
