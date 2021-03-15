package lark.db.jsd.clause;

/**
 * Created by guohua.cui on 15/5/8.
 */
public interface InsertClause {
    /**
     * 设置要插入记录的数据列
     *
     * @param columns 数据列
     * @return
     */
    ColumnsClause columns(String... columns);

    /**
     * 设置要插入记录的数据值
     *
     * @param values 数据列值
     * @return
     */
    ValuesClause values(Object... values);
}

