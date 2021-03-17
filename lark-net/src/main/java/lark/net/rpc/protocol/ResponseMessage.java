package lark.net.rpc.protocol;

import lark.core.lang.BusinessException;
import lark.core.lang.DataException;
import lark.core.util.Strings;
import lark.net.rpc.RpcError;
import lark.net.rpc.protocol.data.SimpleEncoder;
import lark.net.rpc.protocol.data.SimpleValue;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class ResponseMessage {
    private boolean success;
    private SimpleValue result;
    private String errorInfo;
    private long serverTime;
    private long executeTime;
    private int errorCode;
    private String errorDetail;
    private Object errorData;

    public void fillResult(Object result) {
        this.success = true;
        this.result = SimpleEncoder.encode(result);
    }

    public void fillError(Throwable e) throws UnsupportedEncodingException {
        this.success = false;
        if (e instanceof DataException) {
            this.errorCode = ((DataException) e).getCode();
            this.errorDetail = URLEncoder.encode(((DataException) e).getDetail(), "UTF-8");
            this.errorData = ((DataException) e).getData();
        } else if (e instanceof BusinessException) {
            this.errorCode = ((BusinessException) e).getCode();
            this.errorDetail = URLEncoder.encode(((BusinessException) e).getDetail(), "UTF-8");
        } else {
            this.errorCode = RpcError.SERVER_UNKNOWN_ERROR.value();
        }

        this.errorInfo = e.getMessage();
        if (Strings.isEmpty(this.errorInfo)) {
            this.errorInfo = e.toString();
        }
    }

    public void fillError(RpcError re) {
        this.success = false;
        this.errorCode = re.value();
        this.errorInfo = re.message();
    }
}
