package lark.util.cache.limit;

import lark.core.lang.ExecuteHandle;
import lark.core.lang.ProcessHandle;
import lark.core.lang.ServiceException;
import lark.util.cache.RateLimitService;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 限流服务
 * @author andy
 */
public class RedissonRateLimitService implements RateLimitService {
    private static final String KEY_TEMPLATE = "l-r-%s-%s";
    private static final Logger LOGGER = LoggerFactory.getLogger(RedissonRateLimitService.class);
    private final RedissonClient redisson;
    private final String keyPrefix;
    public RedissonRateLimitService(String keyPrefix, RedissonClient redisson) {
        this.keyPrefix = keyPrefix;
        this.redisson = redisson;
    }

    /**
     * 限流
     * @param rateLimitKey
     * @param rate 允许次数
     * @param intervalSecond 限流周期
     * @param handle
     * @return 是否被执行
     */
    @Override
    public boolean tryProcess(String rateLimitKey, int rate, int intervalSecond, ExecuteHandle handle) {
        RRateLimiter rateLimiter = redisson.getRateLimiter( getKey( rateLimitKey ) );
        boolean result = rateLimiter.trySetRate(RateType.OVERALL, rate, intervalSecond, RateIntervalUnit.SECONDS);
//        if ( !result ) {
//            throw new ServiceException( "RedissonRateLimitService trySetRate fail!" );
//        }
        //
        boolean acquireResult = rateLimiter.tryAcquire();
        if ( acquireResult ) {
            try {
                handle.execute();
            } finally {
                rateLimiter.clearExpire();
            }
            return true;
        } else {
            LOGGER.debug("RateLimiter limit: {}, rate:{} intervalSecond:{}", rateLimitKey, rate, intervalSecond );
            return false;
        }
    }

    private String getKey( String key ) {
        return String.format( KEY_TEMPLATE, keyPrefix, key );
    }
}
