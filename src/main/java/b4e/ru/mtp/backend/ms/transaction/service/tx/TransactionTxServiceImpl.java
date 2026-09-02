package b4e.ru.mtp.backend.ms.transaction.service.tx;

import b4e.ru.mtp.backend.ms.transaction.entity.TransactionEntity;
import b4e.ru.mtp.backend.ms.transaction.repository.TransactionRepository;
import b4e.ru.mtp.backend.ms.transaction.service.TransactionTxService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionTxServiceImpl implements TransactionTxService {

    private final TransactionRepository transactionRepository;

    @Transactional
    public TransactionEntity saveTransaction(TransactionEntity transaction) {
        return transactionRepository.save(transaction);
    }

}
