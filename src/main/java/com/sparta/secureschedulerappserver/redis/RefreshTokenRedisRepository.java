package com.sparta.secureschedulerappserver.redis;

import jakarta.annotation.Resource;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
public class RefreshTokenRedisRepository {
    private static final long EXPIRE_TIME = 24 * 60 * 60; // 24시간



    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> valueOperations;

    public void save(String key, String value) {
        valueOperations.set(key, value, EXPIRE_TIME, TimeUnit.SECONDS);
    }

    public String findByKey(String key) {
        return valueOperations.get(key);
    }

    public void updateAccessToken(String oldAccessToken, String newAccessToken) {
        String refreshToken = valueOperations.get(oldAccessToken);
        Long expireTime = valueOperations.getOperations().getExpire(oldAccessToken);

        if (expireTime > 0){
            valueOperations.set(newAccessToken, refreshToken, expireTime, TimeUnit.SECONDS);
        }

        valueOperations.getOperations().delete(oldAccessToken);
    }


    public Boolean existsByKey(String key) {
        return valueOperations.getOperations().hasKey(key);
    }

    public void delete(String key) {
        valueOperations.getOperations().delete(key);
    }
}
