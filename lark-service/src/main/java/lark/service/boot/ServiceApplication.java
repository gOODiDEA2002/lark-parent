package lark.service.boot;

import lark.core.boot.Application;
import lark.net.rpc.annotation.RpcService;
import lark.net.rpc.client.BaseService;
import lark.net.rpc.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ResourceLoader;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author cuigh
 */
public class ServiceApplication extends Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceApplication.class);
    private Server server;

    public ServiceApplication(Class<?>... primarySources) {
        this(null, primarySources);
    }

    public ServiceApplication(ResourceLoader resourceLoader, Class<?>... primarySources) {
        super(resourceLoader, primarySources);
    }

    @Override
    protected void load() {
        super.load();
        this.server = ctx.getBean(Server.class);
        registerServices();
    }

    private void registerServices() {
        // 注册自定义服务
        Map<String, Object> beans = ctx.getBeansWithAnnotation(RpcService.class);
        beans.forEach((name, bean) -> {
            if (!(bean instanceof BaseService)) {
                List<Class<?>> classes = Arrays.stream(bean.getClass().getInterfaces()).
                        filter(i -> i.isAnnotationPresent(RpcService.class)).collect(Collectors.toList());
                if (classes.isEmpty()) {
                    server.registerService(bean);
                } else {
                    classes.forEach(c -> server.registerService(c, bean));
                }
            }
        });
    }
}
