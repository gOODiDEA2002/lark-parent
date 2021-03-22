package lark.db.jsd;

import cn.hutool.core.util.ObjectUtil;
import lark.db.jsd.clause.SetClause;
import lark.db.jsd.clause.UpdateClause;
import lark.db.jsd.clause.UpdateEndClause;
import lark.db.jsd.result.BuildResult;
import lark.db.jsd.result.SimpleResult;

/**
 * 更新操作上下文
 * Created by guohua.cui on 15/5/11.
 */
public class UpdateContext implements UpdateClause, SetClause, UpdateEndClause {
    private ConnectionManager manager;
    private Builder builder;
    private UpdateInfo info;

    UpdateContext(ConnectionManager manager, Builder builder, String table) {
        this.manager = manager;
        this.builder = builder;
        this.info = new UpdateInfo(table);
    }


    UpdateContext(ConnectionManager manager, Builder builder, Object obj, String... columns) {
        this(manager, builder, null, obj, null, columns);
    }

    UpdateContext(ConnectionManager manager, Builder builder, Object obj, Boolean bo, String... columns) {
        this(manager, builder, null, obj, bo, columns);
    }

    UpdateContext(ConnectionManager manager, Builder builder, String table, Object obj, String... columns) {
        this(manager, builder, null, obj, null, columns);
    }


    UpdateContext(ConnectionManager manager, Builder builder, String table, Object obj, Boolean bo, String... columns) {
        this.manager = manager;
        this.builder = builder;

        Mapper.EntityInfo entityInfo = Mapper.getEntityInfo(obj.getClass());
        this.info = new UpdateInfo(table == null ? entityInfo.table : table);

        UpdateValues values = new UpdateValues();
        String[] updateColumns = (columns == null || columns.length == 0) ? entityInfo.getUpdateColumns() : columns;
        for (String col : updateColumns) {
            Object value = entityInfo.getValue(obj, col);
            if (bo == null || bo == false) {
                if (ObjectUtil.isNotEmpty(value)) {
                    values.add(col, value);
                }
            } else {
                values.add(col, value);
            }


        }
        this.info.values = values;

        BasicFilter filter = Filter.create();
        String[] idColumns = entityInfo.getIdColumns();
        if (idColumns == null) {
            throw new JsdException(String.format("实体类型 %s 没有定义 ID 列", obj.getClass().getName()));
        }
        for (String col : idColumns) {
            filter.add(col, entityInfo.getValue(obj, col));
        }
        this.info.where = filter;
    }

    @Override
    public SetClause set(UpdateValues values) {
        this.info.values = values;
        return this;
    }

    @Override
    public UpdateEndClause where(Filter filter) {
        this.info.where = filter;
        return this;
    }

    @Override
    public BuildResult print() {
        BuildResult result = this.builder.buildUpdate(this.info);
        return result;
    }

    @Override
    public SimpleResult result() {
        BuildResult result = this.builder.buildUpdate(this.info);
        Debug.log(result);
        return SimpleResult.of(manager, result);
    }

    static class UpdateInfo {
        String table;
        Filter where;
        UpdateValues values = new UpdateValues();

        UpdateInfo(String table) {
            this.table = table;
        }
    }
}
