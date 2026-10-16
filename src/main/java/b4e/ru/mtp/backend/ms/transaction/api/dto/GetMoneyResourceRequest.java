package b4e.ru.mtp.backend.ms.transaction.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetMoneyResourceRequest {

    private List<UUID> findIds;

}
