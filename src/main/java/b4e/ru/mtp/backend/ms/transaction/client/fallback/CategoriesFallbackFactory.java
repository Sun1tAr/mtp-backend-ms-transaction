package b4e.ru.mtp.backend.ms.transaction.client.fallback;

import b4e.ru.mtp.backend.ms.transaction.client.CategoriesClient;
import b4e.ru.mtp.backend.ms.transaction.client.dto.GetMoneyResourceRequest;
import b4e.ru.mtp.backend.ms.transaction.client.dto.GetMoneyResourcesResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CategoriesFallbackFactory extends MtpFeignClientFallbackFactory<CategoriesClient>{
    @Override
    public CategoriesClient create(Throwable cause) {
        return new CategoriesClient() {
            @Override
            public ResponseEntity<?> getCategoryById(String id, Long userId) {
                return getResponseWithHeadedError(cause);
            }

            @Override
            public ResponseEntity<GetMoneyResourcesResponse> getResources(GetMoneyResourceRequest request, Long userId) {
                return (ResponseEntity<GetMoneyResourcesResponse>) getResponseWithHeadedError(cause);
            }
        };
    }
}
