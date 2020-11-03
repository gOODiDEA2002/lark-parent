package lark.task.boot;

import lark.core.boot.Application;
import org.apache.commons.logging.Log;
import org.springframework.core.io.ResourceLoader;

import java.util.ArrayList;


/**
 * @author cuigh
 */
public class TaskApplication extends Application {

    public TaskApplication(Class<?>... primarySources) {
        this(null, primarySources);
    }

    public TaskApplication(ResourceLoader resourceLoader, Class<?>... primarySources) {
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
