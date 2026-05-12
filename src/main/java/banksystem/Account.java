package banksystem;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lớp trừu tượng đại diện cho một tài khoản ngân hàng.
 * Cung cấp các phương thức cơ bản như gửi tiền, rút tiền và quản lý giao dịch.
 */
public abstract class Account {
  private static final Logger logger = LoggerFactory.getLogger(Account.class);

  public static final String CHECKING_TYPE = "CHECKING";
  public static final String SAVINGS_TYPE = "SAVINGS";

  private long accountNumber;
  private double balance;
  protected List<Transaction> transactions;

  /**
   * Khởi tạo một tài khoản với số tài khoản và số dư ban đầu.
   *
   * @param accountNumber số tài khoản
   * @param balance       số dư ban đầu
   */
  public Account(long accountNumber, double balance) {
    this.accountNumber = accountNumber;
    this.balance = balance;
    this.transactions = new ArrayList<>();
  }

  public long getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(long accountNumber) {
    this.accountNumber = accountNumber;
  }

  public double getBalance() {
    return balance;
  }

  protected void setBalance(double balance) {
    this.balance = balance;
  }

  public List<Transaction> getTransactionList() {
    return transactions;
  }

  /**
   * Thiết lập danh sách giao dịch cho tài khoản.
   *
   * @param transactionList danh sách giao dịch mới
   */
  public void setTransactionList(List<Transaction> transactionList) {
    if (transactionList == null) {
      this.transactions = new ArrayList<>();
    } else {
      this.transactions = transactionList;
    }
  }

  /**
   * Thực hiện gửi tiền vào tài khoản.
   *
   * @param amount số tiền gửi
   */
  public abstract void deposit(double amount);

  /**
   * Thực hiện rút tiền từ tài khoản.
   *
   * @param amount số tiền rút
   */
  public abstract void withdraw(double amount);

  /**
   * Logic chung cho việc nạp tiền.
   *
   * @param amount số tiền nạp
   * @throws InvalidFundingAmountException nếu số tiền không hợp lệ
   */
  protected void doDepositing(double amount) throws InvalidFundingAmountException {
    if (amount <= 0) {
      throw new InvalidFundingAmountException(amount);
    }
    balance += amount;
  }

  /**
   * Logic chung cho việc rút tiền.
   *
   * @param amount số tiền rút
   * @throws Exception nếu có lỗi xảy ra (số dư không đủ hoặc số tiền không hợp lệ)
   */
  protected void doWithdrawing(double amount) throws Exception {
    if (amount <= 0) {
      throw new InvalidFundingAmountException(amount);
    }
    if (amount > balance) {
      throw new InsufficientFundsException(amount);
    }
    balance -= amount;
  }

  /**
   * Thêm một giao dịch vào lịch sử.
   *
   * @param transaction giao dịch cần thêm
   */
  public void addTransaction(Transaction transaction) {
    if (transaction != null) {
      transactions.add(transaction);
    }
  }

  /**
   * Trả về chuỗi tóm tắt lịch sử giao dịch.
   *
   * @return chuỗi lịch sử giao dịch
   */
  public String getTransactionHistory() {
    StringBuilder sb = new StringBuilder();
    sb.append("Lịch sử giao dịch của tài khoản ").append(accountNumber).append(":\n");
    for (int i = 0; i < transactions.size(); i++) {
      sb.append(transactions.get(i).getTransactionSummary());
      if (i < transactions.size() - 1) {
        sb.append("\n");
      }
    }
    logger.debug("Đã lấy lịch sử cho tài khoản: {}", accountNumber);
    return sb.toString();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Account)) {
      return false;
    }
    Account other = (Account) obj;
    return this.accountNumber == other.accountNumber;
  }

  @Override
  public int hashCode() {
    return Long.hashCode(accountNumber);
  }
}
