package b4e.ru.mtp.backend.ms.transaction.client.fallback;

import b4e.ru.mtp.backend.commons.api.MtpHttpHeaders;
import b4e.ru.mtp.backend.commons.exception.MtpErrorCode;
import b4e.ru.mtp.backend.commons.exception.MtpException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

@Slf4j
public abstract class MtpFeignClientFallbackFactory<T> implements FallbackFactory<T> {

    protected ResponseEntity<?> getResponseWithHeadedError(Throwable cause) {
        MtpErrorCode code = MtpErrorCode.CLIENT_ERROR;
        if (cause instanceof MtpException e) {
            code = e.getCode();
        } else if (cause instanceof FeignException) {
            code = MtpErrorCode.CLIENT_SERVICE_UNAVAILABLE;
        } else {
            throw new MtpException(code,
                    "Отловлено неожидаемое исключение: " +
                            "\nclass = [" + cause.getClass().getSimpleName() + "]" +
                            "\nmessage = [" + cause.getMessage() + "]" +
                            "\nclient = [" + this.getClass().getSimpleName() + "]"
            );
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(MtpHttpHeaders.ERROR_CODE, code.name());
        return new ResponseEntity<>(
                null,
                headers,
                code.getHttpStatus()
        );
    }


}
