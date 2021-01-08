package lark.autoconfigure.util.index;

import lark.util.index.IndexService;
import lark.util.index.config.ElasticsearchConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author andy
 */
@Configuration
@ConditionalOnClass({IndexService.class})
@EnableConfigurationProperties(IndexServiceProperties.class)
public class IndexAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public IndexService indexService(IndexServiceProperties props) {
        ElasticsearchConfig config = new ElasticsearchConfig( props.getHost(), props.getPort(), props.getTimeout() );
        return new IndexService( config.elasticsearchClient() );
    }
}
