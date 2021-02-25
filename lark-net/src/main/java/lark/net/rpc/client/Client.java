package lark.net.rpc.client;

import lark.core.boot.RegistryService;
import lark.core.codec.JsonCodec;
import lark.net.Address;
import lark.net.rpc.RpcError;
import lark.net.rpc.RpcException;
import okhttp3.*;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author cuigh
 */
public class Client {
    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);
    private String server;
    private ClientOptions options;
    private List<Node> nodes;
    RegistryService registryService;

    public Client(RegistryService registryService, String server, ClientOptions options) {
        Objects.requireNonNull(options);
        this.registryService = registryService;
        this.server = server;
        this.options = options;
    }

    public Object invoke(RpcMethodInfo method, Object[] args) {
        String serviceUrl = getServiceUrl(method);
        return ClientInvoker.invoke( serviceUrl, method.getService(), method.getName(), args, method.getReturnType() );
    }

    private String getServiceUrl(RpcMethodInfo method) {
        String nodeAddress = registryService.getServiceUrl( this.server, Strings.EMPTY );
        return nodeAddress + method.getPath();
    }

    private synchronized void initNodes() {
        if (this.nodes != null) {
            return;
        }

        List<Address> addresses = resolve();
        if (addresses.isEmpty()) {
            throw new RpcException(RpcError.CLIENT_NO_PROVIDER, server);
        }

        this.updateNodes(addresses);
    }

    private List<Address> resolve() {
        List<Address> list = new ArrayList<>();
        //获取节点地址列表

        return list;
    }

    /**
     * 刷新节点
     */
    private void updateNodes(List<Address> addresses) {
        List<Node> list;
        if (addresses.isEmpty()) {
            list = Collections.emptyList();
        } else {
            list = addresses.stream().map(this::createNode).collect(Collectors.toList());
            for (int i = 0; i < list.size() - 1; i++) {
                list.get(i).setNext(list.get(i + 1));
            }
            list.get(list.size() - 1).setNext(list.get(0));
        }

        this.nodes = list;
    }

    private Node createNode(Address address) {
        return new Node(address, options);
    }

}
