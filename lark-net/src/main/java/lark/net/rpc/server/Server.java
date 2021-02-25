package lark.net.rpc.server;

import io.micrometer.core.instrument.MeterRegistry;
import lark.net.Endpoint;
import lark.net.rpc.client.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;

/**
 * @author cuigh
 */
public class Server {
    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);
    private final ServerOptions options;
    private ServiceContainer container;
    private MeterRegistry meterRegistry;

    public Server(ServerOptions options) {
        this.options = options;
        this.container = new ServiceContainer( options.requestMappingHandlerMapping );
    }

    public void setMeterRegistry(MeterRegistry meterRegistry) {
        if ( meterRegistry != null ) {
            this.meterRegistry = meterRegistry;
        }
    }

    public void registerService(Object instance) {
        if (instance instanceof BaseService) {
            throw new IllegalArgumentException("can't register a proxy object as service, this will cause dead circulation");
        }
        this.container.registerService(instance);
    }

    public void registerService(Class<?> clazz, Object instance) {
        if (instance instanceof BaseService) {
            throw new IllegalArgumentException(String.format("can't register a proxy object as service [%s], this will cause dead circulation", clazz.getName()));
        }
        this.container.registerService(clazz, instance);
    }

    public ServiceContainer getContainer() {
        return container;
    }

    public ServerOptions getOptions() {
        return options;
    }

    private SocketAddress createAddress(String address) {
        return Endpoint.parse(address).toSocketAddress();
    }
}


