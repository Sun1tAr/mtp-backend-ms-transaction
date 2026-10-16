package b4e.ru.mtp.backend.ms.transaction.client;

import b4e.ru.mtp.backend.ms.transaction.api.MtpHttpHeaders;
import b4e.ru.mtp.backend.ms.transaction.api.dto.GetMoneyResourceRequest;
import b4e.ru.mtp.backend.ms.transaction.api.dto.GetMoneyResourcesResponse;
import b4e.ru.mtp.backend.ms.transaction.client.config.CategoriesClientConfig;
import b4e.ru.mtp.backend.ms.transaction.client.fallback.CategoriesFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "categories-client",
        configuration = CategoriesClientConfig.class,
        fallbackFactory = CategoriesFallbackFactory.class,
        url = "${ms.feign-clients.categories-client.url}"
)
public interface CategoriesClient {

    @GetMapping("/categories/by-id")
    ResponseEntity<?> getCategoryById(
            @RequestParam("id") String id,
            @RequestHeader(MtpHttpHeaders.USER_ID) Long userId
    );

    @PostMapping("/categories/resources")
    ResponseEntity<GetMoneyResourcesResponse> getResources(
            @RequestBody GetMoneyResourceRequest request,
            @RequestHeader(MtpHttpHeaders.USER_ID) Long userId
    );

}
