package lark.util.cache;

import java.time.Duration;

/**
 * @author andy
 */
public interface CacheService {
    void set(String key, Object value, Duration time);

    void set(String key, Object value, String versionKey, Duration time);

    String get(String key);

    <T> T get(String key, Class<T> clazz);

    <T> T get(String key, String versionKey, Class<T> clazz);

    boolean exist(String key);

    void remove(String key);
}