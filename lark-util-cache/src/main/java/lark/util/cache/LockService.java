package lark.util.cache;

import lark.core.lang.ProcessHandle;

/**
 * @author andy
 */
public interface LockService {
    /**
     * 分布式锁实现
     * @param lockKey 锁名称
     * @param waitLockSeconds 等待锁时间
     * @param autoUnlockSeconds 自动释放锁时间
     * @param handle 业务处理
     * @param <T> 返回值
     * @return
     */
    <T> T tryLock(String lockKey, int waitLockSeconds, int autoUnlockSeconds, ProcessHandle<T> handle);
}
