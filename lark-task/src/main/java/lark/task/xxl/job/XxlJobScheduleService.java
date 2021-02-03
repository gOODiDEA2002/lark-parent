package lark.task.xxl.job;

import com.xxl.job.core.util.XxlJobRemotingUtil;
import lark.core.codec.JsonCodec;
import lark.task.ScheduleService;
import lark.task.data.*;
import lark.task.xxl.job.config.XxlJobScheduleConfig;
import lark.task.xxl.job.dto.*;
import lark.task.xxl.job.util.XxlJobHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class XxlJobScheduleService implements ScheduleService {
    private static final Logger LOGGER = LoggerFactory.getLogger(XxlJobScheduleService.class);
    private XxlJobHttpClient client;

    public XxlJobScheduleService( XxlJobScheduleConfig config ) {
        client = new XxlJobHttpClient( config.getAddress(), config.getAccessToken() );
    }

    @Override
    public Result registryTask(RegistryTaskRequest param) {
        XxlJobRegistryTaskRequest request =  new XxlJobRegistryTaskRequest( param );
        XxlJobResult xxlJobResult = client.request( "/api/registry", JsonCodec.encode( request ), XxlJobResult.class );
        if ( xxlJobResult == null ) {
            LOGGER.info( "===>>> Task registry fail!" );
            return new Result();
        }
        Result result = xxlJobResult.toResult();
        LOGGER.info( "===>>> Task registry: name:{}, address:{}, result:{}", param.getName(), param.getAddress(), result.isSuccess() );
        return result;
    }

    @Override
    public Result removeTask(RemoveTaskRequest param) {
        XxlJobRemoveTaskRequest request =  new XxlJobRemoveTaskRequest( param );
        XxlJobResult xxlJobResult = client.request( "/api/registryRemove", JsonCodec.encode( request ), XxlJobResult.class );
        if ( xxlJobResult == null ) {
            LOGGER.info( "===>>> Task remove fail!" );
            return new Result();
        }
        Result result = xxlJobResult.toResult();
        LOGGER.info( "===>>> Task remove: name:{}, address:{}, result:{}", param.getName(), param.getAddress(), result.isSuccess() );
        return result;
    }

    @Override
    public Result registryHandler(RegistryHandlerRequest param) {
        return null;
    }

    @Override
    public Result notify(NotifyRequest param) {
        List<XxlJobCallbackRequest> request = new ArrayList<>();
        request.add( new XxlJobCallbackRequest( param ));
        XxlJobResult xxlJobResult = client.request( "/api/callback", JsonCodec.encode( request ), XxlJobResult.class );
        Result result = xxlJobResult.toResult();
        LOGGER.info( "===>>> Task callback: name:{}, logId:{}, result:{}", param.getName(), param.getLogId(), result.isSuccess() );
        return result;
    }
}
