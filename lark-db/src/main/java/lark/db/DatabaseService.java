package lark.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lark.core.lang.ServiceException;
import lark.db.sql.SqlQuery;
import org.apache.shardingsphere.api.config.sharding.KeyGeneratorConfiguration;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.InlineShardingStrategyConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.ShardingStrategyConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author andy
 */
public class DatabaseService {
    private final Map<String, DataSource> datasources = new HashMap<>();
    private final Map<String, SqlQuery> configs = new HashMap<>();
    private final Map<String, SqlQuery> shardConfigs = new HashMap<>();

    public SqlQuery get( String dbName ) {
        if ( configs.containsKey( dbName ) ) {
            return configs.get( dbName );
        }
        throw new ServiceException( String.format( "%s not be config", dbName ) );
    }

    public void setConfig( String dbName, DatabaseConfig config ) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl( config.getUrl() );
        hikariConfig.setUsername( config.getUsername());
        hikariConfig.setPassword( config.getPassword() );
        //
        hikariConfig.setMinimumIdle( config.getMinPoolSize());
        hikariConfig.setMaximumPoolSize( config.getMaxPoolSize());
        hikariConfig.setPoolName( dbName );
        //
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        //
        HikariDataSource dataSource = new HikariDataSource(hikariConfig);
        /*
         * Alibaba Druid
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl( config.getUrl() );
        dataSource.setUsername( config.getUsername() );
        dataSource.setPassword( config.getPassword() );
         */
        //
        datasources.put( dbName, dataSource );
        configs.put( dbName, new SqlQuery( dataSource ) );
    }

    public SqlQuery getShard( String logicTableName ) {
        if ( shardConfigs.containsKey( logicTableName ) ) {
            return shardConfigs.get( logicTableName );
        }
        throw new ServiceException( String.format( "%s not be config", logicTableName ) );
    }

    public void setShardConfig(TableShardingConfig shardingConfig ) throws SQLException {
        Map<String, DataSource> dataSourceMap = new HashMap<>();
        for ( String databaseName : shardingConfig.getDatabaseNames() ) {
            databaseName = databaseName.trim();
            dataSourceMap.put( databaseName, datasources.get( databaseName ) );
        }
        String logicTableName = shardingConfig.getLogicTableName();
        DataSource shardingDatasource = createShardingDataSource( logicTableName, shardingConfig.getActualDataNodes(), shardingConfig.getKeyColumnName(), shardingConfig.getDatabaseShardingColumnName(), shardingConfig.getDatabaseShardingAlgorithmExpression(), shardingConfig.getTableShardingColumnName(), shardingConfig.getTableShardingAlgorithmExpression(), dataSourceMap );
        //
        shardConfigs.put( logicTableName, new SqlQuery( shardingDatasource ) );
    }

    private DataSource createShardingDataSource(String logicTableName, String actualDataNodes, String keyColumnName, String databaseShardingColumnName, String databaseShardingAlgorithmExpression, String tableShardingColumnName, String tableShardingAlgorithmExpression, Map<String, DataSource> dataSourceMap ) throws SQLException {
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        //
        TableRuleConfiguration tableRuleConfiguration = new TableRuleConfiguration(logicTableName, actualDataNodes );
        KeyGeneratorConfiguration keyGeneratorConfiguration = new KeyGeneratorConfiguration("SNOWFLAKE", keyColumnName );
        tableRuleConfiguration.setKeyGeneratorConfig( keyGeneratorConfiguration );
        shardingRuleConfig.getTableRuleConfigs().add( tableRuleConfiguration );
        //
        shardingRuleConfig.getBindingTableGroups().add( logicTableName );
        //
        ShardingStrategyConfiguration databaseShardingStrategy = new InlineShardingStrategyConfiguration( databaseShardingColumnName, databaseShardingAlgorithmExpression );
        shardingRuleConfig.setDefaultDatabaseShardingStrategyConfig( databaseShardingStrategy );
        //
        ShardingStrategyConfiguration tableShardingStrategy = new InlineShardingStrategyConfiguration( tableShardingColumnName, tableShardingAlgorithmExpression);
        shardingRuleConfig.setDefaultTableShardingStrategyConfig( tableShardingStrategy );
        //
        Properties props = new Properties();
        props.setProperty("sql.show", "true" );
        return ShardingDataSourceFactory.createDataSource( dataSourceMap, shardingRuleConfig, new Properties());
    }
}
