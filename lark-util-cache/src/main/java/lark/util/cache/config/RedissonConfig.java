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

    public RedissonConfig( String host, int port, String password ) {
        this.host = host;
        this.port = port;
        this.password = password;
    }

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer().
                setAddress( String.format( REDIS_CONNECT_TEMPLATE, host, port ) ).
                setPassword( password ).
                setConnectionMinimumIdleSize(1).
                setConnectionPoolSize(1);
        return Redisson.create(config);
    }
}

