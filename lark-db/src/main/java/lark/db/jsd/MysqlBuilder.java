package lark.db.jsd;

import cn.hutool.core.collection.CollUtil;
import lark.db.jsd.result.BuildResult;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static lark.db.jsd.FilterType.BETWEEN;
import static lark.db.jsd.FilterType.OR;

/**
 * MySQL 语句生成器
 * Created by guohua.cui on 15/5/19.
 */
public class MysqlBuilder implements Builder {

    @Override
    public BuildResult buildInsert(InsertContext.InsertInfo info) {
        BuildBuffer buffer = new BuildBuffer();

        buffer.addSql("INSERT INTO `%s`", info.getTable());

        // columns
        String[] columns = info.getColumns();
        if (columns != null && columns.length > 0) {
            buffer.addSql("(");
            for (int i = 0; i < columns.length; i++) {
                if (i > 0) buffer.addSql(",");
                buffer.addSql("`%s`", columns[i]);
            }
            buffer.addSql(")");
        }

        // values
        buffer.addSql(" VALUES");
        List<Object[]> values = info.getValues();
        for (int i = 0; i < values.size(); i++) {
            if (i > 0) buffer.addSql(",");

            Object[] args = values.get(i);
            buffer.addArg(args);

            buffer.addSql("(");
            buffer.addSql(args.length, "?", ",");
            buffer.addSql(")");
        }

        return buffer.getResult();
    }

    @Override
    public BuildResult buildDelete(DeleteContext.DeleteInfo info) {
        BuildBuffer buffer = new BuildBuffer();

        buffer.addSql("DELETE FROM `%s`", info.table);

        // where
        if (info.where != null) {
            BuildResult br = buildFilter(info.where);
            if (br != null) {
                buffer.addSql(" WHERE 1=1 ");
                buffer.addSql(br.getSql());
                buffer.addArg(br.getArgs());
            }
        }

        return buffer.getResult();
    }

    @Override
    public BuildResult buildUpdate(UpdateContext.UpdateInfo info) {
        BuildBuffer buffer = new BuildBuffer();

        buffer.addSql("UPDATE `%s` SET ", info.table);

        // columns
        Iterator<Map.Entry<String, UpdateValues.UpdateValue>> iter = info.values.iterator();
        int i = 0;
        while (iter.hasNext()) {
            if (i++ > 0) buffer.addSql(",");

            Map.Entry<String, UpdateValues.UpdateValue> entry = iter.next();
            switch (entry.getValue().getType()) {
                case INC:
                    buffer.addSql("`%s`=`%s`+?", entry.getKey(), entry.getKey());
                    buffer.addArg(entry.getValue().getValue());
                    break;
                case XP:
                    buffer.addSql("`%s`=%s", entry.getKey(), entry.getValue().getValue());
                    break;
                default:
                    buffer.addSql("`%s`=?", entry.getKey());
                    buffer.addArg(entry.getValue().getValue());
                    break;
            }
        }

        // where
        if (info.where != null) {
            BuildResult br = buildFilter(info.where);
            if (br != null) {
                buffer.addSql(" WHERE 1=1 ");
                buffer.addSql(br.getSql());
                buffer.addArg(br.getArgs());
            }
        }

        return buffer.getResult();
    }

