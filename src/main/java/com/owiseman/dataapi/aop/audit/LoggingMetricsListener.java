package com.owiseman.dataapi.aop.audit;

import com.eatthepath.pushy.apns.ApnsClient;
import com.eatthepath.pushy.apns.ApnsClientMetricsListener;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;


public class LoggingMetricsListener implements ApnsClientMetricsListener {

    private static final Logger log = LoggerFactory.getLogger(LoggingMetricsListener.class);
    private final Counter sucessCounter = Metrics.counter("apns.push.success");
    private final Counter failureCounter = Metrics.counter("apns.push.failure");
    private final Timer latencyTimer = Metrics.timer("apns.push.latency");

    @Override
    public void handleWriteFailure(ApnsClient client, long l) {
        failureCounter.increment();
        log.error("APNs写入失败: {}", l);
    }

    @Override
    public void handleNotificationSent(ApnsClient client, long l) {
        latencyTimer.record(Duration.ofMillis(l));
    }

    @Override
    public void handleNotificationAccepted(ApnsClient apnsClient, long l) {
        sucessCounter.increment();
    }

    @Override
    public void handleNotificationRejected(ApnsClient apnsClient, long l) {

    }

    @Override
    public void handleConnectionAdded(ApnsClient apnsClient) {

    }

    @Override
    public void handleConnectionRemoved(ApnsClient apnsClient) {

    }

    @Override
    public void handleConnectionCreationFailed(ApnsClient apnsClient) {

    }
}
