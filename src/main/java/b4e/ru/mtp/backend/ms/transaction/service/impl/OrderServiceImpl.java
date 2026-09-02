package b4e.ru.mtp.backend.ms.transaction.service.impl;

import b4e.ru.mtp.backend.commons.dto.domain.OrderStatusCode;
import b4e.ru.mtp.backend.commons.dto.domain.TransactionStatusCode;
import b4e.ru.mtp.backend.commons.exception.MtpErrorCode;
import b4e.ru.mtp.backend.commons.exception.MtpException;
import b4e.ru.mtp.backend.ms.transaction.entity.OrderEntity;
import b4e.ru.mtp.backend.ms.transaction.entity.TransactionEntity;
import b4e.ru.mtp.backend.ms.transaction.repository.OrderRepository;
import b4e.ru.mtp.backend.ms.transaction.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {


    private final OrderRepository orderRepository;

    @Override
    public OrderEntity findById(UUID orderId) {
        Optional<OrderEntity> optional = orderRepository.findById(orderId);
        if (optional.isEmpty()) {
            throw new MtpException(MtpErrorCode.ORDER_NOT_FOUND, "uuid =  " + orderId);
        }
        return optional.get();
    }

    @Override
    @Transactional
    public void startOrderExecuting(OrderEntity orderEntity) {
        updateOrderStatus(orderEntity, OrderStatusCode.EXECUTING);
    }

    @Override
    @Transactional
    public void endOrderExecuting(OrderEntity orderEntity) {
        TransactionEntity transaction = orderEntity.getTransaction();
        transaction.setStatus(TransactionStatusCode.EXECUTED);
        updateOrderStatus(orderEntity, OrderStatusCode.EXECUTED);
    }

    private void updateOrderStatus(OrderEntity orderEntity, OrderStatusCode newStatus) {
        orderEntity.setStatus(newStatus);
        orderRepository.save(orderEntity);
    }


}
