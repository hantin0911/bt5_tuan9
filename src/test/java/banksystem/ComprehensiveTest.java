package banksystem;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;

public class ComprehensiveTest {

    @Test
    void testAccountSubclasses() {
        CheckingAccount checking = new CheckingAccount(101, 500.0);
        assertEquals(500.0, checking.getBalance());
        assertEquals(101, checking.getAccountNumber());
        
        checking.deposit(200.0);
        assertEquals(700.0, checking.getBalance());
        
        checking.withdraw(100.0);
        assertEquals(600.0, checking.getBalance());
        
        // Test withdraw more than balance (CheckingAccount should fail)
        checking.withdraw(1000.0); 
        assertEquals(600.0, checking.getBalance());

        // SavingsAccount rules: MIN_BALANCE = 5000.0, MAX_WITHDRAW = 1000.0
        SavingsAccount savings = new SavingsAccount(102, 6000.0);
        savings.deposit(500.0);
        assertEquals(6500.0, savings.getBalance());
        
        savings.withdraw(200.0);
        assertEquals(6300.0, savings.getBalance());
        
        // Test exceed MAX_WITHDRAW
        savings.withdraw(1500.0);
        assertEquals(6300.0, savings.getBalance());
        
        // Test go below MIN_BALANCE
        savings.withdraw(2000.0); // 6300 - 2000 = 4300 < 5000
        assertEquals(6300.0, savings.getBalance());
    }

    @Test
    void testAccountBaseMethods() {
        CheckingAccount a1 = new CheckingAccount(100, 100.0);
        CheckingAccount a2 = new CheckingAccount(100, 200.0);
        CheckingAccount a3 = new CheckingAccount(101, 100.0);
        
        assertTrue(a1.equals(a1));
        assertTrue(a1.equals(a2));
        assertFalse(a1.equals(a3));
        assertFalse(a1.equals("string"));
        assertFalse(a1.equals(null));
        
        assertEquals(a1.hashCode(), a2.hashCode());
        assertNotEquals(a1.hashCode(), a3.hashCode());
        
        a1.setAccountNumber(200);
        assertEquals(200, a1.getAccountNumber());
        
        a1.setTransactionList(null);
        assertEquals(0, a1.getTransactionList().size());
        
        a1.deposit(100);
        assertFalse(a1.getTransactionHistory().isEmpty());
    }

    @Test
    void testCustomer() {
        Customer customer = new Customer(123456789L, "John Doe");
        assertEquals(123456789L, customer.getIdNumber());
        assertEquals("John Doe", customer.getFullName());
        
        customer.setFullName("Jane Doe");
        assertEquals("Jane Doe", customer.getFullName());
        customer.setIdNumber(987654321L);
        assertEquals(987654321L, customer.getIdNumber());

        CheckingAccount acc = new CheckingAccount(101, 500.0);
        customer.addAccount(acc);
        
        List<Account> accounts = customer.getAccountList();
        assertEquals(1, accounts.size());
        
        customer.setAccountList(new ArrayList<>());
        assertEquals(0, customer.getAccountList().size());
        
        String info = customer.getCustomerInfo();
        assertTrue(info.contains("Jane Doe"));
    }

    @Test
    void testBank() {
        Bank bank = new Bank();
        bank.setCustomerList(null);
        assertEquals(0, bank.getCustomerList().size());
        
        String data = " \n" + // empty line
                      "InvalidLine\n" + // line without space
                      "John Doe 123456789\n" +
                      "101 CHECKING 500.0\n" +
                      "102 SAVINGS 6000.0\n" +
                      "Jane Smith 987654321\n" +
                      "103 UNKNOWN 1000.0\n"; // invalid type
        InputStream inputStream = new ByteArrayInputStream(data.getBytes());
        bank.readCustomerList(inputStream);
        bank.readCustomerList(null); // test null input
        
        assertEquals(2, bank.getCustomerList().size());
        
        String byId = bank.getCustomersInfoByIdOrder();
        assertTrue(byId.indexOf("123456789") < byId.indexOf("987654321"));
        
        String byName = bank.getCustomersInfoByNameOrder();
        assertTrue(byName.indexOf("Jane Smith") < byName.indexOf("John Doe"));
    }

    @Test
    void testExceptions() {
        assertNotNull(new BankException("Error"));
        InsufficientFundsException ife = new InsufficientFundsException(100.0);
        assertTrue(ife.getMessage().contains("100"));
        
        InvalidFundingAmountException ifae = new InvalidFundingAmountException(-50.0);
        assertTrue(ifae.getMessage().contains("-50"));
    }

    @Test
    void testTransaction() {
        Transaction t = new Transaction(Transaction.TYPE_DEPOSIT_CHECKING, 100.0, 500.0, 600.0);
        t.setType(Transaction.TYPE_DEPOSIT_SAVINGS);
        assertEquals(Transaction.TYPE_DEPOSIT_SAVINGS, t.getType());
        t.setAmount(200.0);
        assertEquals(200.0, t.getAmount());
        t.setInitialBalance(100.0);
        assertEquals(100.0, t.getInitialBalance());
        t.setFinalBalance(300.0);
        assertEquals(300.0, t.getFinalBalance());
        
        assertNotNull(t.getTransactionSummary());
        assertEquals("Nạp tiền vãng lai", Transaction.getTypeString(Transaction.TYPE_DEPOSIT_CHECKING));
        assertEquals("Rút tiền vãng lai", Transaction.getTypeString(Transaction.TYPE_WITHDRAW_CHECKING));
        assertEquals("Nạp tiền tiết kiệm", Transaction.getTypeString(Transaction.TYPE_DEPOSIT_SAVINGS));
        assertEquals("Rút tiền tiết kiệm", Transaction.getTypeString(Transaction.TYPE_WITHDRAW_SAVINGS));
        assertEquals("Không rõ", Transaction.getTypeString(99));
    }
}
