package lozm.gateway.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

import static lozm.gateway.global.util.GatewayUtils.getRemoteIp;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

@Slf4j
public class PreFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        String timestamp = String.valueOf(System.currentTimeMillis());
        context.put("timestamp", timestamp);
        requestLog(request, timestamp);
        return null;
    }

    private void requestLog(HttpServletRequest request, String timestamp) {
        log.info("Request  [{} {}, client={}, timestamp={}]", request.getMethod(), request.getRequestURL().toString(), getRemoteIp(request), timestamp);
    }

}
