package lark.util.cache;

import lark.util.cache.impl.MemoryCacheService;
import lark.util.cache.impl.RedisCacheService;
import lark.core.lang.UncheckedException;

import java.time.Duration;

/**
 * Created by Andy Yuan on 2020/7/19.
 */
public interface CacheService {
    void set(String key, Object value, Duration time);

    void set(String key, Object value, String version, Duration time);

    <T> T get(String key, Class<T> clazz);

    <T> T get(String key, String versionKey, Class<T> clazz);

    boolean exist(String key);

    void remove(String key);

}
