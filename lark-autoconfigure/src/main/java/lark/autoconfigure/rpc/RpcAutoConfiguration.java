package lark.autoconfigure.rpc;

import io.micrometer.core.instrument.MeterRegistry;
import lark.core.util.Networks;
import lark.core.util.Strings;
import lark.net.rpc.server.Server;
import lark.net.rpc.server.ServerOptions;
import lark.service.boot.ServiceApplication;
import lark.service.boot.web.ServiceServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.SocketUtils;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for RPC client and server.
 *
 * @author cuigh
 */
@Configuration
@ConditionalOnClass(ServiceApplication.class)
public class RpcAutoConfiguration {
    private final ApplicationContext ctx;
    @Autowired(required = false)
    private MeterRegistry meterRegistry;

    public RpcAutoConfiguration(ApplicationContext ctx) {
        this.ctx = ctx;
    }

    @Bean
    @ConditionalOnMissingBean
    public ServerOptions rpcServerOptions( RequestMappingHandlerMapping requestMappingHandlerMapping ) {
        return new ServerOptions( requestMappingHandlerMapping );
    }

    @Bean
    @ConditionalOnBean(ServerOptions.class)
    @ConditionalOnMissingBean
    public Server rpcServer(ServerOptions options) {
        if (Strings.isEmpty(options.getName())) {
            options.setName(ctx.getId());
        }
        Server server = new Server(options);
        server.setMeterRegistry(meterRegistry);
        return server;
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean( Server server ){
        return new ServletRegistrationBean(new ServiceServlet( server ), "/lark/*");
    }

}
