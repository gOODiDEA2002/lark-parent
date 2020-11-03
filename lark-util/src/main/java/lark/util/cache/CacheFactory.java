package lark.util.cache;

import lark.core.lang.NotImplementedException;
import lark.util.cache.impl.MemoryCacheService;
import lark.util.cache.impl.RedisCacheService;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存服务
 * eg：CacheService cache = CacheFactory.create("Redis");
 * @author cuigh
 */
public class CacheFactory {
    private static final Map<String, CacheService> cacheSupplier = new ConcurrentHashMap<>();

    private CacheFactory() {
        // 防止实例化
    }

    /**
     * 获取
     *
     * @param name 名称
     * @return
     */
    public static CacheService create(String name) {
        String type = name.toLowerCase();
        CacheService cacheService;
        if ( cacheSupplier.containsKey( type ) ) {
            return cacheSupplier.get( type );
        }
        //
        switch ( type ) {
            case "redis":
                cacheService = new RedisCacheService();
                cacheSupplier.put( "redis", cacheService );
                return cacheService;
            case "memory":
                cacheService = new MemoryCacheService();
                cacheSupplier.put( "memory", cacheService );
                return cacheService;
        }
        throw new NotImplementedException( String.format( "CacheService: %s", name ) );
    }

}
