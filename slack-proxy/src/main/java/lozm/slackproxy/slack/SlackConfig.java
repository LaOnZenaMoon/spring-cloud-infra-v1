package lozm.slackproxy.slack;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "slack")
public class SlackConfig {
    private String baseUrl;
    private String baseWebhookUrl;
    private List<Webhook> webhooks = new ArrayList<>();

    public String getWebhookUrl(String channel) {
        String url = baseWebhookUrl;
        for (Webhook webhook : webhooks) {
            if (webhook.getChannel().equals(channel)) {
                url = webhook.getUrl();
                break;
            }
        }

        if(StringUtils.isBlank(url))
            url = baseWebhookUrl;
        return url;
    }

    @Getter
    @Setter
    public static class Webhook {
        private String channel;
        private String url;
    }
}
