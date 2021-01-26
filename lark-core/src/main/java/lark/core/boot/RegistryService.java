package lark.core.boot;

public interface RegistryService {

    void registerService();

    void deregisterService();

    String getConfig( String id, String groupName );

    String getService( String serviceName, String groupName );
}