    @Override
    public BuildResult buildSelect(SelectContext.SelectInfo info) {
        BuildBuffer buffer = new BuildBuffer();

        // SELECT
        buffer.addSql("SELECT ");
        if (info.distinct) {
            buffer.addSql("DISTINCT ");
        }
        for (int i = 0; i < info.columns.size(); i++) {
            if (i > 0) buffer.addSql(",");

            Columns.Column col = info.columns.get(i);
            if (col instanceof Columns.SimpleColumn) {
                Columns.SimpleColumn simpleColumn = (Columns.SimpleColumn) col;
                if (simpleColumn.table != null) {
                    buffer.addSql("`%s`.", simpleColumn.table.getPrefix());
                }
                buffer.addSql("`%s`", simpleColumn.column);
                if (simpleColumn.alias != null && !simpleColumn.alias.isEmpty()) {
                    buffer.addSql(" AS %s", simpleColumn.alias);
                }
            } else if (col instanceof Columns.ExprColumn) {
                Columns.ExprColumn exprColumn = (Columns.ExprColumn) col;
                buffer.addSql(exprColumn.expr);
                if (exprColumn.alias != null && !exprColumn.alias.isEmpty()) {
                    buffer.addSql(" AS %s", exprColumn.alias);
                }
            } else if (col instanceof Columns.PolyColumn) {
                // todo:
            }
        }

        // FROM
        buffer.addSql(" FROM `%s`", info.table.getName());
        String alias = info.table.getAlias();
        if (alias != null && !alias.isEmpty()) {
            buffer.addSql(" AS %s", alias);
        }

        // JOIN
        if (info.joins != null) {
            for (SelectContext.Joiner joiner : info.joins) {
                buffer.addSql(" %s `%s`", joiner.type.value, joiner.table.getName());
                alias = joiner.table.getAlias();
                if (alias != null && !alias.isEmpty()) {
                    buffer.addSql(" AS %s", alias);
                }
                buffer.addSql(" ON ");
                BuildResult br = this.buildFilter(joiner.on);
                if (br == null) {
                    throw new JsdException("JOIN 语句后面必须有 ON 语句");
                }
                buffer.addSql(br.getSql());
                buffer.addArg(br.getArgs());
            }
        }

        // WHERE
        if (info.where != null) {
            BuildResult br = this.buildFilter(info.where);
            if (br != null) {
                buffer.addSql(" WHERE 1=1 ");
                buffer.addSql(br.getSql());
                buffer.addArg(br.getArgs());
            }
        }

        // GROUP BY
        if (CollUtil.isNotEmpty(info.groups)) {
            buffer.addSql(" GROUP BY ");
            for (int i = 0; i < info.groups.size(); i++) {
                Groupers.Grouper g = info.groups.get(i);
                if (i > 0) buffer.addSql(",");

                for (int j = 0; j < g.columns.length; j++) {
                    if (j > 0) buffer.addSql(",");
                    if (g.table != null) buffer.addSql("`%s`.", g.table.getPrefix());
                    buffer.addSql("`%s`", g.columns[j]);
                }
            }
            if (info.having != null) {
                BuildResult br = this.buildFilter(info.having);
                if (br != null) {
                    buffer.addSql(" HAVING ");
                    buffer.addSql(br.getSql());
                    buffer.addArg(br.getArgs());
                }
            }
        }

        // ORDER BY
        if (CollUtil.isNotEmpty(info.orders)) {
            buffer.addSql(" ORDER BY ");
            for (int i = 0; i < info.orders.size(); i++) {
                Sorters.Sorter order = info.orders.get(i);
                if (i > 0) {
                    buffer.addSql(",");
                }
                for (int j = 0; j < order.columns.length; j++) {
                    if (j > 0) {
                        buffer.addSql(",");
                    }
                    if (order.table != null) {
                        buffer.addSql("`%s`.", order.table.getPrefix());
                    }
                    buffer.addSql("`%s` %s", order.columns[j], order.type.value);
                }
            }
        }

        // LIMIT
        if (info.skip != 0 || info.take != 0) {
            buffer.addSql(" LIMIT %d,%d", info.skip, info.take);
        }

        if (info.lock == LockMode.SHARED) {
            buffer.addSql(" lock in share mode");
        } else if (info.lock == LockMode.EXCLUSIVE) {
            buffer.addSql(" for update");
        }

        return buffer.getResult();
    }

//    @Override
//    public String buildCall(CallContext.CallInfo info) {
//        StringBuilder sb = new StringBuilder("{call ");
//        sb.append(info.getSp());
//
//        CallArgs args = info.getParams();
//        if (args != null) {
//            sb.append('(');
//            int i = 0;
//            Iterator<Map.Entry<Integer, CallArgs.CallArg>> iter = args.iterator();
//            while (iter.hasNext()) {
//                Map.Entry<Integer, CallArgs.CallArg> entry = iter.next();
//                if (i > 0) {
//                    sb.append(',');
//                }
//                sb.append('?');
//            }
//            // todo:
//            sb.append(')');
//        }
//
//        sb.append("}");
//
//        return sb.toString();
//    }

    public BuildResult buildFilter0(Filter filter) {
        if (filter instanceof BasicFilter) {
            BasicFilter f = (BasicFilter) filter;
            if (!f.hasItems()) return null;

            BuildBuffer buffer = new BuildBuffer();
            List<BasicFilter.FilterItem> items = f.getItems();
            for (int i = 0; i < items.size(); i++) {
                BasicFilter.FilterItem filterItem = items.get(i);
                if (filterItem instanceof BasicFilter.SelectFilterItem) {
                    BasicFilter basicFilter = ((BasicFilter.SelectFilterItem) filterItem).getSelectFilter();
                    buffer.addSql(" OR ");
                    buffer.addSql(" ( ");
                    BuildResult buildResult = buildFilter0(basicFilter);
                    buffer.addSql(buildResult.getSql());
                    buffer.addArg(buildResult.getArgs());
                    buffer.addSql(" ) ");
                } else {
                    if (i > 0) {
                        buffer.addSql(" AND ");
                    }
                }
                this.buildFilterItem(buffer, items.get(i));
            }
            return buffer.getResult();
        }
        return null;
    }

