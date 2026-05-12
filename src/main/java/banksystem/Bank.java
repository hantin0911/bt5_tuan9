package banksystem;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lớp đại diện cho hệ thống ngân hàng quản lý danh sách khách hàng.
 */
public class Bank {
  private static final Logger logger = LoggerFactory.getLogger(Bank.class);
  private List<Customer> customerList;

  public Bank() {
    this.customerList = new ArrayList<>();
  }

  public List<Customer> getCustomerList() {
    return customerList;
  }

  /**
   * Thiết lập danh sách khách hàng.
   *
   * @param customerList danh sách khách hàng mới
   */
  public void setCustomerList(List<Customer> customerList) {
    if (customerList == null) {
      this.customerList = new ArrayList<>();
    } else {
      this.customerList = customerList;
    }
  }

  /**
   * Đọc danh sách khách hàng từ InputStream.
   *
   * @param inputStream luồng dữ liệu đầu vào
   */
  public void readCustomerList(InputStream inputStream) {
    logger.debug("Bắt đầu đọc dữ liệu khách hàng...");
    if (inputStream != null) {
      try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
        String line;
        Customer current = null;
        while ((line = reader.readLine()) != null) {
          line = line.trim();
          if (line.isEmpty()) {
            continue;
          }

          int last = line.lastIndexOf(' ');
          if (last <= 0) {
            continue;
          }

          String token = line.substring(last + 1).trim();
          // Kiểm tra xem token có phải là số CMND (9 chữ số) hay không
          if (token.matches("\\d{9}")) {
            String name = line.substring(0, last).trim();
            current = new Customer(Long.parseLong(token), name);
            customerList.add(current);
            logger.info("Thêm khách hàng: {}", name);
          } else if (current != null) {
            String[] parts = line.split("\\s+");
            if (parts.length >= 3) {
              long num = Long.parseLong(parts[0]);
              double bal = Double.parseDouble(parts[2]);
              String type = parts[1];
              if (Account.CHECKING_TYPE.equals(type)) {
                current.addAccount(new CheckingAccount(num, bal));
              } else if (Account.SAVINGS_TYPE.equals(type)) {
                current.addAccount(new SavingsAccount(num, bal));
              }
            }
          }
        }
      } catch (Exception e) {
        logger.error("Lỗi khi đọc danh sách khách hàng: {}", e.getMessage());
      }
    }
  }

  /**
   * Trả về thông tin khách hàng được sắp xếp theo số CMND.
   *
   * @return chuỗi thông tin khách hàng
   */
  public String getCustomersInfoByIdOrder() {
    Collections.sort(customerList, (o1, o2) -> Long.compare(o1.getIdNumber(), o2.getIdNumber()));

    StringBuilder res = new StringBuilder();
    for (int i = 0; i < customerList.size(); i++) {
      res.append(customerList.get(i).getCustomerInfo());
      if (i < customerList.size() - 1) {
        res.append("\n");
      }
    }
    return res.toString();
  }

  /**
   * Trả về thông tin khách hàng được sắp xếp theo tên.
   *
   * @return chuỗi thông tin khách hàng
   */
  public String getCustomersInfoByNameOrder() {
    List<Customer> copy = new ArrayList<>(customerList);
    Collections.sort(copy, (c1, c2) -> {
      int res = c1.getFullName().compareTo(c2.getFullName());
      return res != 0 ? res : Long.compare(c1.getIdNumber(), c2.getIdNumber());
    });

    StringBuilder sb = new StringBuilder();
    for (Customer c : copy) {
      sb.append(c.getCustomerInfo()).append("\n");
    }
    return sb.toString().trim();
  }
}
