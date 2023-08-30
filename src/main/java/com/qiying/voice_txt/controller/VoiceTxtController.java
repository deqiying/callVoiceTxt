package com.qiying.voice_txt.controller;

import com.qiying.voice_txt.service.VoiceTxtService;
import jakarta.annotation.Resource;
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
            return "thread start!";
        } else {
            return "thread fail!";
        }
    }
}
