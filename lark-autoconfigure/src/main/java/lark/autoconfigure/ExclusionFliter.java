package lark.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfigurationImportFilter;
import org.springframework.boot.autoconfigure.AutoConfigurationMetadata;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author andy
 */
public class ExclusionFliter implements AutoConfigurationImportFilter {
    private static final Set<String> SHOULD_SKIP = new HashSet<>(
            Arrays.asList(
                    "org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration",
                    "org.apache.shardingsphere.shardingjdbc.spring.boot.SpringBootConfiguration",
                    "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration",
                    "org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration",
                    "org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration"
            ));

    @Override
    public boolean[] match(String[] autoConfigurationClasses, AutoConfigurationMetadata autoConfigurationMetadata) {
        boolean[] matches = new boolean[autoConfigurationClasses.length];
        for(int i = 0; i< autoConfigurationClasses.length; i++) {
            matches[i] = !SHOULD_SKIP.contains(autoConfigurationClasses[i]);
        }
        return matches;
    }
}
