package b4e.ru.mtp.backend.ms.transaction.service.impl;

import b4e.ru.mtp.backend.commons.api.MtpHttpHeaders;
import b4e.ru.mtp.backend.commons.dto.acc.ExecuteOrderRequest;
import b4e.ru.mtp.backend.commons.dto.acc.GetOrderStatusResponse;
import b4e.ru.mtp.backend.commons.dto.common.GetMoneyResourceRequest;
import b4e.ru.mtp.backend.commons.dto.common.GetMoneyResourcesResponse;
import b4e.ru.mtp.backend.commons.dto.domain.MoneyResource;
import b4e.ru.mtp.backend.commons.dto.domain.Order;
import b4e.ru.mtp.backend.commons.dto.domain.OrderStatusCode;
import b4e.ru.mtp.backend.commons.exception.MtpErrorCode;
import b4e.ru.mtp.backend.commons.exception.MtpException;
import b4e.ru.mtp.backend.commons.service.RequestContextService;
import b4e.ru.mtp.backend.ms.transaction.client.AccountsClient;
import b4e.ru.mtp.backend.ms.transaction.client.CategoriesClient;
import b4e.ru.mtp.backend.ms.transaction.service.IntegrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class IntegrationServiceImpl implements IntegrationService {

    private final AccountsClient accountsClient;
    private final CategoriesClient categoriesClient;
    private final RequestContextService requestContextService;

    @Override
    public void validateAccount(String accountId) {
        Long userId = requestContextService.getUserId();
        ResponseEntity<?> response = accountsClient.getAccountById(accountId, userId);
        if (!responseIsValid(response)) {
            throw new MtpException(MtpErrorCode.ACCOUNT_NOT_FOUND);
        }
    }

    @Override
    public void validateCategory(String categoryId) {
        Long userId = requestContextService.getUserId();
        ResponseEntity<?> response = categoriesClient.getCategoryById(categoryId, userId);
        if (!responseIsValid(response)) {
            throw new MtpException(MtpErrorCode.CATEGORY_NOT_FOUND);
        }
    }

    @Override
    public boolean executeOrder(Order order) {
        ExecuteOrderRequest req = new ExecuteOrderRequest(order);
        ResponseEntity<?> resp = accountsClient.executeOrder(req);
        return responseIsValid(resp);
    }

    @Override
    public OrderStatusCode getOrderStatus(Order order) {
        ExecuteOrderRequest req = new ExecuteOrderRequest(order);
        ResponseEntity<?> resp = accountsClient.getOrderStatus(req);
        if (responseIsValid(resp) && resp.getBody() instanceof GetOrderStatusResponse body) {
            return body.getOrderStatus();
        } else {
            throw new MtpException(MtpErrorCode.INTERNAL_ERROR, "При запросе статуса заявки изменился тип ответа");
        }
    }

    @Override
    public List<MoneyResource> getResources(List<UUID> categories, List<UUID> accounts) {
        Long userId = requestContextService.getUserId();
        ResponseEntity<GetMoneyResourcesResponse> catResourcesRaw = categoriesClient.getResources(new GetMoneyResourceRequest(categories), userId);
        ResponseEntity<GetMoneyResourcesResponse> accResourcesRaw = accountsClient.getResources(new GetMoneyResourceRequest(accounts), userId);
        if (responseIsValid(catResourcesRaw) &&
                responseIsValid(accResourcesRaw)
        ) {
            GetMoneyResourcesResponse cats = (GetMoneyResourcesResponse) catResourcesRaw.getBody();
            GetMoneyResourcesResponse accs = (GetMoneyResourcesResponse) accResourcesRaw.getBody();
            List<MoneyResource> resources = cats.getResources();
            resources.addAll(accs.getResources());
            return resources;
        } else {
            throw new MtpException(MtpErrorCode.INTERNAL_ERROR, "При запросе ресурсов изменился тип ответа");
        }
    }

    boolean responseIsValid(ResponseEntity<?> response) {
        boolean valid = response.getStatusCode().is2xxSuccessful();
        List<String> errorCode = response.getHeaders().get(MtpHttpHeaders.ERROR_CODE);
        valid = valid && errorCode == null;
        return valid;
    }

}
