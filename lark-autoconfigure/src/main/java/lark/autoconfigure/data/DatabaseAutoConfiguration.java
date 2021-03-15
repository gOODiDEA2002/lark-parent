package lark.autoconfigure.data;

import lark.core.lang.ServiceException;
import lark.core.util.Strings;
import lark.db.DatabaseConfig;
import lark.db.DatabaseService;
import lark.db.TableShardingConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.sql.SQLException;
import java.util.List;

/**
 * @author andy
 */
@Configuration
@ConditionalOnClass({DatabaseService.class})
@EnableConfigurationProperties(DatabaseServiceProperties.class)
public class DatabaseAutoConfiguration {

    private static final int DEFAULT_POOL_MIN_SIZE = 1;
    private static final int DEFAULT_POOL_MAX_SIZE = 2;

    @Autowired
    Environment environment;

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix="lark.db",name = "source", matchIfMissing = true)
    public DatabaseService databaseService(DatabaseServiceProperties props) {
        DatabaseService databaseService = new DatabaseService();
        List<String> sources = props.getSource();
        if ( sources != null ) {
            for( String source : sources ) {
                String name = environment.getProperty( String.format( "lark.db.%s.name", source ) );
                String address = environment.getProperty( String.format( "lark.db.%s.address", source ) );
                String username = environment.getProperty( String.format( "lark.db.%s.username", source ) );
                String password = environment.getProperty( String.format( "lark.db.%s.password", source ) );
                //
                String minPoolProperty = environment.getProperty( String.format( "lark.db.%s.min-pool", source ) );
                int minPoolSize = DEFAULT_POOL_MIN_SIZE;
                if (!Strings.isEmpty( minPoolProperty ) ) {
                    minPoolSize = Integer.getInteger( minPoolProperty );
                }
                String maxPoolProperty = environment.getProperty( String.format( "lark.db.%s.max-pool", source ) );
                int maxPoolSize = DEFAULT_POOL_MAX_SIZE;
                if (!Strings.isEmpty( maxPoolProperty ) ) {
                    maxPoolSize = Integer.getInteger( maxPoolProperty );
                }
                DatabaseConfig config = new DatabaseConfig( name, address, username, password, minPoolSize, maxPoolSize );
                databaseService.setConfig( source, config );
            }
        }
        //
        List<String> shards = props.getShard();
        if ( shards != null ) {
            for( String shard : shards ) {
                String databases = environment.getProperty( String.format( "lark.db.%s.database", shard ) );
                String route = environment.getProperty( String.format( "lark.db.%s.route", shard ) );
                String databaseShardingColumn = environment.getProperty( String.format( "lark.db.%s.database-sharding.column", shard ) );
                String databaseShardingAlgorithm = environment.getProperty( String.format( "lark.db.%s.database-sharding.algorithm", shard ) );
                String tableShardingColumn = environment.getProperty( String.format( "lark.db.%s.table-sharding.column", shard ) );
                String tableShardingAlgorithm = environment.getProperty( String.format( "lark.db.%s.table-sharding.algorithm", shard ) );
                TableShardingConfig config = new TableShardingConfig( shard, route, tableShardingColumn, databaseShardingColumn, databaseShardingAlgorithm, tableShardingColumn, tableShardingAlgorithm, databases );
                try {
                    databaseService.setShardConfig( config );
                } catch (SQLException sqlException) {
                    throw new ServiceException( sqlException.getMessage() );
                }
            }
        }
        return databaseService;
    }
}
