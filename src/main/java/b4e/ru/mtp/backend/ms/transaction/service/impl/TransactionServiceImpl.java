package b4e.ru.mtp.backend.ms.transaction.service.impl;

import b4e.ru.mtp.backend.ms.transaction.api.dto.CreateTransactionRequest;
import b4e.ru.mtp.backend.ms.transaction.api.dto.GetTransactionTypesResponse;
import b4e.ru.mtp.backend.ms.transaction.api.dto.GetTransactionsResponse;
import b4e.ru.mtp.backend.ms.transaction.domain.MoneyResource;
import b4e.ru.mtp.backend.ms.transaction.domain.OrderStatusCode;
import b4e.ru.mtp.backend.ms.transaction.domain.Transaction;
import b4e.ru.mtp.backend.ms.transaction.domain.TransactionStatusCode;
import b4e.ru.mtp.backend.ms.transaction.domain.TransactionType;
import b4e.ru.mtp.backend.ms.transaction.domain.TransactionTypeCode;
import b4e.ru.mtp.backend.ms.transaction.entity.OrderEntity;
import b4e.ru.mtp.backend.ms.transaction.entity.TransactionEntity;
import b4e.ru.mtp.backend.ms.transaction.exception.MtpErrorCode;
import b4e.ru.mtp.backend.ms.transaction.exception.MtpException;
import b4e.ru.mtp.backend.ms.transaction.repository.TransactionRepository;
import b4e.ru.mtp.backend.ms.transaction.service.IntegrationService;
import b4e.ru.mtp.backend.ms.transaction.service.OrderExecutorService;
import b4e.ru.mtp.backend.ms.transaction.service.RequestContextService;
import b4e.ru.mtp.backend.ms.transaction.service.TransactionService;
import b4e.ru.mtp.backend.ms.transaction.service.TransactionTxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final RequestContextService requestContextService;
    private final OrderExecutorService  orderExecutorService;
    private final IntegrationService integrationService;
    private final TransactionTxService txService;
    private final TransactionRepository transactionRepository;

    @Override
    public void createTransaction(CreateTransactionRequest request) {
        Long userId = requestContextService.getUserId();

        long amount = request.getAmount();
        if (amount <= 0) {
            throw new MtpException(MtpErrorCode.TRANSACTION_AMOUNT_INVALID);
        }

        TransactionTypeCode type = TransactionTypeCode.valueOf(request.getType());
        switch (type) {
            case REVENUE: {
                integrationService.validateCategory(request.getSource());
                integrationService.validateAccount(request.getTarget());
                break;
            }
            case EXPENSE: {
                integrationService.validateAccount(request.getSource());
                integrationService.validateCategory(request.getTarget());
                break;
            }
            case TRANSFER: {
                integrationService.validateAccount(request.getSource());
                integrationService.validateAccount(request.getTarget());
                break;
            }
        }

        OrderEntity order = OrderEntity.builder()
                .source(UUID.fromString(request.getSource()))
                .target(UUID.fromString(request.getTarget()))
                .amount(amount)
                .status(OrderStatusCode.CREATED)
        .build();

        TransactionEntity transaction = TransactionEntity.builder()
                .description(request.getDescription())
                .ownerId(userId)
                .source(order.getSource())
                .target(order.getTarget())
                .amount(amount)
                .createdAt(request.getCreatedAt())
                .status(TransactionStatusCode.CREATED)
                .type(type)
                .order(order)
        .build();
        order.setTransaction(transaction);
        TransactionEntity created = txService.saveTransaction(transaction);
        orderExecutorService.executeOrder(created.getOrder().getId());
    }

    @Override
    public GetTransactionsResponse getTransactions() {
        log.debug("Начало получения транзакций");
        Long userId = requestContextService.getUserId();
        List<TransactionEntity> transactionEntities = transactionRepository.findAllByUserId(userId);
        log.debug("Найдено [{}] транзакций", transactionEntities.size());

        Set<UUID> categories = new HashSet<>();
        Set<UUID> accounts = new HashSet<>();
        transactionEntities.forEach(transaction -> {
            switch(transaction.getType()) {
                case REVENUE: {
                    categories.add(transaction.getSource());
                    accounts.add(transaction.getTarget());
                }
                case EXPENSE: {
                    accounts.add(transaction.getSource());
                    categories.add(transaction.getTarget());
                }
                case TRANSFER: {
                    accounts.add(transaction.getSource());
                    accounts.add(transaction.getTarget());
                }
            }
        });

        List<UUID> categoriesList = categories.stream().toList();
        List<UUID> accountsList = accounts.stream().toList();
        log.debug("Найдено [{}] категорий и [{}] счетов", categoriesList.size(), accountsList.size());

        List<MoneyResource> resources = integrationService.getResources(categoriesList, accountsList);
        log.debug("Получено [{}] ресурсов", resources.size());

        Map<UUID, MoneyResource> resourceMap = new HashMap<>();
        resources.forEach(res -> resourceMap.put(UUID.fromString(res.getId()), res));

        List<Transaction> transactions = mapToDomain(transactionEntities, resourceMap);
        return new GetTransactionsResponse(transactions);
    }


    @Override
    public GetTransactionTypesResponse getTransactionTypes() {
        List<TransactionType> list =
                Arrays.stream(TransactionTypeCode.values())
                        .map(t -> new TransactionType(t.getTitle(), t))
                        .toList();
        GetTransactionTypesResponse response = new GetTransactionTypesResponse(list);
        return response;
    }

    private List<Transaction> mapToDomain(
            List<TransactionEntity> entities,
            Map<UUID, MoneyResource> resourceMap
    ) {
        return entities.stream().map(ent -> {
            Transaction t = new Transaction();
            t.setDescription(ent.getDescription());
            t.setAmount(ent.getAmount());
            t.setId(ent.getId().toString());
            t.setAmount(ent.getAmount());
            t.setTarget(resourceMap.get(ent.getTarget()));
            t.setSource(resourceMap.get(ent.getSource()));
            return t;
        }).toList();
    }










}
