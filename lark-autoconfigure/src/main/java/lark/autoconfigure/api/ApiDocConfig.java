package lark.autoconfigure.api;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;


/**
 * @author Andy Yuan
 * @date 2020/11/1
 */
@Configuration
public class ApiDocConfig {
    @Autowired
    Environment environment;

    @Bean
    public GroupedOpenApi api() {
        String title = String.format( "%s API", environment.getProperty( "spring.application.name"));
        return GroupedOpenApi.builder()
                .group(title)
                .pathsToMatch("/v*/**")
                .build();
    }
}