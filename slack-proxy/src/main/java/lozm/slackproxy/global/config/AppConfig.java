package lozm.slackproxy.global.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import static java.nio.charset.StandardCharsets.UTF_8;

@Configuration
public class AppConfig {
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(UTF_8));
        return restTemplate;
    }

    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
        loggingFilter.setIncludeQueryString(true);
        loggingFilter.setIncludeClientInfo(true);
        loggingFilter.setIncludeHeaders(true);
        loggingFilter.setIncludePayload(true);
        loggingFilter.setBeforeMessagePrefix("Request [");
        loggingFilter.setAfterMessagePrefix("Response[");
        loggingFilter.setMaxPayloadLength(1024);
        return loggingFilter;
    }

    @Bean
    public FilterRegistrationBean<CommonsRequestLoggingFilter> loggingFilterRegistration() {
        FilterRegistrationBean<CommonsRequestLoggingFilter> registration = new FilterRegistrationBean<>(requestLoggingFilter());
        registration.addUrlPatterns("/slack/*");
        return registration;
    }
}
