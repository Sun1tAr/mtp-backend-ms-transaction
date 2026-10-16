package b4e.ru.mtp.backend.ms.transaction.service;


import b4e.ru.mtp.backend.ms.transaction.domain.MoneyResource;
import b4e.ru.mtp.backend.ms.transaction.domain.Order;
import b4e.ru.mtp.backend.ms.transaction.domain.OrderStatusCode;

import java.util.List;
import java.util.UUID;

public interface IntegrationService {

    void validateAccount(String accountId);
    void validateCategory(String categoryId);
    boolean executeOrder(Order order);


    OrderStatusCode getOrderStatus(Order order);
    List<MoneyResource> getResources(List<UUID> categories, List<UUID> accounts);
}
