package lark.net.rpc.client;

import lark.net.rpc.annotation.RpcMethod;
import lark.net.rpc.annotation.RpcService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author cuigh
 */
public class RpcMethodInfo {
    private static final Map<Class<?>, Map<String, RpcMethodInfo>> CACHES = new ConcurrentHashMap<>();
    private String service;
    private String name;
    private Class<?> returnType;
    private String path;

    public RpcMethodInfo(String service, Method method, PostMapping rpcMethod, RequestMapping rpcService) {
        this.service = service;
        this.name = getMethodName(method, rpcMethod);
        this.returnType = method.getReturnType();
        this.path = rpcService.value()[0] + rpcMethod.value()[0];
    }

    public static Map<String, RpcMethodInfo> get(Class<?> clazz) {
        return CACHES.computeIfAbsent(clazz, cls -> {
            String service;
            Map<String, RpcMethodInfo> methods = new HashMap<>();
            //
            RequestMapping rpcService = clazz.getAnnotation(RequestMapping.class);
            if (rpcService == null || StringUtils.isEmpty(rpcService.name())) {
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
                PostMapping rpcMethod = m.getAnnotation(PostMapping.class);
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

    public static String getMethodName(Method method, PostMapping rpcMethod) {
        String name = null;
        if (rpcMethod != null) {
            name = rpcMethod.name();
        }
        if (StringUtils.isEmpty(name)) {
            name = StringUtils.capitalize(method.getName());
        }
        return name;
    }
}
