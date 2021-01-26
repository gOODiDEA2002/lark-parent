package lark.util.cache.lock;

import lark.core.lang.ProcessingException;
import lark.core.lang.ProcessHandle;
import lark.util.cache.LockService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁服务
 *
 *     @author andy
 *     @Autowired
 *     LockService lockService;
 *
 *        int result = lockService.tryLock( lockKey, waitSec, autoUnlock, () -> {
 *             log.info( "Thread:{} lockService get lock", Thread.currentThread().getName() );
 *             try {
 *                 Thread.sleep(1000 );
 *             } catch (InterruptedException e) {
 *                 e.printStackTrace();
 *             }
 *             log.info( "Thread:{} lockService unlock", Thread.currentThread().getName() );
 *             return 0;
 *         });
 *
 * Created by Andy Yuan on 2020/10/27.
 */
public class RedissonLockService implements LockService  {
    private static final String KEY_TEMPLATE = "l-l-%s";
    private static final Logger LOGGER = LoggerFactory.getLogger(RedissonLockService.class);
    private final RedissonClient redisson;
    public RedissonLockService(RedissonClient redisson) {
        this.redisson = redisson;
    }

    /**
     * 分布式锁实现
     * @param lockKey 锁名称
     * @param waitLockSeconds 等待锁时间
     * @param autoUnlockSeconds 自动释放锁时间
     * @param handle 业务处理
     * @param <T> 返回值
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public <T> T tryLock(String lockKey, int waitLockSeconds, int autoUnlockSeconds, ProcessHandle<T> handle) {
        RLock lock = redisson.getLock( String.format( KEY_TEMPLATE, lockKey ) );
        boolean canLock = false;
        try {
            canLock = lock.tryLock( waitLockSeconds, autoUnlockSeconds, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOGGER.error("acquire lock failure: {} waitLock:{} autoUnlock:{}, Cause:{}", lockKey, waitLockSeconds, autoUnlockSeconds, e.getMessage() );
        }
        if ( !canLock ) {
            LOGGER.error("acquire lock failure: {}, Processing... waitLock:{} autoUnlock:{}", lockKey, waitLockSeconds, autoUnlockSeconds );
            throw new ProcessingException();
        }
        //
        try {
            return handle.execute();
        } finally {
            lock.unlock();
        }
    }
}
