package lark.task;

import lark.task.data.*;

/**
 * @author cuigh
 */
public interface ScheduleService {

    /**
     * 注册Task
     * @param param
     * @return
     */
    Result registryTask(RegistryTaskRequest param);

    /**
     * 移除
     * @param param
     * @return
     */
    Result removeTask(RemoveTaskRequest param);

    /**
     * 注册Handler
     * @param param
     * @return
     */
    Result registryHandler(RegistryHandlerRequest param);

    /**
     * 通知执行结果
     * @param param
     * @return
     */
    Result notify(NotifyRequest param);
}