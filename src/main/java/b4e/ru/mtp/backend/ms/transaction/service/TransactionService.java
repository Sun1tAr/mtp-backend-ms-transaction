package b4e.ru.mtp.backend.ms.transaction.service;


import b4e.ru.mtp.backend.ms.transaction.api.dto.CreateTransactionRequest;
import b4e.ru.mtp.backend.ms.transaction.api.dto.GetTransactionTypesResponse;
import b4e.ru.mtp.backend.ms.transaction.api.dto.GetTransactionsResponse;

public interface TransactionService {

    GetTransactionTypesResponse getTransactionTypes();
    void createTransaction(CreateTransactionRequest request);
    GetTransactionsResponse getTransactions();

}
