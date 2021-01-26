package lark.task.xxl.job.util;

import lark.core.codec.JsonCodec;
import lark.core.lang.FatalException;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class XxlJobHttpClient {
    private static final int TIMEOUT = 30;
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient.Builder().
            connectTimeout(TIMEOUT, TimeUnit.SECONDS).
            readTimeout(TIMEOUT, TimeUnit.SECONDS).
            writeTimeout( TIMEOUT, TimeUnit.SECONDS ).
            build();
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final Logger log = LoggerFactory.getLogger(XxlJobHttpClient.class);
    private final String address;
    private final String accessToken;

    public XxlJobHttpClient( String address, String accessToken ) {
        this.address = address;
        this.accessToken = accessToken;
    }

    public <T> T request( String path, String json, Class<T> clazz ) {
        RequestBody requestBody = RequestBody.create( JSON, json );
        Request request = new Request.Builder().
                addHeader( "XXL-JOB-ACCESS-TOKEN", accessToken ).
                url( address + path ).
                post(requestBody).
                build();
        try (Response response = HTTP_CLIENT.newCall(request).execute()) {
            ResponseBody responseBody = response.body();
            if ( responseBody != null ) {
                String result = responseBody.string();
                return JsonCodec.decode( result, clazz);
            }
        } catch (Exception e) {
            log.error("Request failure: address: {}, path: {}, request: {} , Cause:{}", address, path, json, e.getMessage() );
            throw new FatalException( e );
        }
        return null;
    }
}
