package lark.core.boot;

/**
 * @author andy
 */
public interface RegistryService {

    /**
     * 注册
     */
    void registerService();


    /**
     * 取消注册
     */
    void deregisterService();

    /**
     * 获取服务的访问地址
     * @param serviceName 服务名
     * @param groupName 服务组名
     * @return 服务节点的访问地址
     */
    String getServiceUrl( String serviceName, String groupName );
}
