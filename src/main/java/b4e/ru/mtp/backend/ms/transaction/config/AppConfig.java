package b4e.ru.mtp.backend.ms.transaction.config;

import b4e.ru.mtp.backend.commons.config.FilterConfig;
import b4e.ru.mtp.backend.ms.transaction.client.AccountsClient;
import b4e.ru.mtp.backend.ms.transaction.client.CategoriesClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackageClasses = {CategoriesClient.class, AccountsClient.class})
@ComponentScan(basePackageClasses = {FilterConfig.class})
public class AppConfig {
}
