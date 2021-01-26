package lark.net.nacos;

import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import lark.core.util.Strings;
import lark.net.RegistryConfig;
import lark.core.boot.RegistryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.TaskScheduler;

import java.time.Duration;
import java.util.concurrent.ScheduledFuture;

public class NacosRegistryService implements RegistryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(NacosRegistryService.class);
    RegistryConfig registryConfig;
    NamingService namingService;
    ConfigService configService;
    private final String DEFAULT_GROUP = "LARK";
    private TaskScheduler scheduler;
    private ScheduledFuture future;
    private final Duration interval = Duration.ofMinutes( 3 );

    public NacosRegistryService( RegistryConfig registryConfig, NamingService namingService, TaskScheduler scheduler, ConfigService configService ) {
        this.registryConfig = registryConfig;
        this.namingService = namingService;
        this.configService = configService;
        this.scheduler = scheduler;
    }

    @Override
    public String getConfig(String id, String groupName) {
        return null;
    }

    @Override
    public void registerService() {
        if (this.future != null) {
            return;
        }
        //
        this.future = scheduler.scheduleWithFixedDelay(() -> {
            try {
                namingService.registerInstance( registryConfig.getName(), DEFAULT_GROUP, registryConfig.getIp(), registryConfig.getPort() );
                LOGGER.info("registerService: name:{}, group:{}, ip:{}, port:{}", registryConfig.getName(), DEFAULT_GROUP, registryConfig.getIp(), registryConfig.getPort() );
            } catch (Exception e) {
                LOGGER.error("===> Failed to registerService", e);
            }
        }, interval);
    }

    @Override
    public void deregisterService() {
        if (this.future == null) {
            return;
        }
        //
        this.future.cancel(true);
        try {
            namingService.deregisterInstance( registryConfig.getName(), DEFAULT_GROUP, registryConfig.getIp(), registryConfig.getPort() );
            LOGGER.info("deregisterService: name:{}, group:{}, ip:{}, port:{}", registryConfig.getName(), DEFAULT_GROUP, registryConfig.getIp(), registryConfig.getPort());
        } catch (Exception e) {
            LOGGER.error("===> Failed to deregisterService", e);
        } finally {
            this.future = null;
        }

    }

    @Override
    public String getService(String serviceName, String groupName) {
        try {
            if ( Strings.isEmpty( groupName ) ) {
                groupName = DEFAULT_GROUP;
            }
            //
            Instance node = namingService.selectOneHealthyInstance(serviceName, groupName,false );
            if ( node != null ) {
                return String.format( "http://%s:%s", node.getIp(), node.getPort());
            }
        } catch (NacosException e) {
            LOGGER.error("===> getService failure: {} , Cause:{}", serviceName, e.getMessage() );
        }
        return Strings.EMPTY;
    }
}
