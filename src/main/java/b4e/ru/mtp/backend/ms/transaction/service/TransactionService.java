package b4e.ru.mtp.backend.ms.transaction.service;

import b4e.ru.mtp.backend.commons.dto.transaction.CreateTransactionRequest;
import b4e.ru.mtp.backend.commons.dto.transaction.GetTransactionTypesResponse;
import b4e.ru.mtp.backend.commons.dto.transaction.GetTransactionsResponse;

public interface TransactionService {

    GetTransactionTypesResponse getTransactionTypes();
    void createTransaction(CreateTransactionRequest request);
    GetTransactionsResponse getTransactions();

}
