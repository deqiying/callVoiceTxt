package com.qiying.voice_txt.utils;

import com.alibaba.fastjson.JSON;
import com.qiying.voice_txt.domain.VoiceTxt;
import com.qiying.voice_txt.domain.request.VoiceBody;
import com.qiying.voice_txt.domain.response.VoiceResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.http.*;
import org.springframework.http.converter.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author qiying
 * @version 1.0
 * @description TODO
 * @since 2023/8/30 14:04
 */
@Component
public class GetMessageUtils {
    private final RestTemplate restTemplate = new RestTemplate();

    @PostConstruct
    public void initRestTemplate() {
        List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();

        // 移除默认的 MappingJackson2HttpMessageConverter
        converters.removeIf(converter -> converter instanceof MappingJackson2HttpMessageConverter);
        converters.removeIf(converter -> true);
        converters.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
        converters.add(new MappingJackson2HttpMessageConverter());
        converters.add(new ByteArrayHttpMessageConverter());
        converters.add(new ResourceHttpMessageConverter());
        converters.add(new FormHttpMessageConverter());

        restTemplate.setMessageConverters(converters);
    }

    public String getMessage(VoiceTxt voiceTxt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        VoiceBody voiceBody = new VoiceBody();
        voiceBody.setTenantId(voiceTxt.getTenantId());
        voiceBody.setSipId(voiceTxt.getCallId());
        String postBody = JSON.toJSONString(voiceBody);
        HttpEntity<String> entity = new HttpEntity<>(postBody, headers);
        ResponseEntity<VoiceResponse> response;
        String url = "https://ccc.gacmotor.com/open/cdp/voice/txt";
        response = restTemplate.exchange(url, HttpMethod.POST, entity, VoiceResponse.class);
        VoiceResponse body = response.getBody();
        if (body == null) {
            return null;
        }
        Object result = body.getResult();
        if (result == null) {
            return null;
        }
        return result.toString();
    }

}
