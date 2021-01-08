package lark.util.index.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;

/**
 * @author andy
 */
public class ElasticsearchConfig {

    private final String host;

    private final int port;

    private final int timeout;

    public ElasticsearchConfig( String host, int port, int timeout ) {
        this.host = host;
        this.port = port;
        this.timeout = timeout;
    }

    @Bean
    public RestHighLevelClient elasticsearchClient() {
        HttpHost host = new HttpHost( this.host, this.port );
        return new RestHighLevelClient( RestClient.builder( host ) );
    }
}