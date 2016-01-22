package com.platform.common.cache;


import net.sf.ehcache.Cache;
import org.springframework.cache.ehcache.EhCacheCacheManager;

public class EhcacheCacheManager  extends EhCacheCacheManager{

    public EhcacheCacheManager() {
        setCacheManager(new net.sf.ehcache.CacheManager(new net.sf.ehcache.config.Configuration()));
    }

    public void addCache(Cache cache) {
        getCacheManager().addCache(cache);
    }
}