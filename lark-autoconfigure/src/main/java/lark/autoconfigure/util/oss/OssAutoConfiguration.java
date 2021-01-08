package lark.autoconfigure.util.oss;

import lark.autoconfigure.util.index.IndexServiceProperties;
import lark.util.oss.OssService;
import lark.util.oss.minio.MinioConfig;
import lark.util.oss.minio.MinioOssService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({OssService.class})
@EnableConfigurationProperties(OssServiceProperties.class)
public class OssAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public OssService ossService(OssServiceProperties props) {
        MinioConfig config = new MinioConfig( props.getEndpoint(), props.getUsername(), props.getPassword() );
        MinioOssService minioOssService = new MinioOssService( config.minioClient() );
        return new OssService( minioOssService );
    }
}
