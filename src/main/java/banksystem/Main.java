package banksystem;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lớp chính để chạy demo hệ thống ngân hàng.
 */
public class Main {
  private static final Logger logger = LoggerFactory.getLogger(Main.class);

  /**
   * Phương thức main khởi chạy ứng dụng.
   *
   * @param args tham số dòng lệnh
   */
  public static void main(String[] args) {
    logger.info("Bai05");

    Bank bank = new Bank();

    // Giả lập dữ liệu đầu vào
    String data = "Nguyen Van A 123456789\n"
        + "10000001 CHECKING 500.0\n"
        + "10000002 SAVINGS 10000.0\n"
        + "Le Thi B 987654321\n"
        + "20000001 CHECKING 200.0\n";

    InputStream inputStream = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
    bank.readCustomerList(inputStream);

    logger.info("--- Danh sách khách hàng sắp xếp theo ID ---");
    System.out.println(bank.getCustomersInfoByIdOrder());

    // Thực hiện một số giao dịch demo
    if (!bank.getCustomerList().isEmpty()) {
      Customer firstCustomer = bank.getCustomerList().get(0);
      if (!firstCustomer.getAccountList().isEmpty()) {
        Account account = firstCustomer.getAccountList().get(0);
        account.deposit(150.0);
        account.withdraw(50.0);
        logger.info("Lịch sử giao dịch của {}: \n{}",
            firstCustomer.getFullName(), account.getTransactionHistory());
      }
    }

    logger.info("Kết thúc demo Bank System.");
  }
}