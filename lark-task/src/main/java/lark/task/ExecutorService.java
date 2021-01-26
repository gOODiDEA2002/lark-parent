package lark.task;

import lark.task.data.*;

/**
 * @author cuigh
 */
public interface ExecutorService {

    Result registry();

    Result deregistry();
    /**
     * 执行任务
     * @param param
     * @return
     */
    Result execute(ExecuteParam param );

    /**
     * 获取日志请求
     * @param request
     * @return
     */
    LogResult log(LogRequest request );
}
