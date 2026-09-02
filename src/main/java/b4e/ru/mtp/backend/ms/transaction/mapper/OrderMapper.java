package b4e.ru.mtp.backend.ms.transaction.mapper;


import b4e.ru.mtp.backend.commons.dto.domain.Order;
import b4e.ru.mtp.backend.ms.transaction.entity.OrderEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {


    Order mapOrderEntityToOrder(OrderEntity orderEntity);

    @AfterMapping
    default void afterMapping(@MappingTarget Order order, OrderEntity orderEntity) {
        order.setType(orderEntity.getTransaction().getType());
    }

}