    @Override
    public BuildResult buildFilter(Filter filter) {
        if (filter instanceof BasicFilter) {
            BasicFilter f = (BasicFilter) filter;
            if (!f.hasItems()) return null;

            BuildBuffer buffer = new BuildBuffer();
            List<BasicFilter.FilterItem> items = f.getItems();
            for (int i = 0; i < items.size(); i++) {
                BasicFilter.FilterItem filterItem = items.get(i);
                if (filterItem instanceof BasicFilter.SelectFilterItem) {
                    BasicFilter basicFilter = ((BasicFilter.SelectFilterItem) filterItem).getSelectFilter();
                    buffer.addSql(" OR ");
                    buffer.addSql(" ( ");
                    BuildResult buildResult = buildFilter0(basicFilter);
                    buffer.addSql(buildResult.getSql());
                    buffer.addArg(buildResult.getArgs());
                    buffer.addSql(" ) ");
                } else {
                    FilterType type = null;
                    if (filterItem instanceof BasicFilter.OneColumnFilterItem) {
                        type = ((BasicFilter.OneColumnFilterItem) filterItem).getType();
                    } else if (filterItem instanceof BasicFilter.TwoColumnFilterItem) {
                        type = ((BasicFilter.TwoColumnFilterItem) filterItem).getType();
                    }
                    if (type != null && type.equals(OR)) {
                        buffer.addSql(" OR ");
                    } else {
                        buffer.addSql(" AND ");
                    }
                }

                this.buildFilterItem(buffer, items.get(i));
            }
            return buffer.getResult();
        } else if (filter instanceof Filter.AndFilter) {
            Filter.AndFilter f = (Filter.AndFilter) filter;
            return combineFilter(f.getLeft(), f.getRight(), "AND");
        } else if (filter instanceof Filter.OrFilter) {
            Filter.OrFilter f = (Filter.OrFilter) filter;
            return combineFilter(f.getLeft(), f.getRight(), "OR");
        } else if (filter instanceof Filter.NotFilter) {
            BuildResult r = this.buildFilter(((Filter.NotFilter) filter).getInner());
            if (r != null) {
                return new BuildResult("NOT(" + r.getSql() + ")", r.getArgs());
            }
        } else {
            throw new JsdException("unknown filter type: " + filter.getClass().getName());
        }
        return null;
    }

    private BuildResult combineFilter(Filter left, Filter right, String joiner) {
        BuildResult r1 = this.buildFilter(left);
        BuildResult r2 = this.buildFilter(right);
        if (r1 == null) {
            if (r2 == null) return null;
            else return r2;
        } else {
            if (r2 == null) return r1;
            else {
                String sql = String.format("(%s) %s (%s)", r1.getSql(), joiner, r2.getSql());
                List<Object> args;
                if (r1.getArgs() == null) {
                    args = r2.getArgs();
                } else {
                    args = r1.getArgs();
                    if (r2.getArgs() != null) {
                        args.addAll(r2.getArgs());
                    }
                }
                return new BuildResult(sql, args);
            }
        }
    }

    protected void buildFilterItem(BuildBuffer buffer, BasicFilter.FilterItem item) {
        switch (item.getItemType()) {
            case ONE_COLUMN:
                this.buildOneColumnFilterItem(buffer, (BasicFilter.OneColumnFilterItem) item);
                break;
            case TWO_COLUMN:
                this.buildTwoColumnFilterItem(buffer, (BasicFilter.TwoColumnFilterItem) item);
                break;
            case EXPR:
                this.buildExprFilterItem(buffer, (BasicFilter.ExprFilterItem) item);
                break;
            case SQL:
                this.buildSqlFilterItem(buffer, (BasicFilter.SqlFilterItem) item);
                break;
//            case SELECTFILTER:
//                this.buildOneColumnFilterItem(buffer, (BasicFilter.SelectFilterItem) item);
//                break;
        }
    }

