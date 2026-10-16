package b4e.ru.mtp.backend.ms.transaction.service;


import b4e.ru.mtp.backend.ms.transaction.api.MDCConstants;
import b4e.ru.mtp.backend.ms.transaction.exception.MtpErrorCode;
import b4e.ru.mtp.backend.ms.transaction.exception.MtpException;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

@Service
public class RequestContextService {

    public Long getUserId() {
        String s = MDC.get(MDCConstants.USER_ID);
        Long userId;

        if (s == null || s.isEmpty()) {
            throw new MtpException(MtpErrorCode.HEADER_USER_ID_NOT_FOUND);
        }

        try {
            userId = Long.parseLong(s);
        } catch (Exception e) {
            throw new MtpException(MtpErrorCode.HEADER_USER_ID_INVALID, e.getMessage());
        }
        return userId;
    }

    public void setUserId(String userId) {
        MDC.put(MDCConstants.USER_ID, userId);
    }

}
