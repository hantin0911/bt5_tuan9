package banksystem;

/**
 * Ngoại lệ chung trong hệ thống ngân hàng.
 */
public class BankException extends Exception {
  /**
   * Khởi tạo một ngoại lệ ngân hàng với thông điệp lỗi.
   *
   * @param message thông điệp lỗi
   */
  public BankException(String message) {
    super(message);
  }
}
