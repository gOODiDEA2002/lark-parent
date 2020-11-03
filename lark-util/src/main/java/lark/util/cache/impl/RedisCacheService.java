package lark.util.cache.impl;

import java.util.Base64;
import lark.core.codec.JsonCodec;
import lark.core.lang.UncheckedException;
import lark.core.util.BeanAwares;
import lark.util.cache.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.time.Duration;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;


/**
 * TODO:性能隐患
 */
public class RedisCacheService implements CacheService {
    private static final String DEFAULT_VERSION_KEY = "";
    private static final int BUFFER_SIZE = 1024;
    private static final int COMPRESS_THRESHOLD = 16 * 1024;
    private static final Logger log = LoggerFactory.getLogger(RedisCacheService.class);
    private RedisTemplate<String, Serializable> redisTemplate = ( RedisTemplate<String,Serializable> ) BeanAwares.getBean( "serializableRedisTemplate" );

    public RedisCacheService() {
    }

    @Override
    public void set(String key, Object value, Duration time) {
        if (value == null) {
            return;
        }
        //
        ValueOperations ops = redisTemplate.opsForValue();
        ops.set(key, encode( value ), time);
    }

    @Override
    public void set(String key, Object value, String version, Duration time) {
        if (value == null) {
            return;
        }
        //
        ValueOperations ops = redisTemplate.opsForValue();
        ops.set(key, encode( value, version ), time);
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
        Object value = getData( key, versionKey, clazz );
        if (value != null) {
            return (T) value;
        }
        return null;
    }

    private <T> T getData(String key, Class<T> clazz) {
        return getData( key, DEFAULT_VERSION_KEY, clazz );
    }

    private <T> T getData(String key, String versionKey, Class<T> clazz) {
        ValueOperations ops = redisTemplate.opsForValue();
        Object value = ops.get(key);
        if (value == null) {
            return null;
        }

        byte[] bytes = Base64.getDecoder().decode((String) value );
        CacheItem data = JsonCodec.decodeAsBytes( bytes, CacheItem.class);
        if (!data.version.equals(versionKey)) {
            return null;
        }
        return data.compressed ? JsonCodec.decodeAsBytes( decompress(data.data), clazz ) : JsonCodec.decodeAsBytes( data.data, clazz );
    }

    private byte[] encode(Object value) {
        return encode( value, DEFAULT_VERSION_KEY );
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
        return redisTemplate.hasKey(key);
    }

    @Override
    public void remove(String key) {
        redisTemplate.delete(key);
    }

    static class CacheItem {
        //@JSONField(name = "v")
        public String version;
        //@JSONField(name = "c")
        public boolean compressed;
        //@JSONField(name = "d")
        public byte[] data;

        public CacheItem() {
            // just for serialization
        }

        public CacheItem(byte[] data) {
            this.data = data;
        }
    }

}
