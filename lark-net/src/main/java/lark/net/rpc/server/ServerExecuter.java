package lark.net.rpc.server;

import lark.net.rpc.RpcError;
import lark.net.rpc.protocol.RequestMessage;
import lark.net.rpc.protocol.RequestProcess;
import lark.net.rpc.protocol.ResponseMessage;
import lark.net.rpc.protocol.ResponseProcess;

public class ServerExecuter {

    public static ResponseProcess execute( Server server, String requestData ) {
        ResponseProcess response = new ResponseProcess();
        RequestProcess request = null;
        try {
            request = RequestProcess.from( requestData );
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
            try {
                Object result = method.invoke( request.getArgs( method.getParameterTypes() ) );
                response.fillResult( result );
            } catch ( Exception e ) {
                response.fillError( e );
            }
        } else {
            response.fillError( RpcError.SERVER_SERVICE_NOT_FOUND );
        }
        return response;
    }
}
