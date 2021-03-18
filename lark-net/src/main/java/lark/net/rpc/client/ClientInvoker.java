package lark.net.rpc.client;

import lark.core.lang.DataException;
import lark.net.rpc.RpcError;
import lark.net.rpc.RpcException;
import lark.net.rpc.protocol.RequestProcess;
import lark.net.rpc.protocol.ResponseProcess;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.Introspector;
import java.util.concurrent.TimeUnit;

public class ClientInvoker {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientInvoker.class);
    private static final int TIMEOUT = 30;
    private static final MediaType JSON = MediaType.get("application/json;charset=utf-8");

    public static Object invoke(String url, String service, String method, Object[] args, Class<?> returnType) {
        String requestData = RequestProcess.to(service, method, args);
        //
        RequestBody requestBody = RequestBody.create(JSON, requestData);
        Request request = new Request.Builder().url(url).post(requestBody).build();
        LOGGER.info("===> Request begin: url: {}, data: {}", url, requestData);
        //
        OkHttpClient HTTP_CLIENT = new OkHttpClient.Builder().
                connectTimeout(TIMEOUT, TimeUnit.SECONDS).
                readTimeout(TIMEOUT, TimeUnit.SECONDS).
                writeTimeout(TIMEOUT, TimeUnit.SECONDS).
                build();
        //
        try (Response response = HTTP_CLIENT.newCall(request).execute()) {
            try (ResponseBody responseBody = response.body()) {
                if (responseBody != null) {
                    String responseData = responseBody.string();
                    LOGGER.info("===> Request end: url: {}, result: {}", url, responseData);
                    return toResult(responseData, returnType);
                }
            }
        } catch (DataException e) {
            LOGGER.error("Request failure: url: {}, data: {} , Cause:{}", url, requestData, e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.error("Request failure: url: {}, data: {} , Cause:{}", url, requestData, e.getMessage());
            throw new RpcException(RpcError.CLIENT_DEADLINE_EXCEEDED, e);
        }
        return null;
    }

    public static Object toResult(String responseData, Class<?> returnType) {
        ResponseProcess process = ResponseProcess.from(responseData);
        if (process.isSuccess()) {
            return process.getResult(returnType);
        }
        DataException dataException = process.getDataException();
        if (dataException != null) {
            throw dataException;
        }

        throw process.getError();
    }
}
