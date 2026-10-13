package ua.kpi.comsys.dop;

import java.math.BigDecimal;

public sealed interface Transaction permits Transaction.Deposit, Transaction.Withdrawal, Transaction.Transfer {

    record Deposit(String accountId, BigDecimal amount) implements Transaction {
        public Deposit {
            requireText(accountId);
            requirePositive(amount);
        }
    }

    record Withdrawal(String accountId, BigDecimal amount, String category) implements Transaction {
        public Withdrawal {
            requireText(accountId);
            requirePositive(amount);
            requireText(category);
        }
    }

    record Transfer(String fromAccount, String toAccount, BigDecimal amount) implements Transaction {
        public Transfer {
            requireText(fromAccount);
            requireText(toAccount);
            requirePositive(amount);
        }
    }

    private static void requireText(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Text fields cannot be null or blank");
        }
    }

    private static void requirePositive(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
    }
}