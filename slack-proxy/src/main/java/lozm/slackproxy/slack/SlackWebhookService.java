package lozm.slackproxy.slack;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
@RequiredArgsConstructor
public class SlackWebhookService {
    private final RestTemplate restTemplate;
    private final SlackConfig slackConfig;

    public ResponseEntity<String> send(String message) {
        return sendWebhook(slackConfig.getBaseWebhookUrl(), message);
    }

    public ResponseEntity<String> send(String message, String channel) {
        String url = slackConfig.getWebhookUrl(channel);

        return sendWebhook(url, message);
    }

    public ResponseEntity<String> sendWebhook(String url, String message) {
        ResponseEntity<String> response = restTemplate.postForEntity(url, message, String.class);
        logResponse(url, response);
        return response;
    }

    private void logResponse(String url, ResponseEntity<String> response) {
        log.debug("Slack Response [url={}, code={}, body={}]", url, response.getStatusCode(), response.getBody());
    }
}
