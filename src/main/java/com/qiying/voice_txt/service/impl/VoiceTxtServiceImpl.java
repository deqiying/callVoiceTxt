package com.qiying.voice_txt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qiying.voice_txt.domain.VoiceTxt;
import com.qiying.voice_txt.mapper.VoiceTxtMapper;
import com.qiying.voice_txt.service.VoiceTxtService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author deqing
* @description 针对表【voice_txt(外呼云转义明细)】的数据库操作Service实现
* @createDate 2023-08-30 10:46:22
*/
@Service
public class VoiceTxtServiceImpl extends ServiceImpl<VoiceTxtMapper, VoiceTxt>
    implements VoiceTxtService {

    @Override
    public List<VoiceTxt> getNoViceList() {
        QueryWrapper<VoiceTxt> wrapper = new QueryWrapper<>();
        wrapper.isNotNull("call_id").isNull("message").orderByAsc("id").last("limit 1");
        VoiceTxt voiceTxt = this.getBaseMapper().selectOne(wrapper);
        Long minId = voiceTxt.getId() == null ? 1 : voiceTxt.getId();
        wrapper = new QueryWrapper<>();
        wrapper.ge("id", minId).orderByAsc("id");
        return this.getBaseMapper().selectList(wrapper);
    }
}




