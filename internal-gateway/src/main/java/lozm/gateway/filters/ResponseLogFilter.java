package lozm.gateway.filters;

import com.google.common.io.CharStreams;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static lozm.gateway.global.util.GatewayUtils.getRemoteIp;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.POST_TYPE;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
public class ResponseLogFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletResponse response = context.getResponse();
        try {
            String responseData = getResponseData(context.getResponseDataStream());
            responseLog(context, responseData);

            if (StringUtils.isNotBlank(responseData)) {
                response.addHeader("Content-Type", "application/json;charset=utf-8");
                context.setResponseBody(responseData);
            }
        } catch (Exception e) {
            throw new ZuulException(e, INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }

        return null;
    }

    private String getResponseData(InputStream responseDataStream) throws IOException {
        if (responseDataStream == null) {
            return null;
        }
        String responseData = CharStreams.toString(new InputStreamReader(responseDataStream, UTF_8));
        if (responseData.length() > 1000) {
            responseData = responseData.substring(0, 1000);
        }
        return responseData;
    }

    private void responseLog(RequestContext context, String responseData) {
        HttpServletRequest request = context.getRequest();
        HttpServletResponse response = context.getResponse();
        int status = response.getStatus();

        long timestamp = NumberUtils.toLong((String) context.get("timestamp"));
        long duration = System.currentTimeMillis() - timestamp;
        String remoteIp = getRemoteIp(request);
        log.info("Response [{} {}, client={}, timestamp={}] [{}] [{}ms] responseBody={}", request.getMethod(), request.getRequestURL().toString(), remoteIp, timestamp, status, duration, responseData);
    }
}
