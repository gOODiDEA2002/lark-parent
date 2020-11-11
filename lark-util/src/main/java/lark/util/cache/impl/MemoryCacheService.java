package lark.util.cache.impl;

import lark.core.thread.Clock;
import lark.util.cache.CacheService;

import java.time.Duration;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by guohua.cui on 15/5/19.
 */
@SuppressWarnings("unchecked")
public class MemoryCacheService implements CacheService {
    public static final int CLEAR_INTERVAL = 60 * 1000;
    private ConcurrentHashMap<String, CacheItem> caches = new ConcurrentHashMap<>();

    public MemoryCacheService() {
        Clock.set(() -> {
            Iterator<Map.Entry<String, CacheItem>> iterator = caches.entrySet().iterator();
            while (iterator.hasNext()) {
                CacheItem item = iterator.next().getValue();
                if (item.isExpired()) {
                    iterator.remove();
                }
            }
        }, CLEAR_INTERVAL, CLEAR_INTERVAL);
    }

    @Override
    public void set(String key, Object value, Duration time) {
        CacheItem ci = new CacheItem(value, time, null);
        this.caches.put(key, ci);
    }

    @Override
    public void set(String key, Object value, String version, Duration time) {
        CacheItem ci = new CacheItem(value, time, version);
        this.caches.put(key, ci);
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        CacheItem ci = this.caches.get(key);
        if (ci != null && !ci.isExpired()) {
            return (T) ci.value;
        }
        return null;
    }

    @Override
    public <T> T get(String key, String versionKey, Class<T> clazz) {
        return getData(key, versionKey);
    }

    private <T> T getData(String key, String versionKey) {
        CacheItem ci = this.caches.get(key);
        if (ci != null && !ci.isExpired()) {
            if (!get(versionKey, String.class).equals(ci.version)) { 
                return null;
            }
            return (T) ci.value;
        }
        return null;
    }

    @Override
    public boolean exist(String key) {
        return this.caches.containsKey(key);
    }

    @Override
    public void remove(String key) {
        this.caches.remove(key);
    }

    static class CacheItem {
        private Object value;
        private long expiry;
        private String version;

        CacheItem(Object value, Duration expiry, String version) {
            this.value = value;
            this.expiry = System.currentTimeMillis() + expiry.toMillis();
            this.version = version;
        }

        public boolean isExpired() {
            return this.expiry < System.currentTimeMillis();
        }
    }
}
