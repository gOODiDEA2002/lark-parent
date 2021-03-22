package lark.db.jsd;

import cn.hutool.core.collection.CollectionUtil;
import lark.db.jsd.clause.DeleteClause;
import lark.db.jsd.clause.FromClause;
import lark.db.jsd.util.PageEntity;
import lark.db.jsd.util.QueryFilter;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import static lark.db.jsd.FilterType.EQ;
import static lark.db.jsd.FilterType.IN;
import static lark.db.jsd.Shortcut.f;

public class LambadQuery<T> {


//    public Class<T> getTClass() {
//        Class<T> tClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
//        return tClass;
//    }

    private ConnectionManager manager;

    private Builder builder;

    private Class<?> entityClass;

    LambadQuery(Class<?> entityClass, ConnectionManager manager, Builder builder) {
        this.manager = manager;
        this.builder = builder;
        this.entityClass = entityClass;
    }


    public <T> QueryFilter lambadFilter() {
        return new QueryFilter(entityClass);
    }

    /**
     * 获取单个
     *
     * @description: TODO
     * @return:
     * @author: yandong
     * @date: 2021/3/19 10:14 上午
     */
    public T one(QueryFilter<T> queryFilter) {
        BasicFilter basicFilter = queryFilter.build();
        FromClause fromClause = new SelectContext(this.manager, builder, this.entityClass);
        return (T) fromClause.where(basicFilter).result().one(this.entityClass);
    }

    public List<T> list(QueryFilter<T> queryFilter) {
        BasicFilter basicFilter = queryFilter.build();
        FromClause fromClause = new SelectContext(this.manager, builder, this.entityClass);
        return (List<T>) fromClause.where(basicFilter).result().all(this.entityClass);
    }

    public T selectById(Serializable id) {
        FromClause fromClause = new SelectContext(this.manager, builder, this.entityClass);
        return (T) fromClause.where(f("id", EQ, id)).result().one(this.entityClass);
    }

    public List<T> selectByIds(Collection<? extends Serializable> ids) {
        FromClause fromClause = new SelectContext(this.manager, builder, this.entityClass);
        return (List<T>) fromClause.where(f("id", IN, ids.toArray())).result().all(this.entityClass);
    }


    public int insert(Object entity) {
        List<Object> keys = new InsertContext(this.manager, this.builder, entity).result().getKeys();
        if (CollectionUtil.isNotEmpty(keys)) {
            return keys.size();
        }
        return 0;
    }


    public int saveOrUpdateById(Object entity) {
        Mapper.EntityInfo entityInfo = Mapper.getEntityInfo(entity.getClass());
        Object value = entityInfo.getValue(entity, "id");
        T selectById = selectById(value.toString());
        if (selectById != null) {
            //修改
            return updateById(entity);
        } else {
            return insert(entity);
        }
    }


    public int insert(Collection<? extends Serializable> entitys) {
        return new InsertContext(this.manager, this.builder, entitys).result().getKeys().size();
    }

    public int updateById(Object entity) {
        UpdateContext updateContext = new UpdateContext(this.manager, builder, entity);
        return updateContext.result().getAffectedRows();
    }

    public int deleteById(Serializable id) {
        DeleteClause deleteContext = new DeleteContext(this.manager, builder, this.entityClass);
        return deleteContext.where(f("id", EQ, id)).result().getAffectedRows();
    }

    public int deleteByIds(Collection<? extends Serializable> ids) {
        DeleteClause deleteContext = new DeleteContext(this.manager, builder, this.entityClass);
        return deleteContext.where(f("id", IN, ids.toArray())).result().getAffectedRows();
    }

    public PageEntity<T> page(int pageIndex, int pageSize, QueryFilter<T> queryFilter) {
        FromClause fromClause = new SelectContext(this.manager, builder, this.entityClass);
        BasicFilter build = queryFilter.build();
        PageEntity<T> pageEntity = (PageEntity<T>) fromClause.where(build).page(pageIndex, pageSize).page().pageResult(this.entityClass);
        return pageEntity;
    }


}
