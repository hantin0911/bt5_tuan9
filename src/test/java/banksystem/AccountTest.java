package banksystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AccountTest {

    private CheckingAccount account;

    @BeforeEach
    void setUp() {
        account = new CheckingAccount(123456, 1000.0);
    }

    @Test
    void testInitialBalance() {
        assertEquals(1000.0, account.getBalance());
    }

    @Test
    void testDeposit() {
        account.deposit(500.0);
        assertEquals(1500.0, account.getBalance());
    }

    @Test
    void testWithdraw() {
        account.withdraw(400.0);
        assertEquals(600.0, account.getBalance());
    }

    @Test
    void testWithdrawInsufficientFunds() {
        account.withdraw(2000.0);
        // Balance should remain 1000.0 as it should catch the exception and log error
        assertEquals(1000.0, account.getBalance());
    }
}
