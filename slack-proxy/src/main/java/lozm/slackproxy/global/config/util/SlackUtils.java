package lozm.slackproxy.global.config.util;

import org.apache.commons.lang3.StringUtils;

public class SlackUtils {
    public static String getChannel(String slackMessage) {
        String CHANNEL = "\",\"channel\":\"";
        String TEXT = "\",\"text\":\"";
        int start = StringUtils.indexOf(slackMessage, CHANNEL);
        if (start > 0) {
            int end = StringUtils.indexOf(slackMessage, TEXT, start);
            return StringUtils.substring(slackMessage, start + CHANNEL.length(), end);
        }
        return null;
    }
}
