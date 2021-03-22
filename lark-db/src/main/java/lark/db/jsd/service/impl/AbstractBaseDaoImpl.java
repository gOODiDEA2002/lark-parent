package lark.db.jsd.service.impl;

import lark.db.jsd.Database;
import lark.db.jsd.LambadQuery;
import lark.db.jsd.util.PageEntity;
import lark.db.jsd.service.AbstractBaseDao;
import lark.db.jsd.util.PageVO;
import lark.db.jsd.util.QueryFilter;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;

public abstract class AbstractBaseDaoImpl<T> implements AbstractBaseDao<T> {

    private Class<T> entityClass;

    public AbstractBaseDaoImpl() {
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
        return objectLambadQuery.insert(collection);
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
    public T selectOne(QueryFilter<T> queryFilter) {
        LambadQuery<T> objectLambadQuery = getLambadQuery();
        return objectLambadQuery.one(queryFilter);
    }

    @Override
    public List<T> selectList(QueryFilter<T> queryFilter) {
        LambadQuery<T> objectLambadQuery = getLambadQuery();
        return objectLambadQuery.list(queryFilter);
    }


    @Override
    public PageEntity<T> page(PageVO pageVO, QueryFilter<T> queryFilter) {
        LambadQuery<T> objectLambadQuery = getLambadQuery();
        return objectLambadQuery.page(pageVO.getPageIndex(), pageVO.getPageSize(), queryFilter);
    }


}
