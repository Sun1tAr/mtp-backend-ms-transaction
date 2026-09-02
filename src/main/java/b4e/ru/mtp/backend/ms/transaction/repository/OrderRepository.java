package b4e.ru.mtp.backend.ms.transaction.repository;

import b4e.ru.mtp.backend.ms.transaction.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {

    @Query(value = "select o from OrderEntity o " +
            "right join TransactionEntity t on t.order = o " +
            "where o.id = :uuid")
    Optional<OrderEntity> findById(@Param("uuid") UUID uuid);

}
