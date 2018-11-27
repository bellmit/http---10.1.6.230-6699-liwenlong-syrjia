package cn.syrjia.interceptor;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StopWatchHandlerInterceptor extends HandlerInterceptorAdapter {

    private Logger logger = LogManager.getLogger(StopWatchHandlerInterceptor.class);

    @SuppressWarnings({"unchecked", "rawtypes"})
    private NamedThreadLocal<Long> startTimeThreadLocal = new NamedThreadLocal("StopWatch-StartTime");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        this.startTimeThreadLocal.set(System.currentTimeMillis());

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        long endTime = System.currentTimeMillis();
        long beginTime = this.startTimeThreadLocal.get();
        long consumeTime = endTime - beginTime;
        if (consumeTime > 500L) {
            this.logger.info("请求耗费时间太长了：" +
                    String.format("%s consume %d millis", new Object[]{request.getRequestURI(), Long.valueOf(consumeTime)}));
        }
    }

}
