package payment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Payment {
    public static final String PENDING = "Đang xử lý";
    public static final String SUCCESS = "Giao dịch thành công";
    public static final String FAILED = "Giao dịch thất bại";

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/uuuu");

    private String paymentId;
    private String billId;
    private double amount;
    private String paymentDate;
    private String status;
    private IPayment method;
    private String methodName;

    public Payment(String paymentId, String billId, double amount, IPayment method) {
        setPaymentId(paymentId);
        setBillId(billId);
        setAmount(amount);
        setMethod(method);
        this.status = PENDING;
    }

    public Payment(String paymentId, String billId, double amount,
                   String paymentDate, String status, String methodName) {
        setPaymentId(paymentId);
        setBillId(billId);
        setAmount(amount);
        if (!PENDING.equals(status) && !SUCCESS.equals(status) && !FAILED.equals(status)) {
            throw new IllegalArgumentException("Trạng thái thanh toán không hợp lệ: " + status);
        }
        this.status = status;
        if (paymentDate == null || paymentDate.trim().isEmpty()) {
            this.paymentDate = null;
        } else {
            this.paymentDate = paymentDate.trim();
        }
        this.methodName = (methodName == null) ? "" : methodName;
    }

    public boolean executePayment() {
        if (SUCCESS.equals(status) || method == null) {
            return false; // đã thành công rồi, hoặc giao dịch đọc từ file nên không thể chạy lại
        }
        boolean result = method.pay(amount);
        if (result) {
            paymentDate = LocalDate.now().format(DATE_FORMAT);
            updateStatus();
        } else {
            status = FAILED;
        }
        return result;
    }

    public void updateStatus() {
        if (paymentDate != null) {
            status = SUCCESS;
        } else if (!FAILED.equals(status)) {
            status = PENDING;
        }
    }

    public void displayInfo() {
        System.out.printf("  [%s] Hóa đơn %s | %,.0f VND | %s | Ngày: %s | Phương thức: %s%n",
                paymentId, billId, amount, status,
                paymentDate == null ? "(chưa thanh toán)" : paymentDate, methodName);
    }

    public String toFileLine() {
        return "PAYMENT|" + clean(paymentId) + "|" + numberToString(amount) + "|"
                + (paymentDate == null ? "" : paymentDate) + "|" + status + "|" + clean(methodName);
    }

    public static Payment fromFileLine(String line, String billId) {
        String[] p = line.split("\\|", -1);
        if (p.length < 6 || !p[0].equals("PAYMENT")) {
            throw new IllegalArgumentException("Dòng thanh toán sai định dạng: " + line);
        }
        try {
            return new Payment(p[1], billId, Double.parseDouble(p[2]), p[3], p[4], p[5]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Số không hợp lệ trong dòng: " + line);
        }
    }

    private static String clean(String s) {
        if (s == null) {
            return "";
        }
        return s.replace('|', '/').replace('\n', ' ').replace('\r', ' ').trim();
    }

    private static String numberToString(double v) {
        return BigDecimal.valueOf(v).stripTrailingZeros().toPlainString();
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        if (paymentId == null || paymentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã thanh toán không được để trống");
        }
        this.paymentId = paymentId.trim();
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        if (billId == null || billId.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã hóa đơn không được để trống");
        }
        this.billId = billId.trim();
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Số tiền thanh toán phải lớn hơn 0");
        }
        this.amount = amount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public String getStatus() {
        return status;
    }

    public IPayment getMethod() {
        return method;
    }

    public void setMethod(IPayment method) {
        if (method == null) {
            throw new IllegalArgumentException("Phương thức thanh toán không được null");
        }
        this.method = method;
        this.methodName = method.getMethodName();
    }

    public String getMethodName() {
        return methodName;
    }
}
