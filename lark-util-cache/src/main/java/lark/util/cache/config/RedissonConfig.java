package lark.util.cache.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;

public class RedissonConfig {

    private final String host;

    private final int port;

    private final String password;
    
    private final static String REDIS_CONNECT_TEMPLATE = "redis://%s:%s";

    private int minPoolSize;
    private int maxPoolSize;

    public RedissonConfig( String host, int port, String password, int minPoolSize, int maxPoolSize ) {
        this.host = host;
        this.port = port;
        this.password = password;
        this.minPoolSize = minPoolSize;
        this.maxPoolSize = maxPoolSize;
    }

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer().
                setAddress( String.format( REDIS_CONNECT_TEMPLATE, host, port ) ).
                setPassword( password ).
                setConnectionMinimumIdleSize( minPoolSize ).
                setConnectionPoolSize( maxPoolSize );
        return Redisson.create(config);
    }
}

