package com.qiying.voice_txt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qiying.voice_txt.domain.VoiceTxt;
import com.qiying.voice_txt.mapper.VoiceTxtMapper;
import com.qiying.voice_txt.service.VoiceTxtService;
import com.qiying.voice_txt.utils.GetMessageUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author deqing
 * @description 针对表【voice_txt(外呼云转义明细)】的数据库操作Service实现
 * @createDate 2023-08-30 10:46:22
 */
@Slf4j
@Service
public class VoiceTxtServiceImpl extends ServiceImpl<VoiceTxtMapper, VoiceTxt>
        implements VoiceTxtService {
    @Resource(name = "single")
    ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Resource
    GetMessageUtils getMessageUtils;
    private static final Long oneSecond = 1000L;
    private final ReentrantLock lock = new ReentrantLock();
    private static Boolean isStop = false;

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

    @Override
    public boolean startGetMessage() {
        List<VoiceTxt> noViceList = this.getNoViceList();
        Iterator<VoiceTxt> iterator = noViceList.iterator();
        threadPoolTaskExecutor.execute(() -> getMessage(iterator));
        return true;
    }

    @Override
    public void start() {
        synchronized (lock) {
            isStop = false;
            lock.notifyAll();
        }
    }

    @Override
    public synchronized void stop() {
        isStop = true;
    }

    public Boolean getIsStop() {
        return VoiceTxtServiceImpl.isStop;
    }

    private void getMessage(Iterator<VoiceTxt> iterator) {
        try {
            while (iterator.hasNext()) {
                long start = System.currentTimeMillis();
                VoiceTxt next = iterator.next();
                if (next == null) {
                    continue;
                }
                log.info("run id:" + next.getId());
                if (StringUtils.isNotBlank(next.getCallId()) && StringUtils.isNotBlank(next.getTenantId())) {
                    String message = getMessageUtils.getMessage(next);
                    if (StringUtils.isNotBlank(message)) {
                        next.setMessage(message);
                        this.updateById(next);
                        log.info("update id:" + next.getId());
                    }
                }
                long end = System.currentTimeMillis();
                long l = oneSecond - (end - start);
                if (l > 0) {
                    Thread.sleep(l);
                }
                if (this.getIsStop()) {
                    synchronized (lock) {
                        log.info("Thread wait");
                        lock.wait();
                        log.info("Thread awakening");
                    }
                }
            }
        } catch (Exception e) {
            log.error("some error:", e);
        }
    }
}




