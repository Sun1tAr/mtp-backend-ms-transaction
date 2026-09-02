package b4e.ru.mtp.backend.ms.transaction.repository;

import b4e.ru.mtp.backend.ms.transaction.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<TransactionEntity, UUID> {

    @Query("select t from TransactionEntity t where t.ownerId = :userId")
    List<TransactionEntity> findAllByUserId(@Param("userId") Long userId);

}
