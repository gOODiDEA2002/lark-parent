package lark.util.lock;

/**
 * Created by Andy Yuan on 2020/11/3.
 */
public interface ProcessHandle<T> {
    /**
     * @return
     */
    T execute();
}