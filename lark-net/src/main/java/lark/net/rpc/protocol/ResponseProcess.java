package lark.net.rpc.protocol;

import lark.core.codec.JsonCodec;
import lark.core.lang.BusinessException;
import lark.core.lang.DataException;
import lark.core.util.Strings;
import lark.net.rpc.RpcError;
import lark.net.rpc.protocol.data.SimpleEncoder;

import java.io.UnsupportedEncodingException;

public class ResponseProcess {
    private ResponseMessage message;

    public ResponseProcess() {
        this.message = new ResponseMessage();
    }

    public ResponseProcess(ResponseMessage message) {
        this.message = message;
    }

    public static ResponseProcess from(String responseData) {
        if (Strings.isEmpty(responseData)) {
            return null;
        }
        //
        ResponseMessage message = JsonCodec.decode(responseData, ResponseMessage.class);
        return new ResponseProcess(message);
    }


    public Object getResult(Class<?> returnType) {
        return SimpleEncoder.decode(this.message.getResult(), returnType);
    }

    public String toMessage() {
        return JsonCodec.encode(message);
    }

    public void setServerTime(long serverTime) {
        message.setServerTime(serverTime);
    }

    public void setExecuteTime(long executeTime) {
        message.setExecuteTime(executeTime);
    }

    public boolean isSuccess() {
        return message.isSuccess();
    }

    public void fillResult(Object result) {
        this.message.fillResult(result);
    }

    public void fillError(Throwable e) {
        try {
            this.message.fillError(e);
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
            unsupportedEncodingException.printStackTrace();
        }
    }

    public void fillError(RpcError re) {
        this.message.fillError(re);
    }

    public DataException getDataException() {
        String errorInfo = message.getErrorInfo();
        if (errorInfo.contains("DataException")) {
            return new DataException(message.getErrorCode(), message.getErrorDetail());
        }
        return null;
    }

    public BusinessException getError() {
        return new BusinessException(message.getErrorCode(), message.getErrorInfo(), message.getErrorDetail());
    }
}
