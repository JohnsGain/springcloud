package com.john.flink.demo;

import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-12-15 01:37
 * @since jdk17
 */
public class ProcessFunctionTest {

    @Test
    public void eventTime() {
        Instant instant = Instant.now().plus(-1, ChronoUnit.HOURS);
        System.out.println(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()));
        long eventTime = instant.toEpochMilli();
        long durationMsec = Duration.ofHours(1).toMillis();
        long endOfWindow = (eventTime - (eventTime % durationMsec) + durationMsec - 1);
        LocalDateTime from = LocalDateTime.ofInstant(Instant.ofEpochMilli(endOfWindow), ZoneId.systemDefault());
        System.out.println(from);
    }

}
