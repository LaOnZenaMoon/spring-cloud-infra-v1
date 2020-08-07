package lozm.slackproxy.api;

import lozm.slackproxy.slack.SlackConfig;
import lozm.slackproxy.slack.SlackWebhookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lozm.slackproxy.global.config.util.SlackUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SlackMessageController {

    private final SlackWebhookService slackWebhookService;
    private final SlackConfig slackConfig;

    @PostMapping("/slack/webhook")
    public ResponseEntity<String> sendWebhookDefault(@RequestBody String slackMessage) {
        try {
            String channel = SlackUtils.getChannel(slackMessage);
            return slackWebhookService.send(slackMessage, channel);
        } catch (HttpClientErrorException e) {
            return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
        }
    }

    @PostMapping("/slack/channels/{channel}/**")
    public ResponseEntity<String> sendWebhookDefault(@RequestBody String slackMessage, @PathVariable("channel") String channel) {
        try {
            return slackWebhookService.send(slackMessage, channel);
        } catch (HttpClientErrorException e) {
            return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
        }
    }

    @PostMapping("/slack/webhook/slow")
    public ResponseEntity<String> sendWebhookDefaultSlow(@RequestBody String slackMessage) {
        try {
            Thread.sleep(10000);
            return slackWebhookService.send(slackMessage);
        } catch (InterruptedException e) {
            return null;
        } catch (HttpClientErrorException e) {
            return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
        }
    }

    @PostMapping("/slack/services/**")
    public ResponseEntity<String> sendWebhook(@RequestBody String slackMessage, HttpServletRequest request) {
        try {
            String restOfTheUrl = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
            String slackUrl = slackConfig.getBaseUrl() + StringUtils.replace(restOfTheUrl, "/slack", "");
            return slackWebhookService.sendWebhook(slackUrl, slackMessage);
        } catch (HttpClientErrorException e) {
            return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
        }
    }

}
