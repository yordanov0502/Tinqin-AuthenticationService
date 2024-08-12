package com.tinqinacademy.authenticationservice.core.utils;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtBlacklistCacheService {

    private final CacheManager cacheManager;

    private Cache<Object, Object> getCaffeineCache() {
        org.springframework.cache.Cache springCache = cacheManager.getCache("jwtBlacklist");
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
}