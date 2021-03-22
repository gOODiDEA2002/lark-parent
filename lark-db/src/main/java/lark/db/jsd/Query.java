package lark.db.jsd;

import lark.db.jsd.clause.*;
import lark.db.jsd.template.SqlTemplate;
import lark.db.jsd.lambad.CompareFilter;

import java.util.List;

/**
 * Created by guohua.cui on 16/3/31.
 */
public interface Query {
    /**
     * 插入操作
     *
     * @param table 表名
     * @return
     */
    InsertClause insert(String table);

    /**
     * 插入操作
     *
     * @param obj 实体对象
     * @return
     */
    InsertEndClause insert(Object obj);

    /**
     * 插入操作
     *
     * @param objects 实体对象列表
     * @return
     */
    <T> InsertEndClause insert(List<T> objects);

    /**
     * 删除操作
     *
     * @param table 表名
     * @return
     */
    DeleteClause delete(String table);

    /**
     * 删除操作
     *
     * @param obj 要删除的对象
     * @return
     */
    DeleteEndClause delete(Object obj);

    /**
     * 更新操作
     *
     * @param table 表名
     * @return
     */
    UpdateClause update(String table);

    /**
     * 更新操作
     *
     * @param obj 实体对象
     * @return
     */
    UpdateEndClause update(Object obj);

    /**
     * 更新操作
     *
     * @param obj     实体对象
     * @param columns 更新列
     * @return
     */
    UpdateEndClause update(Object obj, String... columns);

    /**
     * 查询操作
     *
     * @param column 查询返回列
     * @return
     */
    SelectClause select(String column);

    /**
     * @description: TODO
     * @return:
     * @author: yandong
     * @date: 2021/3/18 5:54 下午
     */
    <T> T one(CompareFilter<T> CompareFilter);

    /**
     * 查询操作
     *
     * @param columns 查询返回列
     * @return
     */
    SelectClause select(String... columns);

    /**
     * 查询操作
     *
     * @param columns 查询返回列
     * @return
     */
    SelectClause select(Columns columns);

    /**
     * 查询操作
     *
     * @param clazz 查询映射对象类型
     * @return
     */
    FromClause select(Class<?> clazz);

    /**
     * 查询操作
     *
     * @param obj 实体对象
     * @return
     */
    SelectEndClause select(Object obj);


    /**
     * 直接执行 SQL 语句
     *
     * @param sql  SQL 语句
     * @param args 参数
     * @return
     */
    ExecuteClause execute(String sql, Object... args);

    /**
     * 直接执行 SQL 语句
     *
     * @param tpl SQL 模板
     * @param arg 参数, Map 或 POJO 对象
     * @return
     */
    ExecuteClause execute(SqlTemplate tpl, Object arg);

    /**
     * 调用存储过程
     *
     * @param sp 存储过程名称
     * @return
     */
    CallClause call(String sp);

//    /**
//     * 预编译 SQL 脚本
//     * @param sql
//     * @return
//     */
//    Object prepare(String sql);

    /**
     * 获取分片表查询对象
     *
     * @param name 基础表名
     * @param keys 分片参数
     * @return
     */
    TableQuery table(String name, Object... keys);

    /**
     * 获取分片表查询对象
     *
     * @param obj 数据库实体对象
     * @return
     */
    TableQuery table(Object obj);


    <T> LambadQuery<T> lambadQuery(Class<?> cla);


}
