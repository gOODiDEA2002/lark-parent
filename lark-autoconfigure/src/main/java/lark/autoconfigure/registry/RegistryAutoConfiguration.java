package lark.autoconfigure.registry;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import lark.core.util.Networks;
import lark.net.RegistryConfig;
import lark.core.boot.RegistryService;
import lark.net.nacos.NacosRegistryService;
import lark.net.rpc.client.ServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * @author andy
 */
@Configuration
@ConditionalOnClass({RegistryService.class})
@EnableConfigurationProperties(RegistryServiceProperties.class)
public class RegistryAutoConfiguration {

    @Autowired
    Environment environment;

    /**
     * @return 线程池
     */
    @Bean(name = "registryServiceScheduler")
    @Primary
    public TaskScheduler registryServiceScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(1);
        scheduler.setThreadNamePrefix("registryServiceScheduler-");
        return scheduler;
    }

    @Bean
    @ConditionalOnMissingBean
    public RegistryService registryService(RegistryServiceProperties props, @Qualifier("registryServiceScheduler") TaskScheduler registryServiceScheduler) throws NacosException {
        RegistryConfig config = new RegistryConfig();
        config.setIp( Networks.getLocalIP4() );
        config.setName( environment.getProperty( "spring.application.name") );
        config.setPort( Integer.parseInt( environment.getProperty( "server.port") ) );
        //
        NamingService namingService = NamingFactory.createNamingService(props.getAddress());
        //
        ConfigService configService = NacosFactory.createConfigService(props.getAddress());
        //
        RegistryService registryService = new NacosRegistryService( config, namingService, registryServiceScheduler, configService  );
        return registryService;
    }

    @Bean
    public ServiceFactory serviceFactory( RegistryService registryService ) {
        ServiceFactory serviceFactory = new ServiceFactory( registryService );
        return serviceFactory;
    }
}
