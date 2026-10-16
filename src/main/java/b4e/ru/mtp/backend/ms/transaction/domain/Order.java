package b4e.ru.mtp.backend.ms.transaction.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Order {

    private UUID id;
    private UUID source;
    private UUID target;
    private long amount;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private TransactionTypeCode type;

}
