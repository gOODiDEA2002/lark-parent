package lark.net.rpc.nacos;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import lark.core.util.BeanAwares;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author andy
 */
public class NacosClient {
    Logger log = LoggerFactory.getLogger( NacosClient.class );

    private Environment environment;

    public String getServerAddress() {
        if ( environment == null ) {
            environment = BeanAwares.getBean("environment", Environment.class );
        }
        return environment.resolvePlaceholders("${spring.cloud.nacos.discovery.server-addr:}");
    }

    public String getNodeAddress(String serviceName ) {
        try {
            NamingService namingService = NamingFactory.createNamingService( getServerAddress() );
            Instance node = namingService.selectOneHealthyInstance(serviceName, true );
            if ( node != null ) {
                return String.format( "http://%s:%s", node.getIp(), node.getPort());
            }
        } catch (NacosException e) {
            log.error("NacosClient getTargetUrl failure: {} , Cause:{}", serviceName, e.getMessage() );
        }
        return Strings.EMPTY;
    }
}
