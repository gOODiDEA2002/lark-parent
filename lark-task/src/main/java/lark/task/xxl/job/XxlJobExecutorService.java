package lark.task.xxl.job;

import com.xxl.job.core.util.XxlJobRemotingUtil;
import lark.core.codec.JsonCodec;
import lark.core.util.Strings;
import lark.net.util.Ips;
import lark.task.ExecutorService;
import lark.task.data.ExecuteParam;
import lark.task.data.LogRequest;
import lark.task.data.LogResult;
import lark.task.data.Result;
import lark.task.xxl.job.dto.XxlJobExecuteRequest;
import lark.task.xxl.job.dto.XxlJobLogRequest;
import lark.task.xxl.job.dto.XxlJobLogResult;
import lark.task.xxl.job.dto.XxlJobResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class XxlJobExecutorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(XxlJobExecutorService.class);
    ExecutorService executorService;

    public XxlJobExecutorService( ExecutorService executorService ) {
        this.executorService = executorService;
    }

    @RequestMapping( "/beat")
    public XxlJobResult heartbeat( HttpServletRequest request,
                                   @RequestBody(required = false) String data ) {
        String accessToken = request.getHeader(XxlJobRemotingUtil.XXL_JOB_ACCESS_TOKEN);
        LOGGER.info( "===>>> beat: {}, accessToken:{}, data:{}", Ips.getClientIpAddress( request ), accessToken, data );
        //
        XxlJobResult result = new XxlJobResult();
        result.setCode( 200 );
        result.setMsg(Strings.EMPTY);
        return result;
    }

    @RequestMapping( "/idleBeat")
    public XxlJobResult idlebeat( HttpServletRequest request,
                                  @RequestBody(required = false) String data ) {
        String accessToken = request.getHeader(XxlJobRemotingUtil.XXL_JOB_ACCESS_TOKEN);
        LOGGER.info( "===>>> idleBeat: {}, accessToken:{}, data:{}", Ips.getClientIpAddress( request ), accessToken, data );
        //
        XxlJobResult result = new XxlJobResult();
        result.setCode( 200 );
        result.setMsg(Strings.EMPTY);
        return result;
    }

    @RequestMapping( "/run")
    public XxlJobResult execute(HttpServletRequest request,
                                @RequestBody(required = false) String data ) {
        String accessToken = request.getHeader(XxlJobRemotingUtil.XXL_JOB_ACCESS_TOKEN);
        LOGGER.info( "===>>> run: {}, accessToken:{}, data:{}", Ips.getClientIpAddress( request ), accessToken, data );
        XxlJobExecuteRequest xxlJobExecuteRequest = JsonCodec.decode( data, XxlJobExecuteRequest.class );
        //
        ExecuteParam executeParam = xxlJobExecuteRequest.toExecuteParam();
        Result result = executorService.execute( executeParam );
        return new XxlJobResult( result );
    }

    @RequestMapping( "/log")
    public XxlJobLogResult log(HttpServletRequest request,
                               @RequestBody(required = false) String data ) {
        String accessToken = request.getHeader(XxlJobRemotingUtil.XXL_JOB_ACCESS_TOKEN);
        LOGGER.info( "===>>> log: {}, accessToken:{}, data:{}", Ips.getClientIpAddress( request ), accessToken, data );
        XxlJobLogRequest xxlJobLogRequest = JsonCodec.decode( data, XxlJobLogRequest.class );
        //
        LogRequest logRequest = xxlJobLogRequest.toLogRequest();
        LogResult result = executorService.log( logRequest );
        return new XxlJobLogResult( result );
    }
}
