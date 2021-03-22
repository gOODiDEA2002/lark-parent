package lark.db.jsd.lambad;

import lark.db.jsd.Database;
import lark.db.jsd.LambadQuery;
import lark.db.jsd.Transaction;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class AbstractDaoImpl<T> implements AbstractDao<T> {

    private Class<T> entityClass;

    public AbstractDaoImpl() {
        this.entityClass = getEntityClass();
    }

    /**
     * 获取db
     *
     * @description: TODO
     * @return:
     * @author: yandong
     * @date: 2021/3/19 10:50 上午
     */
    public abstract Database dataBase();


    private <T> LambadQuery<T> lambadQuery(Class<?> cla) {
        return dataBase().lambadQuery(cla);
    }

    private <T> LambadQuery<T> getLambadQuery() {
        return lambadQuery(this.entityClass);
    }

    private Class<T> getEntityClass() {
        Class<T> tClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return tClass;
    }

    /**
     * 单个插入
     *
     * @description: TODO
     * @return:
     * @author: yandong
     * @date: 2021/3/19 3:09 下午
     */
    @Override
    public int insert(T entity) {
        LambadQuery<T> objectLambadQuery = getLambadQuery();
        return objectLambadQuery.insert(entity);
    }

    /**
     * 批量插入
     *
     * @description: TODO
     * @return:
     * @author: yandong
     * @date: 2021/3/19 3:09 下午
     */
    @Override
    public int insert(Collection<? extends Serializable> collection) {
        LambadQuery<T> objectLambadQuery = getLambadQuery();
        return objectLambadQuery.insert( collection);
    }

    @Override
    public int saveOrUpdateById(T entity) {
        LambadQuery<T> objectLambadQuery = getLambadQuery();
        return objectLambadQuery.saveOrUpdateById(entity);
    }


    /**
     * 根据id修改
     *
     * @description: TODO
     * @return:
     * @author: yandong
     * @date: 2021/3/19 3:09 下午
     */
    @Override
    public int updateById(Object entity) {
        LambadQuery<T> objectLambadQuery = getLambadQuery();
        return objectLambadQuery.updateById(entity);
    }

    public int updateByIds(Collection<? extends Serializable> collection) {
        LambadQuery<T> objectLambadQuery = getLambadQuery();
        return objectLambadQuery.updateByIds(collection);
    }

    public int update(UpdateFilter<T> CompareFilter) {
        LambadQuery<T> objectLambadQuery = getLambadQuery();
        return objectLambadQuery.update(CompareFilter);
    }

    ;

    public int delete(DeleteFilter<T> CompareFilter) {
        LambadQuery<T> objectLambadQuery = getLambadQuery();
        return objectLambadQuery.delete(CompareFilter);
    }

    ;


    @Override
    public int deleteById(Serializable id) {
        LambadQuery<T> objectLambadQuery = getLambadQuery();
        return objectLambadQuery.deleteById(id);
    }

    @Override
    public int deleteByIds(Collection<? extends Serializable> ids) {
        LambadQuery<T> objectLambadQuery = getLambadQuery();
        return objectLambadQuery.deleteByIds(ids);
    }


    @Override
    public T selectById(Serializable id) {
        LambadQuery<T> objectLambadQuery = getLambadQuery();
        return objectLambadQuery.selectById(id);
    }

    @Override
    public List<T> selectByIds(Collection<? extends Serializable> collections) {
        LambadQuery<T> objectLambadQuery = getLambadQuery();
        return objectLambadQuery.selectByIds(collections);
    }

    @Override
    public T selectOne(SelectFilter<T> CompareFilter) {
        LambadQuery<T> objectLambadQuery = getLambadQuery();
        return objectLambadQuery.one(CompareFilter);
    }

    @Override
    public List<T> selectList(SelectFilter<T> CompareFilter) {
        LambadQuery<T> objectLambadQuery = getLambadQuery();
        return objectLambadQuery.list(CompareFilter);
    }


    @Override
    public PageEntity<T> page(Pager pager, SelectFilter<T> CompareFilter) {
        LambadQuery<T> objectLambadQuery = getLambadQuery();
        return objectLambadQuery.page(pager.getPageIndex(), pager.getPageSize(), CompareFilter);
    }


    @Override
    public Transaction begin() {
        return dataBase().begin();
    }

    @Override
    public void begin(Consumer<Transaction> action) {
        dataBase().begin(action);
    }

    @Override
    public <T> T begin(Function<Transaction, T> func) {
        return dataBase().begin(func);
    }

    @Override
    public <T> T begin(Function<Transaction, T> func, boolean setContext) {
        return dataBase().begin(func, setContext);
    }

    @Override
    public void begin(Consumer<Transaction> action, boolean setContext) {
        dataBase().begin(action, setContext);
    }
}
