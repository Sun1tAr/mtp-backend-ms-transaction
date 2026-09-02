package b4e.ru.mtp.backend.ms.transaction.client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

@Configuration
public class AccountsClientConfig extends FeignClientConfig {

    protected AccountsClientConfig(
            @Value("${ms.feign-clients.accounts-client.connect-timeout}") int connectTimeout,
            @Value("${ms.feign-clients.accounts-client.read-timeout}") int readTimeout,
            @Value("${ms.feign-clients.accounts-client.logger-level}") String loggerLevel,
            ObjectMapper mapper
    ) {
        super(connectTimeout, readTimeout, loggerLevel, mapper);
    }

}
