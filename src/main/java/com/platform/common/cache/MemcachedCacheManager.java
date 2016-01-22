package com.platform.common.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.PreDestroy;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.spring.MemcachedClientFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractCacheManager;
import org.springframework.util.Assert;
import com.platform.common.utils.TimeLength;

public class MemcachedCacheManager extends AbstractCacheManager{

    private final Logger logger = LoggerFactory.getLogger(MemcachedCacheManager.class);
    private static final TimeLength MEMCACHED_TIME_OUT = TimeLength.seconds(3);

    private final List<MemcachedCache> caches = new ArrayList<MemcachedCache>();
    private MemcachedClient memcachedClient;
    
    public MemcachedCacheManager(String remoteServers){
        setServers(remoteServers);
    }

    @PreDestroy
    public void shutdown() {
        logger.info("shutdown memcached cache client");
        memcachedClient.shutdown();
    }

    @Override
    protected Collection<? extends Cache> loadCaches() {
        return caches;
    }

    public void add(String cacheName, TimeLength expirationTime) {
        Assert.isNull(memcachedClient, "memcached client must be created before adding cache");
        caches.add(new MemcachedCache(cacheName, expirationTime, memcachedClient));
    }

    public void setServers(String remoteServers) {
        MemcachedClientFactoryBean factory = new MemcachedClientFactoryBean();
        factory.setServers(remoteServers);
        factory.setOpTimeout(MEMCACHED_TIME_OUT.toMilliseconds());
        factory.setProtocol(ConnectionFactoryBuilder.Protocol.BINARY);
        try {
            memcachedClient = (MemcachedClient) factory.getObject();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}