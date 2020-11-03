package lark.api.boot;

import org.springframework.core.io.ResourceLoader;
import lark.core.boot.Application;

/**
 *
 * @author Andy Yuan
 * @date 2020/7/13
 */
public class ApiApplication extends Application {
    public ApiApplication(Class<?>... primarySources) {
        this(null, primarySources);
    }

    public ApiApplication(ResourceLoader resourceLoader, Class<?>... primarySources) {
        super(resourceLoader, primarySources);
    }

    @Override
    protected void load() {
        super.load();
    }

    @Override
    protected void start() {
    }
}
