package com.tinqinacademy.authenticationservice.rest.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Collections;

@Configuration
public class CaffeineCacheConfig {

    @Value("${env.JWT_BLACKLIST}")
    private String JWT_BLACKLIST;
    @Value("${env.JWT_BLACKLIST_DURATION}")
    private static int JWT_BLACKLIST_DURATION;

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.registerCustomCache(JWT_BLACKLIST, jwtBlacklist());

        //! To avoid dynamic caches and be sure each name is assigned to a specific config (dynamic = false)
        //! throws error when tries to use a new cache
        cacheManager.setCacheNames(Collections.emptyList());

        return cacheManager;
    }

    private static Cache<Object,Object> jwtBlacklist() {
        return Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(JWT_BLACKLIST_DURATION))
                .build();
    }

}