    protected void buildOneColumnFilterItem(BuildBuffer buffer, BasicFilter.OneColumnFilterItem item) {
        Table t = item.getTable();
        if (t != null) buffer.addSql("`%s`.", t.getPrefix());
        FilterType type = item.getType();
        switch (type) {
            case NE:
                if (item.getValue() == null) buffer.addSql("`%s` IS NOT NULL", item.getColumn());
                else {
                    buffer.addSql("`%s`<>?", item.getColumn());
                    buffer.addArg(item.getValue());
                }
                break;
            case GT:
                buffer.addSql("`%s`>?", item.getColumn());
                buffer.addArg(item.getValue());
                break;
            case GTE:
                buffer.addSql("`%s`>=?", item.getColumn());
                buffer.addArg(item.getValue());
                break;
            case LT:
                buffer.addSql("`%s`<?", item.getColumn());
                buffer.addArg(item.getValue());
                break;
            case LTE:
                buffer.addSql("`%s`<=?", item.getColumn());
                buffer.addArg(item.getValue());
                break;
            case LK:
                buffer.addSql("`%s` LIKE CONCAT('%%', ?, '%%')", item.getColumn());
                buffer.addArg(item.getValue());
                break;
            case NOTLK:
                buffer.addSql("`%s` NOT LIKE CONCAT('%%', ?, '%%')", item.getColumn());
                buffer.addArg(item.getValue());
                break;
            case LKLEFT:
                buffer.addSql("`%s`  LIKE  CONCAT('%%', ?)", item.getColumn());
                buffer.addArg(item.getValue());
                break;
            case LKRIGHT:
                buffer.addSql("`%s`  LIKE  CONCAT( ?, '%%')", item.getColumn());
                buffer.addArg(item.getValue());
                break;
            case BETWEEN:
                buffer.addSql("`%s` BETWEEN ? AND  ?", item.getColumn());
                buffer.addArg(item.getValue(), item.getValue2());
                break;
            case NOTBETWEEN:
                buffer.addSql("`%s` NOT BETWEEN ? AND  ?", item.getColumn());
                buffer.addArg(item.getValue(), item.getValue2());
                break;
            case OR:
                buffer.addSql("`%s` = ? ", item.getColumn());
                buffer.addArg(item.getValue());
                break;
            case IN:
            case NIN:
                Class<?> clazz = item.getValue().getClass();
                if (clazz.isArray()) {
                    Class<?> elemType = clazz.getComponentType();
                    StringBuilder sb = new StringBuilder();
//                    if (elemType == String.class) {
                    for (int i = 0; i < Array.getLength(item.getValue()); i++) {
                        if (i > 0) {
                            sb.append(",");
                        }
                        sb.append("'");
                        sb.append(Array.get(item.getValue(), i));
                        sb.append("'");
                    }
                    // }
//                    else {
//                        for (int i = 0; i < Array.getLength(item.getValue()); i++) {
//                            if (i > 0) {
//                                sb.append(",");
//                            }
//                            sb.append(Array.get(item.getValue(), i));
//                        }
//                    }
                    buffer.addSql("`%s` %s(%s)", item.getColumn(), item.getType().value, sb.toString());
                } else {
                    buffer.addSql("`%s` %s(%s)", item.getColumn(), item.getType().value, item.getValue());
                }
                break;
            default:
                if (item.getValue() == null) buffer.addSql("`%s` IS NULL", item.getColumn());
                else {
                    buffer.addSql("`%s`=?", item.getColumn());
                    buffer.addArg(item.getValue());
                    if (type.value.equals(BETWEEN)) {
                        buffer.addArg(item.getValue(), item.getValue2());
                    }
                }
                break;
        }
    }

    protected void buildTwoColumnFilterItem(BuildBuffer buffer, BasicFilter.TwoColumnFilterItem item) {
        switch (item.getType()) {
            case EQ:
                buffer.addSql("`%s`.`%s`=`%s`.`%s`", item.getTable1().getPrefix(), item.getColumn1(), item.getTable2().getPrefix(), item.getColumn2());
                break;
            case NE:
                buffer.addSql("`%s`.`%s`<>`%s`.`%s`", item.getTable1().getPrefix(), item.getColumn1(), item.getTable2().getPrefix(), item.getColumn2());
                break;
            case LT:
                buffer.addSql("`%s`.`%s`<`%s`.`%s`", item.getTable1().getPrefix(), item.getColumn1(), item.getTable2().getPrefix(), item.getColumn2());
                break;
            case GT:
                buffer.addSql("`%s`.`%s`>`%s`.`%s`", item.getTable1().getPrefix(), item.getColumn1(), item.getTable2().getPrefix(), item.getColumn2());
                break;
            case LTE:
                buffer.addSql("`%s`.`%s`<=`%s`.`%s`", item.getTable1().getPrefix(), item.getColumn1(), item.getTable2().getPrefix(), item.getColumn2());
                break;
            case GTE:
                buffer.addSql("`%s`.`%s`>=`%s`.`%s`", item.getTable1().getPrefix(), item.getColumn1(), item.getTable2().getPrefix(), item.getColumn2());
                break;
            default:
                throw new JsdException("invalid filter type: " + item.getType().value);
        }
    }

    protected void buildExprFilterItem(BuildBuffer buffer, BasicFilter.ExprFilterItem item) {
        buffer.addSql(item.getExpr());
    }

    protected void buildSqlFilterItem(BuildBuffer buffer, BasicFilter.SqlFilterItem item) {
        buffer.addSql(item.getSql());
    }


}
