package com.manage.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;

import java.util.concurrent.TimeUnit;

@SpringBootTest
public class RedisTest {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Test
    public void testRedis() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        System.out.println(redisTemplate);
        ValueOperations valueOperations = redisTemplate.opsForValue();
        HashOperations hashOperations = redisTemplate.opsForHash();
        ListOperations listOperations = redisTemplate.opsForList();
        SetOperations setOperations = redisTemplate.opsForSet();

    }
    @Test
    public void testString() {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        ops.set("com.manage.string", "hello world");
        ops.set("code", "23567", 30, TimeUnit.SECONDS);
        String value = (String) ops.get("code");
        System.out.println( value);

    }
    @Test
    public void testHash() {
        HashOperations<String, String, Object> ops = redisTemplate.opsForHash();
        ops.put("66", "name", "zhangsan");
        ops.put("66", "age", 18);

        System.out.println(ops.keys("66"));
    }
}
