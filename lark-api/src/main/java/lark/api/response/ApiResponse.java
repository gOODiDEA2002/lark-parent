package lark.api.response;

import org.apache.logging.log4j.util.Strings;

/**
 *
 * @author Andy Yuan
 * @date 2020/7/18
 */
public class ApiResponse<T> {
    protected int code = 0;
    protected String msg = Strings.EMPTY;
    protected T data;

    public ApiResponse() {
    }

    public ApiResponse(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(T data) {
        this.data = data;
    }

    static class ErrorResponse extends ApiResponse {
        public ErrorResponse() {
            this.code = -1;
            this.msg = Strings.EMPTY;
            this.data = Strings.EMPTY;
        }
    }
}