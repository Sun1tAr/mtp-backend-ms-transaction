package b4e.ru.mtp.backend.ms.transaction.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Transaction {

    private String id;
    private MoneyResource source;
    private MoneyResource target;
    private Long amount;
    private LocalDateTime created;
    private String description;

}
