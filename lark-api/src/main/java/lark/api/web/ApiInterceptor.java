package lark.api.web;

/**
 * Created by Andy Yuan on 2020/8/18.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @desc: 创建一个拦截器
 * @author: toby
 */
@Component
public class ApiInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(ApiInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Long beginTime = System.currentTimeMillis();
        request.setAttribute("beginTime", beginTime );
        logger.info("ApiInterceptor->preHandle: {}", request.getRequestURI());
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
}