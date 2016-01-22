package com.platform.common.cache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.config.CacheConfiguration;

import org.springframework.cache.CacheManager;
import org.springframework.util.Assert;

import com.platform.common.utils.TimeLength;

public class CacheRegistryImpl implements CacheRegistry {

    private final CacheManager cacheManager;

    public CacheRegistryImpl(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public void addCache(String cacheName, TimeLength expirationTime) {
        if (cacheManager instanceof MemcachedCacheManager) {
            ((MemcachedCacheManager) cacheManager).add(cacheName, expirationTime);
        } else {
            addCache(cacheName, expirationTime, 0);
        }
    }

    @Override
    public void addCache(String cacheName, TimeLength expirationTime, int maxEntriesInHeap) {
        Assert.isTrue(cacheManager instanceof EhcacheCacheManager, "must use ehcache manager, for memcached, please use addCache method");
        CacheConfiguration cacheConfiguration = new CacheConfiguration(cacheName, maxEntriesInHeap);
        cacheConfiguration.setTimeToLiveSeconds(expirationTime.toSeconds());
        ((EhcacheCacheManager) cacheManager).addCache(new Cache(cacheConfiguration));
    }

}
