package cn.lsnu.vote.service.impl;

import cn.lsnu.vote.mapper.DebateGroupMapper;
import cn.lsnu.vote.model.domain.DebateGroup;
import cn.lsnu.vote.service.DebateGroupService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    /**
     * 查询所有小组列表
     * @return
     */
    @Override
    public List<DebateGroup> searchGroupList() {
        return groupMapper.selectGroupList();
    }
}




