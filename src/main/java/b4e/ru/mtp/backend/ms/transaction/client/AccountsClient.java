package b4e.ru.mtp.backend.ms.transaction.client;

import b4e.ru.mtp.backend.ms.transaction.api.MtpHttpHeaders;
import b4e.ru.mtp.backend.ms.transaction.api.dto.ExecuteOrderRequest;
import b4e.ru.mtp.backend.ms.transaction.api.dto.GetMoneyResourceRequest;
import b4e.ru.mtp.backend.ms.transaction.api.dto.GetMoneyResourcesResponse;
import b4e.ru.mtp.backend.ms.transaction.client.config.AccountsClientConfig;
import b4e.ru.mtp.backend.ms.transaction.client.fallback.AccountsFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "accounts-client",
        configuration = AccountsClientConfig.class,
        fallbackFactory = AccountsFallbackFactory.class,
        url = "${ms.feign-clients.accounts-client.url}"
)
public interface AccountsClient {


    @GetMapping("/accounts/by-id")
    ResponseEntity<?> getAccountById(
            @RequestParam String id,
            @RequestHeader(MtpHttpHeaders.USER_ID) Long userId
    );

    @PostMapping("/orders/execute")
    ResponseEntity<?> executeOrder(@RequestBody ExecuteOrderRequest request);

    @PostMapping("/orders/status")
    ResponseEntity<?> getOrderStatus(@RequestBody ExecuteOrderRequest request);

    @PostMapping("/accounts/resources")
    ResponseEntity<GetMoneyResourcesResponse> getResources(
            @RequestBody GetMoneyResourceRequest request,
            @RequestHeader(MtpHttpHeaders.USER_ID) Long userId
    );

}
