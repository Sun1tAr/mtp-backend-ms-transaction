package b4e.ru.mtp.backend.ms.transaction.api.dto;

import b4e.ru.mtp.backend.ms.transaction.domain.OrderStatusCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetOrderStatusResponse {

    private OrderStatusCode orderStatus;

}
