package lark.util.cache;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;


import java.io.Serializable;
import java.time.Duration;

/**
 * Created by Andy Yuan on 2020/10/27.
 */

public class CacheTest {
    @Autowired
    RedisTemplate<String, Serializable> redisTemplate;
    @Test
    public void testRedisCache() {
        CacheService cache = CacheFactory.create( "redis" );
        redisTemplate.getClientList();
        String key = "CacheTest_123456";
        String value = "CacheValue_123456";
        cache.set( key, value, Duration.ofMinutes( 3 ) );
        Assert.assertEquals( value, cache.get( key, String.class ) );
    }
}
