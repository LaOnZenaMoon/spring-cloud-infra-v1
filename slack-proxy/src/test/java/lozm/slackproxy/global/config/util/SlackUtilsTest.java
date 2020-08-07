package lozm.slackproxy.global.config.util;

import lozm.slackproxy.global.config.util.SlackUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SlackUtilsTest {

    @Test
    public void getChannelTest() {
        String slackMsg = "{\"icon_url\":null,\"attachments\":[{\"color\":\"warning\",\"text\":\"Runner Start!!\\n\"}],\"icon_emoji\":\":fire:\",\"channel\":\"infra-prod\",\"text\":\"[*WARN*] lozm}";
        String channel = SlackUtils.getChannel(slackMsg);
        assertEquals("infra-prod", channel);
    }

}