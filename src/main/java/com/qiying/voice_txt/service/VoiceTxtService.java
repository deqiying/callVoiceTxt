package com.qiying.voice_txt.service;

import com.qiying.voice_txt.domain.VoiceTxt;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author deqing
* @description 针对表【voice_txt(外呼云转义明细)】的数据库操作Service
* @createDate 2023-08-30 10:46:22
*/
public interface VoiceTxtService extends IService<VoiceTxt> {
    List<VoiceTxt> getNoViceList();

    boolean startGetMessage();

    void start();

    void stop();

    void setSleepTime(Long timeMillis);

}
