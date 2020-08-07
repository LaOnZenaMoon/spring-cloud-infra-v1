package lozm.gateway.global.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AppRunner implements ApplicationRunner {
    private final ArrowApiKeys arrowApiKeys;

    @Override
    public void run(ApplicationArguments args) {
        log.info("arrowApiKeys : " + arrowApiKeys.toString());
    }
}
