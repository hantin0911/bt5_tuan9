package banksystem;

import java.util.Locale;

/**
 * Ngoại lệ khi số dư tài khoản không đủ để thực hiện giao dịch.
 */
public class InsufficientFundsException extends BankException {
  /**
   * Khởi tạo ngoại lệ với số tiền gây ra lỗi.
   *
   * @param amount số tiền yêu cầu rút
   */
  public InsufficientFundsException(double amount) {
    super(String.format(Locale.US,
        "Số dư tài khoản không đủ $%.2f để thực hiện giao dịch", amount));
  }
}
