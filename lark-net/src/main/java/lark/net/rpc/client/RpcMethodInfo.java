package lark.net.rpc.client;

import lark.core.util.Strings;
import lark.net.rpc.annotation.RpcMethod;
import lark.net.rpc.annotation.RpcService;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author cuigh
 */
public class RpcMethodInfo {
    private static final Map<Class<?>, Map<String, RpcMethodInfo>> CACHES = new ConcurrentHashMap<>();
    private final String service;
    private final String name;
    private Class<?> returnType;
    private String path;
    private final static String PATH_TEMPLATE = "/lark/%s/%s";

    public RpcMethodInfo(String service, Method method, RpcMethod rpcMethod, RpcService rpcService) {
        this.service = service;
        this.name = getMethodName(method, rpcMethod);
        this.returnType = method.getReturnType();
        this.path = String.format( PATH_TEMPLATE, this.service.toLowerCase(), this.name.toLowerCase() );
    }

    public static Map<String, RpcMethodInfo> get(Class<?> clazz) {
        return CACHES.computeIfAbsent(clazz, cls -> {
            String service;
            Map<String, RpcMethodInfo> methods = new HashMap<>();
            //
            RpcService rpcService = clazz.getAnnotation(RpcService.class);
            if (rpcService == null || Strings.isEmpty(rpcService.name())) {
                service = clazz.getSimpleName();
            } else {
                service = rpcService.name();
            }

            Method[] clazzMethods = clazz.getMethods();
            for (Method m : clazzMethods) {
                if (m.getDeclaringClass() == Object.class) {
                    continue;
                }
                //
                RpcMethod rpcMethod = m.getAnnotation(RpcMethod.class);
                methods.put(m.getName(), new RpcMethodInfo(service, m, rpcMethod, rpcService));
            }
            return methods;
        });
    }

    public String getService() {
        return service;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public static String getMethodName(Method method, RpcMethod rpcMethod) {
        String name = null;
        if (rpcMethod != null) {
            name = rpcMethod.name();
        }
        if (Strings.isEmpty(name)) {
            name = StringUtils.capitalize(method.getName());
        }
        return name;
    }
}
