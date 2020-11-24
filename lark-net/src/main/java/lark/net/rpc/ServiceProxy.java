package lark.net.rpc;
import com.netflix.config.ConfigurationManager;
import feign.Feign;
import feign.hystrix.HystrixFeign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.ribbon.LoadBalancingTarget;
import org.apache.commons.configuration.AbstractConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.ribbon.LoadBalancerFeignClient;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Configuration;
import lark.core.util.Strings;

import java.util.List;

/**
 * @author andy
 */
@Configuration
public class ServiceProxy {

    private DiscoveryClient discoveryClient;

    @Autowired
    public ServiceProxy(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    //TODO:重构
    public <T> T get( String serverName, Class<T> clazz ) {
        //手动模式下无法路由，先这样
        String serverList = getServerList( serverName );
        if ( !Strings.isEmpty( serverList ) ) {
            setServerList( serverName, serverList );
        }
        //
        T service = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .contract(new SpringMvcContract())
                .target( LoadBalancingTarget.create( clazz, "http://" + serverName ) );
        return service;
    }

    String getServerList( String serverName ) {
        List<ServiceInstance> serviceInstances = discoveryClient.getInstances( serverName );
        StringBuilder builder = new StringBuilder();
        for(ServiceInstance serviceInstance : serviceInstances){
            builder.append(serviceInstance.getUri());
            builder.append(",");
        }
        return builder.toString();
    }

    void setServerList( String serverName, String serverList ) {
        AbstractConfiguration config = ConfigurationManager.getConfigInstance();
        config.setProperty( serverName + ".ribbon.listOfServers", serverList );
    }
}
