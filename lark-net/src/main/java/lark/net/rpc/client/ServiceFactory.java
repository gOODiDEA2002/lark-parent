package lark.net.rpc.client;

import lark.core.boot.RegistryService;
import lark.net.rpc.annotation.RpcService;
import org.reflections8.Reflections;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author cuigh
 */
public class ServiceFactory {
    private final Map<Class, Object> proxies = new ConcurrentHashMap<>();
    private final Map<String, Client> clients = new ConcurrentHashMap<>();
    private final Map<String, ClientOptions> options = new HashMap<>();
    private final ServiceClassLoader classLoader = new ServiceClassLoader(Thread.currentThread().getContextClassLoader());
    private String serverName;

    private RegistryService registryService;

    public ServiceFactory( RegistryService registryService ) {
        this.registryService = registryService;
    }

    public Set<Class<?>> getServices( String packageName, String serverName ) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(RpcService.class);
        return classes;
//        if ( classes != null && classes.size() > 0 ) {
//            for ( Class clazz : classes ) {
//                get(serverName, clazz);
//            }
//        }
//        return proxies;
    }
    /**
     * 获取编解码器
     *
     * @param server 服务端名称
     * @param cls    目标类信息
     * @param <T>    数据类型
     * @return 编解码器
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String server, Class<T> cls) {
        return (T) proxies.computeIfAbsent(cls, c -> createProxy(server, cls));
    }

    private Object createProxy(String server, Class cls) {
        if (!cls.isInterface()) {
            throw new IllegalArgumentException("Can't create proxy for non-interface type: " + cls.getName());
        }
        this.serverName = server;
        String proxyTypeName = ServiceClassLoader.getProxyClassName(cls);
        try {
            Class<?> proxyClass = classLoader.loadProxyClass(proxyTypeName, cls);
            Constructor<?> constructor = proxyClass.getConstructor(Client.class);
            Client client = clients.computeIfAbsent(server, this::createClient);
            return constructor.newInstance(client);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private Client createClient(String server) {
        ClientOptions options = this.options.get(server);
        if (options == null) {
            options = new ClientOptions();
        }
        Client client = new Client( registryService, server, options);
        return client;
    }

    public void setName(String serverName) {
        this.serverName = serverName;
    }

    public void addOptions(String serverName, ClientOptions options) {
        this.options.put(serverName, options);
    }

}
