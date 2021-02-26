package lark.net.rpc.server;

import com.esotericsoftware.reflectasm.MethodAccess;
import lark.core.util.Strings;
import lark.net.rpc.annotation.RpcMethod;
import lark.net.rpc.annotation.RpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author cuigh
 */
public class ServiceContainer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceContainer.class);
    private final HashMap<String, ServiceMethod> executors = new HashMap<>();
    private final Map<String, ServiceInfo> services = new TreeMap<>();
    RequestMappingHandlerMapping requestMappingHandlerMapping;

    public ServiceContainer(RequestMappingHandlerMapping requestMappingHandlerMapping) {
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
    }

    public static String getMethodName(Method method) {
        return getMethodName(method, method.getAnnotation(RpcMethod.class));
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

    public void registerService(Object instance) {
        Class<?> clazz = instance.getClass();
        this.registerService(clazz, instance);
    }

    public void registerService(String name, Object instance) {
        Class<?> clazz = instance.getClass();
        RpcService rpcService = clazz.getAnnotation(RpcService.class);
        String description = rpcService == null ? "" : rpcService.description();
        this.registerService(clazz, instance, name, description);
    }

    public void registerService(Class<?> clazz, Object instance) {
        RpcService rpcService = clazz.getAnnotation(RpcService.class);
        String description = rpcService == null ? "" : rpcService.description();

        String name = null;
        if (rpcService != null) {
            name = rpcService.name();
        }
        if (Strings.isEmpty(name)) {
            name = clazz.getSimpleName();
        }

        this.registerService(clazz, instance, name, description);
    }

    private void registerService(Class<?> clazz, Object instance, String name, String description) {
        MethodAccess access = MethodAccess.get(clazz);
        Method[] methods = clazz.getMethods();
        for (Method m : methods) {
            if (m.getDeclaringClass() == Object.class) {
                continue;
            }

            try {
                int index = access.getIndex(m.getName());
                String methodName = getMethodName(m);
                ServiceMethod mi = new ServiceMethod(instance, access, index);
                executors.put(buildKey(name, methodName), mi);
                LOGGER.info("register service: " + name + "." + methodName);
            } catch (IllegalArgumentException e) {
                LOGGER.warn("find method index failed: {}", e);
            }
//            // for spring mvc
//            String methodName = getMethodName(m);
//            RequestMappingInfo.Builder builder = RequestMappingInfo
//                    .paths( "/" + name.toLowerCase() + "/" + methodName.toLowerCase() )
//                    .methods(RequestMethod.POST)
//                    .consumes(MediaType.APPLICATION_JSON_VALUE)
//                    .produces(MediaType.APPLICATION_JSON_VALUE);
//
//            requestMappingHandlerMapping.registerMapping(builder.build(),instance,m);
        }

        ServiceInfo serviceInfo = new ServiceInfo(clazz, name, description);
        this.services.put(serviceInfo.getName(), serviceInfo);
    }

    public ServiceMethod getMethod(String service, String method) {
        return executors.get(buildKey(service, method));
    }

    public Map<String, ServiceInfo> getServices() {
        return services;
    }

    private String buildKey(String serviceName, String methodName) {
        return serviceName.toLowerCase() + "." + methodName.toLowerCase();
    }
}
