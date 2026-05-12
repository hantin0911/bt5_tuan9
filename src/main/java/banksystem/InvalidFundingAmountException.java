package banksystem;

import java.util.Locale;

/**
 * Ngoại lệ khi số tiền giao dịch không hợp lệ.
 */
public class InvalidFundingAmountException extends BankException {
  /**
   * Khởi tạo ngoại lệ với số tiền không hợp lệ.
   *
   * @param amount số tiền gây lỗi
   */
  public InvalidFundingAmountException(double amount) {
    super(String.format(Locale.US, "Số tiền không hợp lệ: $%.2f", amount));
  }
}
