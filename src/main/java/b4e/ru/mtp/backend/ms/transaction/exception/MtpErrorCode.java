package b4e.ru.mtp.backend.ms.transaction.exception;

import b4e.ru.mtp.backend.ms.transaction.api.MtpHttpHeaders;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum MtpErrorCode {

    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Произошла непредвиденная ошибка"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Переданы невалидные данные"),
    REQUEST_VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "Данные не соответствуют политике валидации"),
    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "Указанный счет не найден"),
    HEADER_USER_ID_NOT_FOUND(HttpStatus.BAD_REQUEST, "Заголовок " + MtpHttpHeaders.USER_ID + " не найден"),
    HEADER_USER_ID_INVALID(HttpStatus.BAD_REQUEST, "Заголовок " + MtpHttpHeaders.USER_ID + " содержит некорректные данные"),
    CLIENT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Непредвиденная ошибка работы с клиентом"),
    CLIENT_SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "Вызываемый клиент недоступен"),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "Категория не найдена"),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "Заявка на перемещение средств не найдена"),
    TRANSACTION_AMOUNT_INVALID(HttpStatus.BAD_REQUEST, "Сумма транзакции должна быть положительна"),
    ;


    private final HttpStatus httpStatus;
    private final String message;

    MtpErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
