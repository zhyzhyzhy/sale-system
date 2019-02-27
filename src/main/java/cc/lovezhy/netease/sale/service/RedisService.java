package cc.lovezhy.netease.sale.service;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    private static final Logger log = LoggerFactory.getLogger(RedisService.class);

    private final RedisTemplate<String, String> template;

    private static final Long TTL_COMMON = 3 * 24 * 3600L;

    @Autowired
    public RedisService(RedisTemplate<String, String> template) {
        this.template = template;
    }

    public <T> T getFromCache(String key, Class<T> clazz) {
        String string = template.boundValueOps(key).get();
        if (StringUtils.isEmpty(string)) {
            return null;
        }
        return JSON.parseObject(string, clazz);
    }

    public void removeFromCache(String key) {
        if (StringUtils.isEmpty(key)) {
            return;
        }
        template.delete(key);
    }

    public void removeFromCacheAsync(String key) {
        CompletableFuture.runAsync(() -> template.delete(key))
                .exceptionally(e -> {
                    log.error(e.getMessage(), e);
                    return null;
                });
    }

    public <T> void setToCache(String key, T value, long timeout, TimeUnit timeUnit) {
        String content = JSON.toJSONString(value);
        setToCache(key, content, timeout, timeUnit);
    }

    public <T> void setToCache(String key, T value) {
        setToCache(key, value, TTL_COMMON, TimeUnit.SECONDS);
    }

    public <T> void setToCacheAsync(String key, T value, long timeout, TimeUnit timeUnit) {
        String content = JSON.toJSONString(value);
        setToCacheAsync(key, content, timeout, timeUnit);
    }

    public <T> void setToCacheAsync(String key, T value) {
        setToCacheAsync(key, value, TTL_COMMON, TimeUnit.SECONDS);
    }

    public String getFromCache(String key) {
        return template.boundValueOps(key).get();
    }

    public void setToCache(String key, String value, long timeout, TimeUnit timeUnit) {
        template.boundValueOps(key).set(value, timeout, timeUnit);
    }

    public void setToCacheAsync(String key, String value, long timeout, TimeUnit timeUnit) {
        CompletableFuture.runAsync(() -> template.boundValueOps(key).set(value, timeout, timeUnit))
                .exceptionally(e -> {
                    log.error(e.getMessage(), e);
                    return null;
                });
    }

    public void setToCache(String key, String value) {
        setToCache(key, value, TTL_COMMON, TimeUnit.SECONDS);
    }

    public void setToCacheAsync(String key, String value) {
        setToCacheAsync(key, value, TTL_COMMON, TimeUnit.SECONDS);
    }

    public boolean setToCacheIfAbsent(String key, String value, long timeout, TimeUnit timeUnit) {
        Boolean res = template.execute((RedisCallback<Boolean>) connection -> connection.stringCommands()
                .set(key.getBytes(), value.getBytes(), Expiration.from(timeout, timeUnit), RedisStringCommands.SetOption.SET_IF_ABSENT));
        if (res == null) {
            return false;
        }
        return res;
    }

    public void expandExpireTime(String key, int time, TimeUnit timeUnit) {
        template.boundValueOps(key).expire(time, timeUnit);
    }
}
