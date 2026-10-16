package b4e.ru.mtp.backend.ms.transaction.domain;


import lombok.Getter;

public enum TransactionTypeCode {
    REVENUE("Доход"), // доход: категория --сумма--> счет
    EXPENSE("Расход"), // расход: счет --сумма--> категория
    TRANSFER("Перевод") // перевод: счет --сумма--> счет
    ;

    @Getter
    private final String title;

    TransactionTypeCode(String title) {
        this.title = title;
    }
}
