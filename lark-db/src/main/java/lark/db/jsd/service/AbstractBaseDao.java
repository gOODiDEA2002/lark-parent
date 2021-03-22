package lark.db.jsd.service;


import lark.db.jsd.lambad.*;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface AbstractBaseDao<T> {

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

    int update(UpdateFilter<T> CompareFilter);

    int delete(DeleteFilter<T> deleteFilter);


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
    T selectOne(SelectFilter<T> CompareFilter);

    /**
     * 根据条件查询多个
     *
     * @description: TODO
     * @return:
     * @author: yandong
     * @date: 2021/3/19 3:55 下午
     */
    List<T> selectList(SelectFilter<T> CompareFilter);


    /**
     * 分页
     *
     * @description: TODO
     * @return:
     * @author: yandong
     * @date: 2021/3/19 10:02 上午
     */
    PageEntity<T> page(PageVO pageVO, SelectFilter<T> CompareFilter);


}
