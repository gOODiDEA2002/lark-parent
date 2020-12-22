package lark.net.rpc.client;

import lark.core.codec.JsonCodec;
import lark.net.Address;
import lark.net.rpc.RpcError;
import lark.net.rpc.RpcException;
import lark.net.rpc.nacos.NacosClient;
import okhttp3.*;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author cuigh
 */
public class Client {
    private static final NacosClient NACOS_CLIENT = new NacosClient();
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final Logger log = LoggerFactory.getLogger(Client.class);
    private String server;
    private ClientOptions options;
    private List<Node> nodes;

    public Client(String server, ClientOptions options) {
        Objects.requireNonNull(options);
        this.server = server;
        this.options = options;
    }

    public Object invoke(RpcMethodInfo method, Object[] args) {
//        if (this.nodes == null) {
//            initNodes();
//        }
        String nodeAddress = NACOS_CLIENT.getNodeAddress( this.server );
        nodeAddress += method.getPath();
        //
        String bodyJson = Strings.EMPTY;
        if ( args != null && args.length > 0 ) {
            bodyJson = JsonCodec.encode( args[0] );
        }
        //
        RequestBody requestBody = RequestBody.create( JSON, bodyJson );
        Request request = new Request.Builder().url(nodeAddress).post(requestBody).build();
        try (Response response = HTTP_CLIENT.newCall(request).execute()) {
            ResponseBody responseBody = response.body();
            if ( responseBody != null ) {
                String result = responseBody.string();
                return JsonCodec.decode( result, method.getReturnType());
            }
        } catch (IOException e) {
            log.error("Request failure: server: {}, url: {}, data: {} , Cause:{}", server, nodeAddress, bodyJson, e.getMessage() );
        }
        return null;
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
