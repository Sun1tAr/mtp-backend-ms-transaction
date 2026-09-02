package b4e.ru.mtp.backend.ms.transaction.service;

import java.util.UUID;

public interface OrderExecutorService {

    void executeOrder(UUID orderId);



}
