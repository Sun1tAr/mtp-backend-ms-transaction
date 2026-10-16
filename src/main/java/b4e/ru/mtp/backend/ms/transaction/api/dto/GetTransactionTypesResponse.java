package b4e.ru.mtp.backend.ms.transaction.api.dto;

import b4e.ru.mtp.backend.ms.transaction.domain.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetTransactionTypesResponse {

    List<TransactionType> allowedTypes;

}
