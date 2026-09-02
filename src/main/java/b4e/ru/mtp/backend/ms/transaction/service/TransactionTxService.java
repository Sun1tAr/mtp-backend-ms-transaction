package b4e.ru.mtp.backend.ms.transaction.service;

import b4e.ru.mtp.backend.ms.transaction.entity.TransactionEntity;

public interface TransactionTxService {

    TransactionEntity saveTransaction(TransactionEntity transaction);

}
