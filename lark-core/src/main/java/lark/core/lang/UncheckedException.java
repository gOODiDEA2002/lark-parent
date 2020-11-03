package lark.core.lang;

/**
 * 把 checked 异常转换成 unchecked 异常的包装类
 *
 * Created by guohua.cui on 16/8/30.
 */
public class UncheckedException extends RuntimeException {
    public UncheckedException() {
        super();
    }

    public UncheckedException(String msg) {
        super(msg);
    }

    public UncheckedException(Throwable inner) {
        super(inner);
    }

    public UncheckedException(String msg, Throwable inner) {
        super(msg, inner);
    }
}
