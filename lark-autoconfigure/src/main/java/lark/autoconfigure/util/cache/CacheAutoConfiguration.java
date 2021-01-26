package lark.autoconfigure.util.cache;

import lark.util.cache.CacheService;
import lark.util.cache.LockService;
import lark.util.cache.RateLimitService;
import lark.util.cache.cache.RedissonCacheService;
import lark.util.cache.config.RedissonConfig;
import lark.util.cache.limit.RedissonRateLimitService;
import lark.util.cache.lock.RedissonLockService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author andy
 */
@Configuration
@ConditionalOnClass({CacheService.class})
@ConditionalOnProperty(prefix="lark.util.cache",name = "address")
@EnableConfigurationProperties(CacheServiceProperties.class)
public class CacheAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RedissonConfig redissonConfig(CacheServiceProperties props) {
        return new RedissonConfig( props.getHost(), props.getPort(), props.getPassword() );
    }

    @Bean
    @ConditionalOnMissingBean
    public CacheService cacheService( RedissonConfig redissonConfig ) {
        return new RedissonCacheService( redissonConfig.redissonClient() );
    }

    @Bean
    @ConditionalOnMissingBean
    public LockService lockService( RedissonConfig redissonConfig ) {
        return new RedissonLockService( redissonConfig.redissonClient() );
    }

    @Bean
    @ConditionalOnMissingBean
    public RateLimitService rateLimitService(RedissonConfig redissonConfig ) {
        return new RedissonRateLimitService( redissonConfig.redissonClient() );
    }
}
