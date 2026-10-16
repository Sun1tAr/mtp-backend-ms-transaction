package b4e.ru.mtp.backend.ms.transaction.api.dto;

import b4e.ru.mtp.backend.ms.transaction.domain.MoneyResource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetMoneyResourcesResponse {

    List<MoneyResource> resources;

}
