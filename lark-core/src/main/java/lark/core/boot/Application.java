package lark.core.boot;

import lark.core.util.Networks;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import lark.core.data.Guid;
import lark.core.lang.FatalException;
import org.springframework.util.StringUtils;

import javax.annotation.PreDestroy;
import java.util.*;

/**
 * @author cuigh
 */

public class Application extends SpringApplication {
    protected ConfigurableApplicationContext ctx;
    protected Environment env;
    private String id = new Guid().toString();

    public Application(Class<?>... primarySources) {
        this(null, primarySources);
    }

    public Application(ResourceLoader resourceLoader, Class<?>... primarySources) {
        super(resourceLoader, primarySources);
        this.init();
//        super.addListeners(this::handleEvent);
    }

    /**
     * 获取程序启动时动态生成的唯一 ID
     *
     * @return 程序 ID
     */
    public String getId() {
        return id;
    }

    /**
     * 获取程序名称
     *
     * @return 程序名称
     */
    public String getName() {
        return env.getProperty( "spring.application.name");
    }

    public String getAddress() {
        return Networks.getLocalIP4();
    }

    public String getPort() {
        return env.getProperty( "server.port");
    }
    /**
     * 获取程序版本
     *
     * @return 程序版本
     */
    public String getVersion() {
        return env.getProperty("lark.application.version");
    }

    protected void init() {
        System.setProperty("java.net.preferIPv4Stack", "true");
        setBannerMode(Banner.Mode.OFF);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> stop()));
    }

    protected void load() {
        // 留给子类扩展
    }

    protected void start() {
        // 留给子类扩展
    }

    protected void stop() {
        // 留给子类扩展
    }

    @Override
    protected void afterRefresh(ConfigurableApplicationContext ctx, ApplicationArguments args) {
        this.ctx = ctx;
        this.env = ctx.getEnvironment();
        //
        try {
            load();
            start();
            register();
        } catch (Exception e) {
            throw new FatalException(e);
        }
    }

    private void register() {
        RegistryService registry = findBean(RegistryService.class);
        if (registry == null) {
            return;
        }
        //
        registry.registerService();
        //
        ctx.addApplicationListener((ContextClosedEvent e) -> registry.deregisterService());
    }

    /**
     * Find bean by type, return null if no bean was found
     *
     * @param type bean class
     * @param <T>  bean type
     * @return bean instance
     */
    public <T> T findBean(Class<T> type) {
        Map<String, T> beans = ctx.getBeansOfType(type);
        if (beans.isEmpty()) {
            return null;
        }

        Iterator<Map.Entry<String, T>> iterator = beans.entrySet().iterator();
        return iterator.next().getValue();
    }

    /**
     * Find beans by type
     *
     * @param type bean class
     * @param <T>  bean type
     * @return bean list
     */
    public <T> List<T> findBeans(Class<T> type) {
        Map<String, T> beans = ctx.getBeansOfType(type);
        List<T> list = new ArrayList<>(beans.size());
        beans.forEach((k, v) -> list.add(v));
        return list;
    }
}
