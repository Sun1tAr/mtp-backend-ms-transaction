package b4e.ru.mtp.backend.ms.transaction.service;

import b4e.ru.mtp.backend.ms.transaction.entity.OrderEntity;

import java.util.UUID;

public interface OrderService {

    OrderEntity findById(UUID orderId);

    void startOrderExecuting(OrderEntity orderEntity);
    void endOrderExecuting(OrderEntity orderEntity);

}
