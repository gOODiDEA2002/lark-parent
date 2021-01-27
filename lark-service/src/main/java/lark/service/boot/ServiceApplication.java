package lark.service.boot;

import lark.core.boot.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ResourceLoader;

/**
 * @author cuigh
 */
public class ServiceApplication extends Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceApplication.class);

    public ServiceApplication(Class<?>... primarySources) {
        this(null, primarySources);
    }

    public ServiceApplication(ResourceLoader resourceLoader, Class<?>... primarySources) {
        super(resourceLoader, primarySources);
    }

    @Override
    protected void load() {
        super.load();
//        this.server = ctx.getBean(Server.class);
//
//        // Set Register
//        Register register = findBean(Register.class);
//        if (register != null) {
//            server.setRegister(register);
//        }
//
//        // Register protocols
//        Map<String, ServerProtocol> protocols = ctx.getBeansOfType(ServerProtocol.class);
//        this.server.registerProtocol(protocols.values().toArray(new ServerProtocol[0]));
//
//        // Register filters
//        Map<String, ServerFilter> filters = ctx.getBeansOfType(ServerFilter.class);
//        this.server.use(filters.values().toArray(new ServerFilter[0]));
//
        registerServices();
    }

    @Override
    protected void start() {
//        this.server.start();
    }

    private void registerServices() {
//        // 注册系统服务
//        this.server.registerService(SystemService.class, new SystemServiceImp(server, ctx.getStartupDate()));
//        this.server.registerService(MetaService.class, new MetaServiceImp(server));
//
        // 获取服务列表
//        Map<String, Object> beans = ctx.getBeansWithAnnotation(RestController.class);
//        beans.forEach((name, bean) -> {
//            RestController service = ( RestController ) bean;
//            LOGGER.info( "find service: {}, {}", name, service.value() );
//        });
    }
}
