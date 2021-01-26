package lark.db;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 *    shard:
 *       table: t_order
 *         route: ds$->{0..1}.t_order_$->{0..1}
 *         database:
 *           column: user_id
 *           algorithm: ds$->{user_id % 2}
 *         table:
 *           column: order_id
 *           algorithm: t_order_$->{order_id % 2}
 *
 *                     actual-data-nodes: ds$->{0..1}.t_order_$->{0..1}
 *           database-strategy:
 *             inline:
 *               sharding-column: user_id
 *               algorithm-expression: ds$->{user_id % 2}
 *           table-strategy:
 *             inline:
 *               sharding-column: order_id
 *               algorithm-expression: t_order_$->{order_id % 2}
 *       binding-tables: t_order
 */
@Data
@AllArgsConstructor
public class TableShardingConfig {
    private String logicTableName;
    private String actualDataNodes;
    private String keyColumnName;
    private String databaseShardingColumnName;
    private String databaseShardingAlgorithmExpression;
    private String tableShardingColumnName;
    private String tableShardingAlgorithmExpression;
    private String databaseNames;

    public String[] getDatabaseNames() {
        String[] datanbaseNameList = databaseNames.split( "," );
        return datanbaseNameList;
    }
}
