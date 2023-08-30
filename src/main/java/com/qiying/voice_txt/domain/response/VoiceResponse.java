package com.qiying.voice_txt.domain.response;

import lombok.Data;

import java.util.List;

/**
 * @author qiying
 * @version 1.0
 * @description TODO
 * @since 2023/8/30 14:25
 */
@Data
public class VoiceResponse {
    private Integer status;
    private String message;
    private Object result;
    private String param;
}
