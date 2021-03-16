package lark.net.rpc.protocol;

import lark.core.codec.JsonCodec;
import lark.core.lang.BusinessException;
import lark.core.util.Strings;
import lark.net.rpc.RpcError;
import lark.net.rpc.protocol.data.SimpleEncoder;
import lark.net.rpc.protocol.data.SimpleValue;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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

    public void fillResult(Object result) {
        this.success = true;
        this.result = SimpleEncoder.encode(result);
    }

    public void fillError(Throwable e) {
        this.success = false;
        if (e instanceof BusinessException) {
            this.errorCode = ((BusinessException) e).getCode();
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
