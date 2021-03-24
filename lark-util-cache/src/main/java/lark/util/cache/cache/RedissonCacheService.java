package lark.util.cache.cache;

import lark.core.codec.JsonCodec;
import lark.core.lang.UncheckedException;
import lark.util.cache.CacheService;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author andy
 */
public class RedissonCacheService implements CacheService {
    private static final String KEY_TEMPLATE = "l-c-%s-%s";
    private final String keyPrefix;
    private static final String DEFAULT_VERSION_KEY = "";
    private static final int BUFFER_SIZE = 1024;
    private static final int COMPRESS_THRESHOLD = 16 * 1024;
    RedissonClient redissonClient;

    public RedissonCacheService(String keyPrefix, RedissonClient redissonClient) {
        this.keyPrefix = keyPrefix;
        this.redissonClient = redissonClient;
    }

    @Override
    public void set(String key, Object value) {
        set(key, value, DEFAULT_VERSION_KEY, Duration.ZERO);
    }

    @Override
    public void set(String key, Object value, Duration time) {
        set(key, value, DEFAULT_VERSION_KEY, time);
    }

    @Override
    public void set(String key, Object value, String version, Duration time) {
        if (value == null) {
            return;
        }
        //
        RBucket<Object> bucket = this.redissonClient.getBucket(getKey(key));
        if (time.isZero()) {
            bucket.set(encode(value, version));
        } else {
            bucket.set(encode(value, version), time.getSeconds(), TimeUnit.SECONDS);
        }
    }

    @Override
    public String get(String key) {
        return getData(key, String.class);
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        Object value = getData(key, clazz);
        if (value != null) {
            return (T) value;
        }
        return null;
    }

    @Override
    public <T> T get(String key, String versionKey, Class<T> clazz) {
        Object value = getData(key, versionKey, clazz);
        if (value != null) {
            return (T) value;
        }
        return null;
    }


    private <T> T getData(String key, Class<T> clazz) {
        return getData(key, DEFAULT_VERSION_KEY, clazz);
    }

    private <T> T getData(String key, String versionKey, Class<T> clazz) {
        RBucket<Object> bucket = this.redissonClient.getBucket(getKey(key));
        Object value = bucket.get();
        if (value == null) {
            return null;
        }
        //
        CacheItem data = JsonCodec.decodeAsBytes((byte[]) value, CacheItem.class);
        if (!data.version.equals(versionKey)) {
            return null;
        }
        return data.compressed ? JsonCodec.decodeAsBytes(decompress(data.data), clazz) : JsonCodec.decodeAsBytes(data.data, clazz);
    }

    private byte[] encode(Object value, String version) {
        CacheItem data = new CacheItem();
        data.version = version;
        data.data = JsonCodec.encodeAsBytes(value);
        if (data.data.length > COMPRESS_THRESHOLD) {
            data.compressed = true;
            data.data = compress(data.data);
        }
        return JsonCodec.encodeAsBytes(data);
    }

    private static byte[] compress(byte[] data) {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream();
             GZIPOutputStream gos = new GZIPOutputStream(os)) {
            gos.write(data, 0, data.length);
            gos.close();
            return os.toByteArray();
        } catch (Exception e) {
            throw new UncheckedException(e);
        }
    }

    private static byte[] decompress(byte[] data) {
        try (ByteArrayInputStream is = new ByteArrayInputStream(data);
             GZIPInputStream gis = new GZIPInputStream(is);
             ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            int count;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((count = gis.read(buffer)) >= 0) {
                os.write(buffer, 0, count);
            }

            os.flush();
            return os.toByteArray();
        } catch (Exception e) {
            throw new UncheckedException(e);
        }
    }

    @Override
    public boolean exist(String key) {
        RBucket<Object> bucket = this.redissonClient.getBucket(getKey(key));
        return bucket.isExists();
    }

    @Override
    public Boolean expire(String key, Duration time) {
        RBucket<Object> bucket = this.redissonClient.getBucket(getKey(key));
        return bucket.expire(time.getSeconds(), TimeUnit.SECONDS);
    }

    @Override
    public void remove(String key) {
        RBucket<Object> bucket = this.redissonClient.getBucket(getKey(key));
        bucket.delete();
    }

    private String getKey(String key) {
        return String.format(KEY_TEMPLATE, keyPrefix, key);
    }

}
