package lark.db.jsd;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 数据库
 * Created by guohua.cui on 15/5/8.
 */
public interface Database extends Query {
    /**
     * 启动一个事务
     *
     * @return
     */
    Transaction begin();

    /**
     * 启动一个事务
     *
     * @param action 事务操作
     * @return
     */
    void begin(Consumer<Transaction> action);

    /**
     * 启动一个事务
     *
     * @param func 事务操作
     * @return 操作结果
     */
    <T> T begin(Function<Transaction, T> func);

    /**
     * 启动一个事务
     *
     * @param func 事务操作
     * @return 操作结果
     */
    <T> T begin(Function<Transaction, T> func, boolean setContext);

    /**
     * 启动一个事务
     *
     * @param action     事务操作
     * @param setContext 是否设置上下文事务
     * @return
     */
    void begin(Consumer<Transaction> action, boolean setContext);

}
