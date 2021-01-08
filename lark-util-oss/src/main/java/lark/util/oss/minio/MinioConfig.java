package lark.util.oss.minio;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.context.annotation.Bean;

/**
 * @author andy
 */
@Data
public class MinioConfig {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;

    public MinioConfig( String endpoint, String accessKeyId, String accessKeySecret ) {
        this.endpoint = endpoint;
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
    }

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKeyId, accessKeySecret)
                .build();
    }
}
