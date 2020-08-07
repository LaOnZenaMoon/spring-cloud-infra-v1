package lozm.gateway.global.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.util.SubnetUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpStatus.*;

@Slf4j
@ToString
@Getter
@Setter
@Component
@Validated
@ConfigurationProperties("lozm")
public class ArrowApiKeys {
    @Size(min = 1)
    private List<ArrowApiKey> arrowApiKeys = new ArrayList<>();

    @PostConstruct
    public void init() {
        arrowApiKeys.forEach(ArrowApiKey::initArrowIpInfos);
    }

    public int arrowResCode(String requestKey, String remoteIp) {
        if ("0:0:0:0:0:0:0:1".equals(remoteIp))
            return SC_OK;
        for (ArrowApiKey arrowApiKey : arrowApiKeys) {
            if (arrowApiKey.getApiKey().equals(requestKey)) {
                if (arrowApiKey.isArrowIp(remoteIp)) {
                    return SC_OK;
                } else {
                    return SC_FORBIDDEN;
                }
            }
        }
        return SC_UNAUTHORIZED;
    }

    @Getter
    @Setter
    @ToString(exclude = "arrowIpInfos")
    public static class ArrowApiKey {
        private String site;
        @NotBlank
        private String apiKey;
        @Size(min = 1)
        private List<String> arrowIps;

        private List<SubnetUtils> arrowIpInfos;

        public boolean isArrowIp(String remoteIp) {
            initArrowIpInfos();
            return arrowIpInfos.stream()
                    .anyMatch(arrowIp -> arrowIp.getInfo().isInRange(remoteIp));
        }

        protected void initArrowIpInfos() {
            if (arrowIpInfos == null) {
                log.debug("initArrowIpInfos => key[{}] ipCount[{}]", apiKey, arrowIps.size());
                arrowIpInfos = new ArrayList<>();
                for (String arrowIp : arrowIps) {
                    arrowIpInfos.add(new SubnetUtils(arrowIp));
                }
            }
        }
    }
}
