package b4e.ru.mtp.backend.ms.transaction.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Account {

    private String id;
    private String title;
    private String description;
    private long amount;

}
