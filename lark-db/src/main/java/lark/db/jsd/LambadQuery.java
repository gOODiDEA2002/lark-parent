package lark.db.jsd;

import cn.hutool.core.collection.CollectionUtil;
import lark.db.jsd.clause.DeleteClause;
import lark.db.jsd.clause.FromClause;
import lark.db.jsd.lambad.*;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import static lark.db.jsd.FilterType.EQ;
import static lark.db.jsd.FilterType.IN;
import static lark.db.jsd.Shortcut.f;

public class LambadQuery<T> {

    private ConnectionManager manager;

    private Builder builder;

    private Class<?> entityClass;


    LambadQuery(Class<?> entityClass, ConnectionManager manager, Builder builder) {
        this.manager = manager;
        this.builder = builder;
        this.entityClass = entityClass;
    }

    /**
     * 获取单个
     *
     * @description: TODO
     * @return:
     * @author: yandong
     * @date: 2021/3/19 10:14 上午
     */
    public T one(SelectFilter<T> selectFilter) {
        BasicFilter basicFilter = selectFilter.select();
        FromClause fromClause = new SelectContext(this.manager, builder, this.entityClass);
        return (T) fromClause.where(basicFilter).groupBy(selectFilter.group()).orderBy(selectFilter.order()).result().one(this.entityClass);
    }

    public List<T> list(SelectFilter<T> selectFilter) {
        BasicFilter basicFilter = selectFilter.select();
        FromClause fromClause = new SelectContext(this.manager, builder, this.entityClass);
        return (List<T>) fromClause.where(basicFilter).groupBy(selectFilter.group()).orderBy(selectFilter.order()).result().all(this.entityClass);
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

    public int update(UpdateFilter<T> updateFilter) {
        UpdateContext updateContext = new UpdateContext(this.manager, builder, this.entityClass);
        return updateContext.set(updateFilter.set()).where(updateFilter.select()).result().getAffectedRows();
    }


    public int delete(DeleteFilter<T> deleteFilter) {
        DeleteClause deleteContext = new DeleteContext(this.manager, builder, this.entityClass);
        return deleteContext.where(deleteFilter.select()).result().getAffectedRows();
    }


    public int deleteById(Serializable id) {
        DeleteClause deleteContext = new DeleteContext(this.manager, builder, this.entityClass);
        return deleteContext.where(f("id", EQ, id)).result().getAffectedRows();
    }

    public int deleteByIds(Collection<? extends Serializable> ids) {
        DeleteClause deleteContext = new DeleteContext(this.manager, builder, this.entityClass);
        return deleteContext.where(f("id", IN, ids.toArray())).result().getAffectedRows();
    }

    public PageEntity<T> page(int pageIndex, int pageSize, SelectFilter<T> selectFilter) {
        FromClause fromClause = new SelectContext(this.manager, builder, this.entityClass);

        BasicFilter build = selectFilter.select();
        PageEntity<T> pageEntity = (PageEntity<T>) fromClause.where(build).groupBy(selectFilter.group()).orderBy(selectFilter.order()).page(pageIndex, pageSize).page().pageResult(this.entityClass);
        return pageEntity;
    }


}
