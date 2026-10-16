package b4e.ru.mtp.backend.ms.transaction.api.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTransactionRequest {

    private String description;
    private String type;
    private long amount;
    private String source;
    private String target;
    private LocalDateTime createdAt;


}
