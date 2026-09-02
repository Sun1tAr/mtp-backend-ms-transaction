package b4e.ru.mtp.backend.ms.transaction.service.impl;

import b4e.ru.mtp.backend.commons.dto.domain.Order;
import b4e.ru.mtp.backend.commons.dto.domain.OrderStatusCode;
import b4e.ru.mtp.backend.commons.exception.MtpErrorCode;
import b4e.ru.mtp.backend.commons.exception.MtpException;
import b4e.ru.mtp.backend.ms.transaction.entity.OrderEntity;
import b4e.ru.mtp.backend.ms.transaction.mapper.OrderMapper;
import b4e.ru.mtp.backend.ms.transaction.service.IntegrationService;
import b4e.ru.mtp.backend.ms.transaction.service.OrderExecutorService;
import b4e.ru.mtp.backend.ms.transaction.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderExecutorServiceImpl implements OrderExecutorService {

    private final OrderService orderService;
    private final OrderMapper orderMapper;
    private final IntegrationService integrationService;

    @Value("${ms.order-executor.max-retry-counts}")
    private int maxCountRetry;

    @Value("${ms.order-executor.retry-delay-ms}")
    private long retryDelayMs;


    @Override
    public void executeOrder(UUID orderId) {
        log.debug("Начало исполнения заявки [{}]", orderId);
        OrderEntity orderEntity = orderService.findById(orderId);

        if (orderEntity.getStatus() == OrderStatusCode.EXECUTED) {
            log.warn("Заявка уже выполнена [{}]", orderId);
            return;
        }
        orderService.startOrderExecuting(orderEntity);
        executeOrderWithRetry(orderEntity);
    }

    private void executeOrderWithRetry(OrderEntity orderEntity) {
        Order order = orderMapper.mapOrderEntityToOrder(orderEntity);
        boolean orderExecuted = integrationService.executeOrder(order);
        if (orderExecuted) {
            orderService.endOrderExecuting(orderEntity);
        } else {
            retryOrderExecuting(orderEntity);
        }
        log.debug("Заявка исполнена [{}]", orderEntity.getId());
    }

    private void retryOrderExecuting(OrderEntity orderEntity){
        Order order = orderMapper.mapOrderEntityToOrder(orderEntity);
        OrderStatusCode status = integrationService.getOrderStatus(order);
        for (int i = 1; i <= maxCountRetry; i++) {
            log.debug("Начало повторного исполнения заявки [{}], попытка [{}/{}]", orderEntity.getId(), i, maxCountRetry);
            switch (status) {
                case EXECUTED: {
                    orderService.endOrderExecuting(orderEntity);
                    return;
                }
                case UNREGISTERED: {
                    executeOrderWithRetry(orderEntity);
                    return;
                }
                case CREATED: {
                    try {
                        Thread.sleep(retryDelayMs);
                    } catch (InterruptedException e) {
                        throw new MtpException(MtpErrorCode.INTERNAL_ERROR, "message = " + e.getMessage());
                    }
                    status = integrationService.getOrderStatus(order);
                }
            }
        }
    }
}
