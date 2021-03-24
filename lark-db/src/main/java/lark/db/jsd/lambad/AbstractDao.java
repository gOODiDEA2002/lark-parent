package lark.db.jsd.lambad;


import lark.db.jsd.Transaction;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public interface AbstractDao<T> {

    /**
     * 插入一条数据
     *
     * @param entity
     * @return
     */
    int insert(T entity);

    /**
     * 插入多条数据
     *
     * @param collection
     * @return
     */
    int insert(Collection<? extends Serializable> collection);


    int saveOrUpdateById(T entity);

    /**
     * 根据id修改
     *
     * @param id
     * @return
     */
    int updateById(Object id);


    int updateByIds(Collection<? extends Serializable> id);

    int update(UpdateFilter<T, ?> CompareFilter);


    int delete(DeleteFilter<T, ?> deleteFilter);


    /**
     * 根据id删除
     *
     * @description: TODO
     * @return:
     * @author: yandong
     * @date: 2021/3/19 9:58 上午
     */
    int deleteById(Serializable id);

    /**
     * 根据ids 批量删除
     *
     * @description: TODO
     * @return:
     * @author: yandong
     * @date: 2021/3/19 3:49 下午
     */
    int deleteByIds(Collection<? extends Serializable> ids);


    <M> int count(SelectFilter<T, M> CompareFilter);


    /**
     * 根据id查询
     *
     * @description: TODO
     * @return:
     * @author: yandong
     * @date: 2021/3/19 9:59 上午
     */
    T selectById(Serializable id);


    /**
     * 根据ids 批量查询
     *
     * @description: TODO
     * @return:
     * @author: yandong
     * @date: 2021/3/19 10:00 上午
     */
    List<T> selectByIds(Collection<? extends Serializable> collections);

    /**
     * 根据条件查询单个
     *
     * @description: TODO
     * @return:
     * @author: yandong
     * @date: 2021/3/19 3:54 下午
     */
    <M> M selectOne(SelectFilter<T, M> CompareFilter);

    /**
     * 根据条件查询多个
     *
     * @description: TODO
     * @return:
     * @author: yandong
     * @date: 2021/3/19 3:55 下午
     */
    <M> List<M> selectList(SelectFilter<T, M> CompareFilter);


    /**
     * 分页
     *
     * @description: TODO
     * @return:
     * @author: yandong
     * @date: 2021/3/19 10:02 上午
     */
    <M> PageEntity<M> page(Pager pager, SelectFilter<T, M> CompareFilter);


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
