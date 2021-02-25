package lark.net.rpc.server;

import lark.core.codec.JsonCodec;
import lark.net.rpc.RpcError;
import lark.net.rpc.protocol.RequestMessage;
import lark.net.rpc.protocol.ResponseMessage;
import lark.net.rpc.protocol.data.SimpleEncoder;
import lark.net.rpc.protocol.data.SimpleValue;

import java.util.List;

public class ServerExecuter {

    public static ResponseMessage execute( Server server, String requestData ) {
        ResponseMessage response = new ResponseMessage();
        RequestMessage request = null;
        try {
            request = toRequest( requestData );
        } catch ( Exception e ) {
            response.fillError( e );
        }
        //
        if ( request == null ) {
            return response;
        }
        //
        ServiceContainer serviceContainer = server.getContainer();
        ServiceMethod method = serviceContainer.getMethod( request.getService(), request.getMethod() );
        //
        if ( method != null ) {
            List<SimpleValue> params = request.getArgs();
            if ( params != null && params.size() > 0 ) {
                Object[] args = new Object[method.getParameterTypes().length];
                for (int i = 0; i < params.size(); i++) {
                    args[i] = SimpleEncoder.decode( params.get(i), method.getParameterTypes()[i] );
                }
                //
                try {
                    Object result = method.invoke( args );
                    response.fillResult( result );
                } catch ( Exception e ) {
                    response.fillError( e );
                }

            } else {
                response.fillError( RpcError.SERVER_UNKNOWN_PROTOCOL );
            }
        } else {
            response.fillError( RpcError.SERVER_SERVICE_NOT_FOUND );
        }
        return response;
    }

    private static RequestMessage toRequest( String requestData ) {
        RequestMessage request = JsonCodec.decode( requestData, RequestMessage.class );
        return request;
    }
}
