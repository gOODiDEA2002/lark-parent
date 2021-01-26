package lark.util.cache;

import lark.core.lang.ExecuteHandle;

/**
 * @author andy
 */
public interface RateLimitService {
    /**
     * 限流
     * @param rateLimitKey
     * @param rate 允许次数
     * @param intervalSecond 限流周期
     * @param handle
     * @return 是否被执行
     */
    boolean tryProcess(String rateLimitKey, int rate, int intervalSecond, ExecuteHandle handle);
}
