package lark.service.boot.web;

import lark.core.codec.JsonCodec;
import lark.net.rpc.protocol.ResponseMessage;
import lark.net.rpc.server.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Andy Yuan on 2020/8/18.
 */
@Component
public class ServiceServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(ServiceServlet.class);
    Server server;

    public ServiceServlet( Server server ) {
        this.server = server;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("ServiceServlet->doGet");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestData = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        //
        ResponseMessage responseMessage = ServerExecuter.execute( server, requestData );
        //
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        out.print(JsonCodec.encode( responseMessage ));
        out.flush();
    }
}
