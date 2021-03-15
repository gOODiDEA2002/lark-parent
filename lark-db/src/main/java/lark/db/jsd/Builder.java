package lark.db.jsd;

import lark.db.jsd.result.BuildResult;

/**
 * Created by guohua.cui on 15/5/12.
 */
public interface Builder {
    BuildResult buildInsert(InsertContext.InsertInfo info);
    BuildResult buildSelect(SelectContext.SelectInfo info);
    BuildResult buildUpdate(UpdateContext.UpdateInfo info);
    BuildResult buildDelete(DeleteContext.DeleteInfo info);
    BuildResult buildFilter(Filter filter);

    static Builder get(String provider) {
        switch (provider) {
            case "mysql":
                return new MysqlBuilder();
            case "mssql":
                return new MssqlBuilder();
            default:
                throw new JsdException("unknown provider type: " + provider);
        }
    }
}
