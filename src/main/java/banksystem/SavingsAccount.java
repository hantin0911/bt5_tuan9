package banksystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lớp đại diện cho tài khoản tiết kiệm với các quy định riêng về rút tiền và nạp tiền.
 */
public class SavingsAccount extends Account {
  private static final Logger logger = LoggerFactory.getLogger(SavingsAccount.class);

  public static final double MAX_WITHDRAW = 1000.0;
  public static final double MIN_BALANCE = 5000.0;

  public SavingsAccount(long accountNumber, double balance) {
    super(accountNumber, balance);
  }

  @Override
  public void deposit(double amount) {
    logger.debug("Giao dịch nạp tiền tiết kiệm đang xử lý...");
    double initialBalance = getBalance();
    try {
      doDepositing(amount);
      double finalBalance = getBalance();
      Transaction t = new Transaction(
          Transaction.TYPE_DEPOSIT_SAVINGS,
          amount,
          initialBalance,
          finalBalance);
      addTransaction(t);
      logger.info("Nạp tiền vào tài khoản tiết kiệm {} thành công: +{}",
          getAccountNumber(), amount);
    } catch (Exception e) {
      logger.error("Lỗi nạp tiền vào tài khoản tiết kiệm {}: {}",
          getAccountNumber(), e.getMessage());
    }
  }

  @Override
  public void withdraw(double amount) {
    double initialBalance = getBalance();
    try {
      if (amount > MAX_WITHDRAW) {
        throw new InvalidFundingAmountException(amount);
      }
      if (initialBalance - amount < MIN_BALANCE) {
        throw new InsufficientFundsException(amount);
      }

      doWithdrawing(amount);
      double finalBalance = getBalance();

      Transaction t = new Transaction(
          Transaction.TYPE_WITHDRAW_SAVINGS,
          amount,
          initialBalance,
          finalBalance);
      addTransaction(t);
      logger.info("Rút tiền từ tài khoản tiết kiệm {} thành công: -{}. Số dư còn: {}",
          getAccountNumber(), amount, finalBalance);
    } catch (Exception e) {
      logger.error("Rút tiền từ tài khoản tiết kiệm {} bị lỗi: {}",
          getAccountNumber(), e.getMessage());
    }
  }
}
