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
    private static final int TIMEOUT = 30;
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient.Builder().
            connectTimeout(TIMEOUT, TimeUnit.SECONDS).
            readTimeout(TIMEOUT, TimeUnit.SECONDS).
            writeTimeout( TIMEOUT, TimeUnit.SECONDS ).
            build();
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final Logger log = LoggerFactory.getLogger(Client.class);
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
//        if (this.nodes == null) {
//            initNodes();
//        }
        String nodeAddress = registryService.getService( this.server, Strings.EMPTY );
        nodeAddress += method.getPath();
        //
        String bodyJson = Strings.EMPTY;
        if ( args != null && args.length > 0 ) {
            bodyJson = JsonCodec.encode( args[0] );
        }
        //
        RequestBody requestBody = RequestBody.create( JSON, bodyJson );
        Request request = new Request.Builder().url(nodeAddress).post(requestBody).build();
        log.info("===> Request begin: server: {}, url: {}, data: {}", server, nodeAddress, bodyJson );
        try (Response response = HTTP_CLIENT.newCall(request).execute()) {
            ResponseBody responseBody = response.body();
            if ( responseBody != null ) {
                String result = responseBody.string();
                log.info("===> Request end: server: {}, url: {}, result: {}", server, nodeAddress, result );
                return JsonCodec.decode( result, method.getReturnType());
            }
        } catch (Exception e) {
            log.error("Request failure: server: {}, url: {}, data: {} , Cause:{}", server, nodeAddress, bodyJson, e.getMessage() );
            throw new RpcException( RpcError.CLIENT_DEADLINE_EXCEEDED, e );
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
