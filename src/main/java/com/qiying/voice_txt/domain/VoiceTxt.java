package com.qiying.voice_txt.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 外呼云转义明细
 * @TableName voice_txt
 */
@TableName(value ="voice_txt")
@Data
public class VoiceTxt implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 公司代码
     */
    private String tenantId;

    /**
     * 呼叫id
     */
    private String callId;

    /**
     * 邀请时间
     */
    private String inviteDate;

    /**
     * 消息文本
     */
    private String message;

}