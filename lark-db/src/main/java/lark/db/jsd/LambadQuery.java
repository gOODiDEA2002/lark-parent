package lark.db.jsd;

import cn.hutool.core.collection.CollUtil;
import lark.db.jsd.clause.DeleteClause;
import lark.db.jsd.clause.FromClause;
import lark.db.jsd.clause.SelectClause;
import lark.db.jsd.clause.WhereClause;
import lark.db.jsd.lambad.*;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import static lark.db.jsd.FilterType.EQ;
import static lark.db.jsd.FilterType.IN;
import static lark.db.jsd.Shortcut.c;
import static lark.db.jsd.Shortcut.f;

public class LambadQuery<T, M> {

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
    public M one(SelectFilter<T, M> selectFilter) {
        BasicFilter basicFilter = selectFilter.select();
        FromClause fromClause = new SelectContext(this.manager, builder, this.entityClass);
        return (M) fromClause.where(basicFilter).groupBy(selectFilter.group()).orderBy(selectFilter.order()).result().one(this.entityClass);
    }

    public List<M> list(SelectFilter<T, M> selectFilter) {
        BasicFilter basicFilter = selectFilter.select();
        FromClause fromClause = new SelectContext(this.manager, builder, this.entityClass);
        return (List<M>) fromClause.where(basicFilter).groupBy(selectFilter.group()).orderBy(selectFilter.order()).result().all(this.entityClass);
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
        return new InsertContext(this.manager, this.builder, entity).result().getAffectedRows();
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


    public int insert(Collection<? extends Serializable> list) {
        return new InsertContext(this.manager, this.builder, list).result().getAffectedRows();
    }

    public int updateById(Object entity) {
        return new UpdateContext(this.manager, builder, entity).result().getAffectedRows();
    }

    public int updateByIds(Collection<? extends Serializable> collection) {
        int i = 0;
        if (CollUtil.isNotEmpty(collection)) {
            for (Serializable serializable : collection) {
                i += updateById(serializable);
            }
        }
        return i;
    }

    public int update(UpdateFilter<T, ?> updateFilter) {
        UpdateContext updateContext = new UpdateContext(this.manager, builder, this.entityClass, true);
        return updateContext.set(updateFilter.set()).where(updateFilter.select()).result().getAffectedRows();
    }


    public int delete(DeleteFilter<T, ?> deleteFilter) {
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

    public PageEntity<M> page(int pageIndex, int pageSize, SelectFilter<T, M> selectFilter) {
        FromClause fromClause = new SelectContext(this.manager, builder, this.entityClass);
        BasicFilter build = selectFilter.select();
        PageEntity<M> pageEntity = (PageEntity<M>) fromClause.where(build).groupBy(selectFilter.group()).orderBy(selectFilter.order()).page(pageIndex, pageSize).page().pageResult(this.entityClass);
        return pageEntity;
    }


    public int count(SelectFilter<T, M> compareFilter) {
        FromClause fromClause = new SelectContext(this.manager, builder, this.entityClass, Shortcut.count());
        return fromClause.where(compareFilter.select()).result().one(Integer.class);
    }
}
