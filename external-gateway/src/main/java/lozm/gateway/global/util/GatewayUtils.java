package lozm.gateway.global.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class GatewayUtils {
    /**
     * nginx config
     * proxy_set_header X-Real-IP $remote_addr
     */
    public static String getRemoteIp(HttpServletRequest request) {

        String remoteIp = request.getHeader("X-Real-IP");
        if (StringUtils.isBlank(remoteIp))
            return request.getRemoteAddr(); //nginx server ip
        return remoteIp;
    }
}
