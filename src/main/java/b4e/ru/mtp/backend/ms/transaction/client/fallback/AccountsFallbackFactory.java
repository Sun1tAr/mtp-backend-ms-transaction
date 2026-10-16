package b4e.ru.mtp.backend.ms.transaction.client.fallback;

import b4e.ru.mtp.backend.ms.transaction.client.AccountsClient;
import b4e.ru.mtp.backend.ms.transaction.client.dto.ExecuteOrderRequest;
import b4e.ru.mtp.backend.ms.transaction.client.dto.GetMoneyResourceRequest;
import b4e.ru.mtp.backend.ms.transaction.client.dto.GetMoneyResourcesResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class AccountsFallbackFactory extends MtpFeignClientFallbackFactory<AccountsClient> {

    @Override
    public AccountsClient create(Throwable cause) {
        return new AccountsClient() {
            @Override
            public ResponseEntity<?> getAccountById(String id, Long userId) {
                return getResponseWithHeadedError(cause);
            }

            @Override
            public ResponseEntity<?> executeOrder(ExecuteOrderRequest request) {
                return getResponseWithHeadedError(cause);
            }

            @Override
            public ResponseEntity<?> getOrderStatus(ExecuteOrderRequest request) {
                return getResponseWithHeadedError(cause);
            }

            @Override
            public ResponseEntity<GetMoneyResourcesResponse> getResources(GetMoneyResourceRequest request, Long userId) {
                return (ResponseEntity<GetMoneyResourcesResponse>) getResponseWithHeadedError(cause);
            }
        };
    }
}
