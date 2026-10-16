package b4e.ru.mtp.backend.ms.transaction.api.dto;

import b4e.ru.mtp.backend.ms.transaction.domain.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetTransactionsResponse {

    private List<Transaction> transactions;

}
