package com.inditex.infrastructure.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.inditex.application.dto.ProductDetailDTO;
import com.inditex.infrastructure.model.ProductResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
/**
 * Provides a static Caffeine cache for {@link ProductDetailDTO} objects.
 */
@Component
public class CacheProvider {

    private static Cache<String, ProductResponse> cache;

    @Value("${spring.cache.time}")
    private int cacheTime;

    @Value("${spring.cache.maxSize}")
    private int maxSize;

    /**
     * Initializes the cache with time-to-live and maximum size settings.
     */
    @PostConstruct
    public void init() {
        cache = Caffeine.newBuilder()
                .expireAfterWrite(cacheTime, TimeUnit.SECONDS)
                .maximumSize(maxSize)
                .build();
    }

    /**
     * Returns the shared cache instance.
     *
     * @return the cache instance
     */

    public static Cache<String, ProductResponse> getCache() {
        return cache;
    }
}
