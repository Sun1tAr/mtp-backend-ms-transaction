package b4e.ru.mtp.backend.ms.transaction.controller;

import b4e.ru.mtp.backend.commons.dto.transaction.CreateTransactionRequest;
import b4e.ru.mtp.backend.ms.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/types")
    public ResponseEntity<?> getTransactionTypes() {
        return ResponseEntity.ok(transactionService.getTransactionTypes());
    }

    @PostMapping("/create")
    public ResponseEntity<?> createTransaction(@RequestBody CreateTransactionRequest request) {
        transactionService.createTransaction(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<?> getTransactions() {
        return ResponseEntity.ok(transactionService.getTransactions());
    }


}
