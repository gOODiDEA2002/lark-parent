package lark.service.boot;

import org.springframework.core.io.ResourceLoader;
import lark.core.boot.Application;

/**
 * @author cuigh
 */
public class ServiceApplication extends Application {
//    protected Server server;

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
//        registerServices();
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
//        // 注册自定义服务
//        Map<String, Object> beans = ctx.getBeansWithAnnotation(RpcService.class);
//        beans.forEach((name, bean) -> {
//            if (!(bean instanceof BaseService)) {
//                List<Class<?>> classes = Arrays.stream(bean.getClass().getInterfaces()).
//                        filter(i -> i.isAnnotationPresent(RpcService.class)).collect(Collectors.toList());
//                if (classes.isEmpty()) {
//                    server.registerService(bean);
//                } else {
//                    classes.forEach(c -> server.registerService(c, bean));
//                }
//            }
//        });
    }
}
