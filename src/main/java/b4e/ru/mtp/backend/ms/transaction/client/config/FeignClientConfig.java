package b4e.ru.mtp.backend.ms.transaction.client.config;

import b4e.ru.mtp.backend.ms.transaction.api.dto.ErrorDtoResponse;
import b4e.ru.mtp.backend.ms.transaction.exception.MtpErrorCode;
import b4e.ru.mtp.backend.ms.transaction.exception.MtpException;
import feign.Client;
import feign.Logger;
import feign.Request;
import feign.Util;
import feign.codec.ErrorDecoder;
import feign.okhttp.OkHttpClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

@Configuration
@RequiredArgsConstructor
@Slf4j
public abstract class FeignClientConfig {

    private final int connectTimeout;
    private final int readTimeout;
    private final String loggerLevel;
    private final ObjectMapper mapper;


    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.valueOf(loggerLevel);
    }

    @Bean
    public Request.Options feignOptions() {
        return new Request.Options(connectTimeout, readTimeout, true);
    }

    @Bean  // Работает при не 2хх статусах
    public ErrorDecoder decoder() {
        return (methodKey, response) -> {
            if (response.body() != null) {
                try {
                    String rawMsg = Util.toString(response.body().asReader());
                    ErrorDtoResponse errorResponse = mapper.readValue(rawMsg, ErrorDtoResponse.class);
                    return new MtpException(errorResponse.getCode(), errorResponse.getMessage());
                } catch (Exception e) {
                    return new MtpException(MtpErrorCode.CLIENT_ERROR, e.getMessage());
                }
            }
            return new MtpException(MtpErrorCode.CLIENT_ERROR);
        };
    }

    @Bean
    public Client client() {
        return new OkHttpClient();
    }
}
