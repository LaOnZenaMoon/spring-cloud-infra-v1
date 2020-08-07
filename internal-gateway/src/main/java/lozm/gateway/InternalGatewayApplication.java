package lozm.gateway;

import lozm.gateway.filters.PreFilter;
import lozm.gateway.filters.ResponseLogFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "lozm")
public class InternalGatewayApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplicationBuilder()
                .sources(InternalGatewayApplication.class)
                .listeners(new ApplicationPidFileWriter("./application.pid"))
                .build();
        application.run(args);
    }

    @Bean
    public PreFilter preFilter() {
        return new PreFilter();
    }

    @Bean
    public ResponseLogFilter responseLogFilter() {
        return new ResponseLogFilter();
    }
}
