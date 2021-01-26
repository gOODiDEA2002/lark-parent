package lark.autoconfigure.data;

import lark.autoconfigure.msg.MsgServiceProperties;
import lark.core.lang.ServiceException;
import lark.db.DatabaseConfig;
import lark.db.DatabaseService;
import lark.db.TableShardingConfig;
import lark.msg.Publisher;
import lark.msg.rocketmq.RocketmqPublisher;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
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
                DatabaseConfig config = new DatabaseConfig( name, address, username, password );
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
