package b4e.ru.mtp.backend.ms.transaction.client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

@Configuration
public class CategoriesClientConfig extends FeignClientConfig {

    protected CategoriesClientConfig(
            @Value("${ms.feign-clients.categories-client.connect-timeout}") int connectTimeout,
            @Value("${ms.feign-clients.categories-client.read-timeout}") int readTimeout,
            @Value("${ms.feign-clients.categories-client.logger-level}") String loggerLevel,
            ObjectMapper mapper
    ) {
        super(connectTimeout, readTimeout, loggerLevel, mapper);
    }

}
