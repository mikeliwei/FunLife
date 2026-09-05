package com.hmdp.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Component
public class RedisWorker {
    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final long BASE_TIMESTAMP = 1767225600L;

    public long nextId(String keyPrefix) {
        long nowTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        long timestamp = nowTime - BASE_TIMESTAMP;
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        long count = redisTemplate.opsForValue().increment("icr:" + keyPrefix + ":" + date);

        return  timestamp << 32 | count;
    }

//    public static void main(String[] args) {
//        LocalDateTime localDateTime = LocalDateTime.of(2026, 1, 1, 0, 0, 0);
//        System.out.println(localDateTime.toEpochSecond(ZoneOffset.UTC));
//    }
}
