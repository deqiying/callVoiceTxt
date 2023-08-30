package com.qiying.voice_txt.domain.request;

import lombok.Data;

/**
 * @author qiying
 * @version 1.0
 * @description TODO
 * @since 2023/8/30 14:10
 */
@Data
public class VoiceBody {
    private String sipId;
    private String tenantId;
}
