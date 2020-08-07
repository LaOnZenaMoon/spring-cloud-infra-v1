package lozm.admin;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAdminServer
@EnableDiscoveryClient
@EnableScheduling
@SpringBootApplication
public class SpringBootAdminApplication {


    public static void main(String[] args) {
        SpringApplication application = new SpringApplicationBuilder()
                .sources(SpringBootAdminApplication.class)
                .listeners(new ApplicationPidFileWriter("./application.pid"))
                .build();
        application.run(args);
    }
}
