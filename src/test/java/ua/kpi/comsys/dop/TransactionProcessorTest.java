package ua.kpi.comsys.dop;

import ua.kpi.comsys.dop.Transaction.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class TransactionProcessorTest {

    private static final BigDecimal OK = BigDecimal.TEN;

    private final TransactionProcessor processor = new TransactionProcessor();

    private static void assertIae(Runnable action) {
        assertThrows(IllegalArgumentException.class, action::run);
    }

    // testing TransactionProcessor

    @Test
    void processDeposit() {
        String expected = """
                Transaction Type: DEPOSIT
                Account: ACC-1
                Amount: USD 100.50
                """;
        assertEquals(expected, processor.process(new Deposit("ACC-1", new BigDecimal("100.50"))));
    }

    @Test
    void processWithdrawal() {
        String expected = """
                Transaction Type: WITHDRAWAL
                Account: ACC-2
                Category: Food
                Amount: USD 25.00
                """;
        assertEquals(expected, processor.process(new Withdrawal("ACC-2", new BigDecimal("25.00"), "Food")));
    }

    @Test
    void processTransfer() {
        String expected = """
                Transaction Type: TRANSFER
                From: ACC-1
                To: ACC-2
                Amount: USD 300
                """;
        assertEquals(expected, processor.process(new Transfer("ACC-1", "ACC-2", new BigDecimal("300"))));
    }

    // Testing the exceptions
    @Test
    void processNullThrows() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> processor.process(null));
        assertEquals("Transaction cannot be null", ex.getMessage());
    }


    @Test
    void depositInvalidAccount() {
        assertIae(() -> new Deposit(null, OK));
        assertIae(() -> new Deposit("", OK));
        assertIae(() -> new Deposit("   ", OK));
    }

    @Test
    void depositInvalidAmount() {
        assertIae(() -> new Deposit("A", null));
        assertIae(() -> new Deposit("A", BigDecimal.ZERO));
        assertIae(() -> new Deposit("A", new BigDecimal("-1")));
    }


    @Test
    void withdrawalInvalidAccount() {
        assertIae(() -> new Withdrawal(null, OK, "Food"));
        assertIae(() -> new Withdrawal(" ", OK, "Food"));
    }

    @Test
    void withdrawalInvalidAmount() {
        assertIae(() -> new Withdrawal("A", null, "Food"));
        assertIae(() -> new Withdrawal("A", BigDecimal.ZERO, "Food"));
        assertIae(() -> new Withdrawal("A", new BigDecimal("-0.01"), "Food"));
    }

    @Test
    void withdrawalInvalidCategory() {
        assertIae(() -> new Withdrawal("A", OK, null));
        assertIae(() -> new Withdrawal("A", OK, ""));
        assertIae(() -> new Withdrawal("A", OK, "  "));
    }

    @Test
    void transferInvalidFrom() {
        assertIae(() -> new Transfer(null, "B", OK));
        assertIae(() -> new Transfer(" ", "B", OK));
    }

    @Test
    void transferInvalidTo() {
        assertIae(() -> new Transfer("A", null, OK));
        assertIae(() -> new Transfer("A", "", OK));
    }

    @Test
    void transferInvalidAmount() {
        assertIae(() -> new Transfer("A", "B", null));
        assertIae(() -> new Transfer("A", "B", BigDecimal.ZERO));
        assertIae(() -> new Transfer("A", "B", new BigDecimal("-5")));
    }
}