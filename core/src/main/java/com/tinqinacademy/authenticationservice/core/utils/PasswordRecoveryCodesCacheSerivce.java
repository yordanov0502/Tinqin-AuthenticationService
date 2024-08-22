package com.tinqinacademy.authenticationservice.core.utils;

import com.github.benmanes.caffeine.cache.Cache;
import com.tinqinacademy.authenticationservice.api.exceptions.custom.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PasswordRecoveryCodesCacheSerivce {

    private final CacheManager cacheManager;
    @Value("${env.PASSWORD_RECOVERY_CODES}")
    private String PASSWORD_RECOVERY_CODES;

    private Cache<Object, Object> getCaffeineCache() {
        org.springframework.cache.Cache springCache = cacheManager.getCache(PASSWORD_RECOVERY_CODES);
        if (springCache instanceof CaffeineCache) {
            return ((CaffeineCache) springCache).getNativeCache();
        }
        throw new IllegalStateException("Caffeine cache not found or not properly configured");
    }

    public void addToCache(Object key, Object value) {
        getCaffeineCache().put(key, value);
    }

    public boolean existsInCache(Object key) {
        return getCaffeineCache().asMap().containsKey(key);
    }

    public void removeFromCache(Object key) {
        getCaffeineCache().invalidate(key);
    }

    public Object getKeyForValueInCache(Object value) {
        for (Map.Entry<Object, Object> entry : getCaffeineCache().asMap().entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        throw new NotFoundException("Invalid password recovery code.");
    }

}