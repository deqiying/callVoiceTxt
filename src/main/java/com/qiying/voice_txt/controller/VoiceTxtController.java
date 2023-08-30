package com.qiying.voice_txt.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.qiying.voice_txt.domain.VoiceTxt;
import com.qiying.voice_txt.service.VoiceTxtService;
import com.qiying.voice_txt.utils.GetMessageUtils;
import jakarta.annotation.Resource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;


/**
 * @author qiying
 * @version 1.0
 * @description TODO
 * @since 2023/8/30 11:04
 */
@RestController
@RequestMapping("/api/voice")
public class VoiceTxtController {

    @Resource
    VoiceTxtService voiceTxtService;
    @Resource(name = "single")
    ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Resource
    GetMessageUtils getMessageUtils;

    private static final long oneSecond = 1000L;
    @GetMapping("/message")
    public String message() {
        List<VoiceTxt> noViceList = voiceTxtService.getNoViceList();
        Iterator<VoiceTxt> iterator = noViceList.iterator();
        threadPoolTaskExecutor.execute(()->{
            try {
                while (iterator.hasNext()) {
                    long start = System.currentTimeMillis();
                    VoiceTxt next = iterator.next();
                    if (next == null) {
                        continue;
                    }
                    if (StringUtils.isNotBlank(next.getCallId())&&StringUtils.isNotBlank(next.getTenantId())) {
                        String message = getMessageUtils.getMessage(next);
                        if (StringUtils.isNotBlank(message)) {
                            next.setMessage(message);
                            voiceTxtService.updateById(next);
                        }
                    }
                    System.out.println(next.getId());
                    long end = System.currentTimeMillis();
                    long time = end - start;
                    long l = oneSecond - time;
                    if (l > 0) {
                        Thread.sleep(l);
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
        return "thread start!";
    }


}
