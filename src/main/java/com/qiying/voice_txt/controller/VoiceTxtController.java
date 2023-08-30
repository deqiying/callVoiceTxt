package com.qiying.voice_txt.controller;

import com.qiying.voice_txt.service.VoiceTxtService;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;


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

    @GetMapping("/message")
    public String message() {
        boolean b = voiceTxtService.startGetMessage();
        if (b) {
            return "get message thread start!";
        } else {
            return "get message thread fail!";
        }
    }

    @GetMapping("/start")
    public void start() {
        voiceTxtService.start();
    }

    @GetMapping("/stop")
    public void stop() {
        voiceTxtService.stop();
    }

    @GetMapping("/setSleepTime")
    public void setSleepTime(@Param("timeMillis") Long timeMillis) {
        voiceTxtService.setSleepTime(timeMillis);
    }
}
