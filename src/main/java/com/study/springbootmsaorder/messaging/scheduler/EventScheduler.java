package com.study.springbootmsaorder.messaging.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.study.springbootmsaorder.messaging.processor.EventDeleteProcessor;
import com.study.springbootmsaorder.messaging.processor.EventRetryProcessor;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EventScheduler {

    private final EventDeleteProcessor eventDeleteProcessor;
    private final EventRetryProcessor eventRetryProcessor;

    /***
     * 자정마다 완료된 이벤트 삭제 스케줄링
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void scheduleCompletedEventDelete() {
        eventDeleteProcessor.processCompletedEventDelete();
    }

    /***
     * 5분마다 실패한 이벤트 재시도 스케줄링
     */
    @Scheduled(cron = "0 0/5 * * * *")
    public void scheduleFailedEventRetry() {
        eventRetryProcessor.processFailedEventRetry();
    }
}
