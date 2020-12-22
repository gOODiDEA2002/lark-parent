package lark.net.rpc.client;

import java.util.Map;

/**
 * @author cuigh
 */
public abstract class BaseService {
    private Client client;
    private Map<String, RpcMethodInfo> methods;

    protected BaseService(Client client, Class cls) {
        this.client = client;
        this.methods = RpcMethodInfo.get(cls);
    }

    protected Object invoke(String method, Object[] args) {
        return client.invoke(methods.get(method), args);
    }
}
