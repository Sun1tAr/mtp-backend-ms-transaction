package b4e.ru.mtp.backend.ms.transaction.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MoneyResource {

    private String id;
    private MoneyResourceType type;
    private String title;

}
