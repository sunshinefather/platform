package com.platform.common.cache;

import java.lang.reflect.Method;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.interceptor.KeyGenerator;
import com.platform.common.security.Digests;
import com.platform.common.utils.DateUtils;

public class DefaultCacheKeyGenerator implements KeyGenerator {

    private final Logger logger = LoggerFactory.getLogger(DefaultCacheKeyGenerator.class);

    private static final String NO_PARAM_KEY = "";

    @Override
    public Object generate(Object target, Method method, Object... params) {
        if (params.length == 0) {
            return NO_PARAM_KEY;
        }

        String key = buildStringCacheKey(params);
        logger.debug("cache key, method={}, key={}", method, key);
        return encodeKey(key);
    }

    public String encodeKey(String key) {
        if (key.length() > 32 || containsIllegalKeyChar(key)) {
            return new String(Digests.md5(key.getBytes()));
        }
        return key;
    }

    private String buildStringCacheKey(Object[] params) {
        if (params.length == 1) return getKeyValue(params[0]);

        StringBuilder builder = new StringBuilder();
        int index = 0;
        for (Object param : params) {
            if (index > 0) builder.append(':');
            String value = getKeyValue(param);
            builder.append(value);
            index++;
        }
        return builder.toString();
    }

    private String getKeyValue(Object param) {
        if (param instanceof CacheKeyGenerator) return ((CacheKeyGenerator) param).buildCacheKey();
        if (param instanceof Enum) return ((Enum) param).name();
        if (param instanceof Date) return DateUtils.formatDateTime((Date) param);
        return String.valueOf(param);
    }

    private boolean containsIllegalKeyChar(String value) {
        return value.contains(" ");
    }
}