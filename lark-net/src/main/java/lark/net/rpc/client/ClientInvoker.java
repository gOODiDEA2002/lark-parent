package lark.net.rpc.client;

import lark.core.codec.JsonCodec;
import lark.core.lang.BusinessException;
import lark.net.rpc.RpcError;
import lark.net.rpc.RpcException;
import lark.net.rpc.protocol.RequestMessage;
import lark.net.rpc.protocol.ResponseMessage;
import lark.net.rpc.protocol.data.SimpleEncoder;
import lark.net.rpc.protocol.data.SimpleValue;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ClientInvoker {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientInvoker.class);
    private static final int TIMEOUT = 30;
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient.Builder().
            connectTimeout(TIMEOUT, TimeUnit.SECONDS).
            readTimeout(TIMEOUT, TimeUnit.SECONDS).
            writeTimeout( TIMEOUT, TimeUnit.SECONDS ).
            build();
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public static Object invoke( String url, String service, String method, Object[] args, Class<?> returnType) {
        String requestData = toRequest( service, method, args );
        //
        RequestBody requestBody = RequestBody.create( JSON, requestData );
        Request request = new Request.Builder().url(url).post(requestBody).build();
        LOGGER.info("===> Request begin: url: {}, data: {}", url, requestData );
        try (Response response = HTTP_CLIENT.newCall(request).execute()) {
            ResponseBody responseBody = response.body();
            if ( responseBody != null ) {
                String result = responseBody.string();
                LOGGER.info("===> Request end: url: {}, result: {}", url, result );
                return toResult( result, returnType );
            }
        } catch (Exception e) {
            LOGGER.error("Request failure: url: {}, data: {} , Cause:{}", url, requestData, e.getMessage() );
            throw new RpcException( RpcError.CLIENT_DEADLINE_EXCEEDED, e );
        }
        return null;
    }

    private static String toRequest( String service, String method, Object[] args ) {
        RequestMessage request = new RequestMessage();
        request.setService( service );
        request.setMethod( method );
        if ( args != null && args.length > 0 ) {
            List<SimpleValue> params = new ArrayList<>();
            for (Object arg : args) {
                params.add(SimpleEncoder.encode(arg));
            }
            request.setArgs( params );
        }
        return JsonCodec.encode( request );
    }

    private static Object toResult( String result, Class<?> returnType ) {
        ResponseMessage reponse = JsonCodec.decode( result, ResponseMessage.class );
        if ( reponse.isSuccess() ) {
            return reponse.getResult();
        }
        //
        throw new BusinessException( reponse.getErrorCode(), reponse.getErrorInfo(), reponse.getErrorDetail() );
    }
}
