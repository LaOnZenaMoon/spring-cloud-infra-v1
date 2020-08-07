package lozm.gateway.filters;

import lozm.gateway.global.config.ArrowApiKeys;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import lozm.gateway.global.util.GatewayUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

import static org.apache.http.HttpStatus.*;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

@Slf4j
public class PreFilter extends ZuulFilter {

    @Autowired
    private ArrowApiKeys arrowApiKeys;

    private static final String unAuthorizedMessage = "{\"success\":false,\"code\":\"UNAUTHORIZED\",\"message\":\"유효하지 않은 인증키 입니다.\",\"data\":null}";
    private static final String forbiddenMessage = "{\"success\":false,\"code\":\"FORBIDDEN\",\"message\":\"허용되지 않은 IP 에서의 요청 입니다.\",\"data\":null}";
    private static final String badRequestMessage = "{\"success\":false,\"code\":\"BAD_REQUEST\",\"message\":\"잘못된 요청 입니다.\",\"data\":null}";

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
        String authorization = request.getHeader("Authorization");
        String remoteIp = GatewayUtils.getRemoteIp(request);

        String timestamp = String.valueOf(System.currentTimeMillis());
        context.put("timestamp", timestamp);
        requestLog(request, authorization, remoteIp, timestamp);

        int resCode = arrowApiKeys.arrowResCode(authorization, remoteIp);
        if(resCode != SC_OK ) {
            setErrorResponse(context, resCode);
            requestErrorLog(context, authorization, remoteIp, resCode);
        }

        return null;
    }

    private void requestLog(HttpServletRequest request, String authorization, String remoteIp, String timestamp) {
        log.info("Request  [{} {}, client={}, authorization={}, timestamp={}]", request.getMethod(), request.getRequestURL().toString(), remoteIp, authorization, timestamp);
    }

    private void requestErrorLog(RequestContext context, String authorization, String remoteIp, int resCode) {
        HttpServletRequest request = context.getRequest();
        log.warn("*Authorization fails*\nRequest[{} {}]\nAuthorization[{}]\nIP[{}]\nResponse[{} {}]", request.getMethod(), request.getRequestURI(), authorization, remoteIp, resCode, context.getResponseBody());
    }

    private void setErrorResponse(RequestContext context, int resCode) {
        context.setSendZuulResponse(false);

        context.getResponse().addHeader("Content-Type", "application/json;charset=utf-8");
        context.setResponseStatusCode(resCode);
        if(resCode == SC_FORBIDDEN)
            context.setResponseBody(forbiddenMessage);
        else if(resCode == SC_UNAUTHORIZED)
            context.setResponseBody(unAuthorizedMessage);
        else
            context.setResponseBody(badRequestMessage);
    }

}
