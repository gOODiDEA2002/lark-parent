package lark.api.web;

/**
 * Created by Andy Yuan on 2020/8/18.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @desc: 创建一个拦截器
 * @author: toby
 */
@Component
public class ApiInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(ApiInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("ApiInterceptor->preHandle: {}", request.getRequestURI());
        Long beginTime = System.currentTimeMillis();
        request.setAttribute("beginTime", beginTime );
        // request
        String postData;
        HttpServletRequest requestCacheWrapperObject = null;
        try {
            // To overcome request stream closed issue
            requestCacheWrapperObject = new ContentCachingRequestWrapper(request);
            requestCacheWrapperObject.getParameterMap();
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            postData = readPayload(requestCacheWrapperObject);
            logger.info("REQUEST DATA: {}", postData);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        logger.info("ApiInterceptor->postHandle: {}", request.getRequestURI());
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        logger.info("ApiInterceptor->afterCompletion: {}", request.getRequestURI());
    }

    private static String readPayload(final HttpServletRequest request) throws IOException {
        String payloadData = null;
        ContentCachingRequestWrapper contentCachingRequestWrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (null != contentCachingRequestWrapper) {
            byte[] buf = contentCachingRequestWrapper.getContentAsByteArray();
            if (buf.length > 0) {
                payloadData = new String(buf, 0, buf.length, contentCachingRequestWrapper.getCharacterEncoding());
            }
        }
        return payloadData;
    }

}