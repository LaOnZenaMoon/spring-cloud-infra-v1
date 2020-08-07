package lozm.global.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.LayoutBase;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>SlackAppender</p>
 *
 * @version 2020.04.10
 * @see "https://github.com/maricn/logback-slack-appender"
 */
@Getter
@Setter
public class SlackAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

    private static final Layout<ILoggingEvent> defaultLayout = new LayoutBase<ILoggingEvent>() {
        public String doLayout(ILoggingEvent event) {
            return "[*" + event.getLevel() + "*] " +
                    event.getLoggerName() + "\n" +
                    event.getFormattedMessage();
        }
    };

    private String webhookUrl;
    private String token;
    private String channel;
    private String username;
    private String iconEmoji;
    private String iconUrl;
    private Boolean enabled = false;
    private Boolean colorCoding = true;
    private Layout<ILoggingEvent> layout = defaultLayout;

    private int timeout = 1000;

    @Override
    protected void append(final ILoggingEvent evt) {
        try {
            if (!enabled)
                return;
            if (StringUtils.isBlank(webhookUrl) || StringUtils.contains(webhookUrl, "UNDEFINED"))
                return;
            sendMessageWithWebhookUrl(evt);
        } catch (Exception ex) {
            ex.printStackTrace();
            addError("Error posting log to Slack.com (" + channel + "): " + evt, ex);
        }
    }

    private void sendMessageWithWebhookUrl(final ILoggingEvent evt) throws IOException {
        String[] parts = layout.doLayout(evt).split("\n", 2);

        Map<String, Object> message = new HashMap<>();
        message.put("channel", channel);
        message.put("username", username);
        message.put("icon_emoji", iconEmoji);
        message.put("icon_url", iconUrl);
        message.put("text", parts[0]);

        // Send the lines below the first line as an attachment.
        if (parts.length > 1 && parts[1].length() > 0) {
            Map<String, String> attachment = new HashMap<>();
            if (colorCoding) {
                attachment.put("color", colorByEvent(evt));
            }
            attachment.put("text", parts[1]);
            message.put("attachments", Collections.singletonList(attachment));
        }

        ObjectMapper objectMapper = new ObjectMapper();
        final byte[] bytes = objectMapper.writeValueAsBytes(message);

        postMessage(bytes);
    }

    private String colorByEvent(ILoggingEvent evt) {
        if (Level.ERROR.equals(evt.getLevel())) {
            return "danger";
        } else if (Level.WARN.equals(evt.getLevel())) {
            return "warning";
        } else if (Level.INFO.equals(evt.getLevel())) {
            return "good";
        }

        return "";
    }

    private void postMessage(byte[] bytes) throws IOException {
        final HttpURLConnection conn = (HttpURLConnection) new URL(webhookUrl).openConnection();
        conn.setConnectTimeout(timeout);
        conn.setReadTimeout(timeout);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setFixedLengthStreamingMode(bytes.length);
        conn.setRequestProperty("Content-Type", "application/json");

        final OutputStream os = conn.getOutputStream();
        os.write(bytes);

        os.flush();
        os.close();
    }

    public void setIconEmoji(String iconEmojiArg) {
        this.iconEmoji = iconEmojiArg;
        if (iconEmoji != null && !iconEmoji.isEmpty() && iconEmoji.startsWith(":") && !iconEmoji.endsWith(":")) {
            iconEmoji += ":";
        }
    }

}
