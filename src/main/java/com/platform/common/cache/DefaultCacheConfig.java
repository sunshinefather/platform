package com.platform.common.cache;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;

public abstract class DefaultCacheConfig implements CachingConfigurer {
    
    public abstract CacheSettings cacheSettings();
    
    @Override
    @Bean
    public CacheManager cacheManager() {
        CacheManager cacheManager = createCacheManager(cacheSettings());
        addCaches(new CacheRegistryImpl(cacheManager));
        return cacheManager;
    }

    private CacheManager createCacheManager(CacheSettings settings) {
         CacheProvider provider = settings.getCacheProvider();
         if (CacheProvider.MEMCACHED.equals(provider)) {
             
             MemcachedCacheManager cacheManager = new MemcachedCacheManager(settings.getRemoteCacheServers());
             return cacheManager;
             
         }else if (CacheProvider.EHCACHE.equals(provider)) {
             
             return new EhcacheCacheManager();
         }
         throw new IllegalStateException("not supported cache provider, provider=" +provider);
    }

    @Override
    @Bean
    public KeyGenerator keyGenerator() {
        return new DefaultCacheKeyGenerator();
    }
    
    protected abstract void addCaches(CacheRegistry registry);

}
