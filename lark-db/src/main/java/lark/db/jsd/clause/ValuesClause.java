package lark.db.jsd.clause;

/**
 * Created by guohua.cui on 15/5/13.
 */
public interface ValuesClause extends InsertEndClause {
    ValuesClause values(Object... values);
}
