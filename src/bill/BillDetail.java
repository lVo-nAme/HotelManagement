package bill;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import payment.IPayment;
import payment.Payment;

public class Bill {
    public static final String UNPAID = "Chưa thanh toán";
    public static final String PAID = "Đã thanh toán";

    public static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    private static int idCounter = 0;

    private String billId;
    private String bookingId;
    private String staffId;
    private String staffName;
    private LocalDate issueDate;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private IPayment paymentMethod;
    private double roomAmount;
    private double serviceAmount;
    private double discountAmount;
    private double totalAmount;
    private String status;

    private Promotion promotion;
    private String promotionId;

    private BillDetail[] details;
    private int detailCount;
    private Payment[] payments;
    private int paymentCount;

    public Bill(String billId, String bookingId, String staffId, String staffName,
                LocalDate issueDate, LocalDate checkInDate, LocalDate checkOutDate) {
        setBillId(billId);
        setBookingId(bookingId);
        if (staffId == null || staffId.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã người lập không được để trống");
        }
        this.staffId = staffId.trim();
        this.staffName = (staffName == null) ? "" : staffName.trim();
        validateDates(issueDate, checkInDate, checkOutDate);
        this.issueDate = issueDate;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.status = UNPAID;
        this.promotionId = "";
        this.details = new BillDetail[4];
        this.detailCount = 0;
        this.payments = new Payment[2];
        this.paymentCount = 0;
    }

    private static void validateDates(LocalDate issue, LocalDate checkIn, LocalDate checkOut) {
        if (issue == null || checkIn == null || checkOut == null) {
            throw new IllegalArgumentException("Các ngày lập / nhận phòng / trả phòng không được null");
        }
        if (checkOut.isBefore(checkIn)) {
            throw new IllegalArgumentException("Ngày trả phòng không được trước ngày nhận phòng");
        }
    }

    public static String generateBillId() {
        idCounter++;
        return String.format("HD%04d", idCounter);
    }

    public static void updateCounter(String existingId) {
        if (existingId == null || !existingId.startsWith("HD")) {
            return;
        }
        try {
            int number = Integer.parseInt(existingId.substring(2));
            if (number > idCounter) {
                idCounter = number;
            }
        } catch (NumberFormatException e) {
        }
    }
    public static LocalDate parseDate(String s) {
        if (s == null || s.trim().isEmpty()) {
            throw new IllegalArgumentException("Ngày không được để trống");
        }
        try {
            return LocalDate.parse(s.trim(), DATE_FORMAT);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Ngày không hợp lệ (cần dd/MM/yyyy): " + s);
        }
    }

    public static String formatDate(LocalDate date) {
        return date == null ? "" : date.format(DATE_FORMAT);
    }

    public static String numberToString(double v) {
        return BigDecimal.valueOf(v).stripTrailingZeros().toPlainString();
    }

    public static String cleanField(String s) {
        if (s == null) {
            return "";
        }
        return s.replace('|', '/').replace('\n', ' ').replace('\r', ' ').trim();
    }

    public double calculateTotal() {
        roomAmount = 0;
        serviceAmount = 0;
        for (int i = 0; i < detailCount; i++) {
            BillDetail detail = details[i];
            if (BillDetail.TYPE_ROOM.equals(detail.getDetailType())) {
                roomAmount += detail.calculateTotal();
            } else {
                serviceAmount += detail.calculateTotal();
            }
        }
        double beforeDiscount = roomAmount + serviceAmount;

        if (promotion != null && !PAID.equals(status)) {
            discountAmount = promotion.calculateDiscount(beforeDiscount);
        }
        if (discountAmount > beforeDiscount) {
            discountAmount = beforeDiscount;
        }
        totalAmount = beforeDiscount - discountAmount;
        return totalAmount;
    }

    public void applyPromotion(Promotion promo) {
        ensureUnpaid();
        if (promo == null) {
            throw new IllegalArgumentException("Khuyến mãi không được null");
        }
        if (!promo.checkValidity(issueDate)) {
            throw new IllegalArgumentException("Khuyến mãi " + promo.getPromotionId()
                    + " không còn hiệu lực vào ngày lập hóa đơn");
        }
        calculateTotal();
        double beforeDiscount = roomAmount + serviceAmount;
        if (beforeDiscount < promo.getMinOrderValue()) {
            throw new IllegalArgumentException(String.format(
                    "Hóa đơn chưa đạt điều kiện tối thiểu %,.0f VND của khuyến mãi %s",
                    promo.getMinOrderValue(), promo.getPromotionId()));
        }
        this.promotion = promo;
        this.promotionId = promo.getPromotionId();
        calculateTotal();
    }

    public void removePromotion() {
        ensureUnpaid();
        promotion = null;
        promotionId = "";
        discountAmount = 0;
        calculateTotal();
    }

    public void addDetail(BillDetail detail) {
        ensureUnpaid();
        appendDetail(detail);
        calculateTotal();
    }

    public boolean removeDetail(String detailId) {
        ensureUnpaid();
        int index = indexOfDetail(detailId);
        if (index < 0) {
            return false;
        }
        for (int i = index; i < detailCount - 1; i++) {
            details[i] = details[i + 1];
        }
        details[--detailCount] = null;
        calculateTotal();
        return true;
    }

    public void updateDetail(String detailId, int newQuantity, double newUnitPrice) {
        ensureUnpaid();
        BillDetail detail = findDetail(detailId);
        if (detail == null) {
            throw new IllegalArgumentException("Không có chi tiết " + detailId + " trong hóa đơn");
        }
        detail.setQuantity(newQuantity);
        detail.setUnitPrice(newUnitPrice);
        calculateTotal();
    }

    public BillDetail findDetail(String detailId) {
        int index = indexOfDetail(detailId);
        return index < 0 ? null : details[index];
    }

    public String generateDetailId() {
        int k = detailCount + 1;
        while (findDetail("CT" + k) != null) {
            k++;
        }
        return "CT" + k;
    }

    public boolean pay(IPayment method) {
        if (method == null) {
            throw new IllegalArgumentException("Phương thức thanh toán không được null");
        }
        ensureUnpaid();
        if (detailCount == 0) {
            throw new IllegalStateException("Hóa đơn phải có ít nhất một chi tiết trước khi thanh toán");
        }
        calculateTotal();
        if (totalAmount <= 0) {
            status = PAID;
            return true;
        }
        String paymentId = "PAY-" + billId + "-" + (paymentCount + 1);
        Payment payment = new Payment(paymentId, billId, totalAmount, method);
        boolean result = payment.executePayment();
        appendPayment(payment);
        if (result) {
            paymentMethod = method;
            status = PAID;
        }
        return result;
    }

    public void displayBill() {
        calculateTotal();
        System.out.println("=========== HÓA ĐƠN " + billId + " ===========");
        System.out.println("Mã đặt phòng : " + bookingId);
        System.out.println("Người lập    : " + staffName + " (" + staffId + ")");
        System.out.println("Ngày lập     : " + formatDate(issueDate));
        System.out.println("Lưu trú      : " + formatDate(checkInDate) + " -> " + formatDate(checkOutDate));
        System.out.println("--- Chi tiết ---");
        for (int i = 0; i < detailCount; i++) {
            details[i].displayDetail();
        }
        System.out.println("----------------");
        System.out.printf("Tiền phòng   : %,.0f VND%n", roomAmount);
        System.out.printf("Tiền dịch vụ : %,.0f VND%n", serviceAmount);
        System.out.printf("Giảm giá     : -%,.0f VND%s%n", discountAmount,
                promotionId.isEmpty() ? "" : " (" + promotionId + ")");
        System.out.printf("TỔNG TIỀN    : %,.0f VND%n", totalAmount);
        System.out.println("Trạng thái   : " + getStatusText());
        if (paymentCount > 0) {
            System.out.println("--- Giao dịch ---");
            for (int i = 0; i < paymentCount; i++) {
                payments[i].displayInfo();
            }
        }
        System.out.println("==========================================");
    }

    public String getStatusText() {
        return PAID.equals(status) ? "Đã thanh toán" : "Chưa thanh toán";
    }

    private int indexOfDetail(String detailId) {
        if (detailId == null) {
            return -1;
        }
        for (int i = 0; i < detailCount; i++) {
            if (details[i].getDetailId().equalsIgnoreCase(detailId.trim())) {
                return i;
            }
        }
        return -1;
    }

    private void appendDetail(BillDetail detail) {
        if (detail == null) {
            throw new IllegalArgumentException("Chi tiết hóa đơn không được null");
        }
        if (findDetail(detail.getDetailId()) != null) {
            throw new IllegalArgumentException("Mã chi tiết " + detail.getDetailId() + " đã tồn tại trong hóa đơn");
        }
        if (detailCount == details.length) {
            BillDetail[] bigger = new BillDetail[details.length * 2];
            for (int i = 0; i < detailCount; i++) {
                bigger[i] = details[i];
            }
            details = bigger;
        }
        details[detailCount++] = detail;
    }

    private void appendPayment(Payment payment) {
        if (paymentCount == payments.length) {
            Payment[] bigger = new Payment[payments.length * 2];
            for (int i = 0; i < paymentCount; i++) {
                bigger[i] = payments[i];
            }
            payments = bigger;
        }
        payments[paymentCount++] = payment;
    }

    private void ensureUnpaid() {
        if (PAID.equals(status)) {
            throw new IllegalStateException("Hóa đơn " + billId + " đã thanh toán, không thể thay đổi");
        }
    }

    public String toFileLine() {
        calculateTotal();
        return "BILL|" + cleanField(billId) + "|" + cleanField(bookingId) + "|"
                + cleanField(staffId) + "|" + cleanField(staffName) + "|"
                + formatDate(issueDate) + "|" + formatDate(checkInDate) + "|" + formatDate(checkOutDate) + "|"
                + cleanField(promotionId) + "|" + numberToString(discountAmount) + "|" + status;
    }
    public static Bill fromFileLines(String[] lines, int start, int end) {
        String[] p = lines[start].trim().split("\\|", -1);
        if (p.length < 11 || !p[0].equals("BILL")) {
            throw new IllegalArgumentException("Dòng hóa đơn sai định dạng: " + lines[start]);
        }
        if (!UNPAID.equals(p[10]) && !PAID.equals(p[10])) {
            throw new IllegalArgumentException("Trạng thái hóa đơn không hợp lệ: " + p[10]);
        }
        Bill bill;
        try {
            bill = new Bill(p[1], p[2], p[3], p[4], parseDate(p[5]), parseDate(p[6]), parseDate(p[7]));
            bill.promotionId = p[8];
            bill.discountAmount = Double.parseDouble(p[9]);
            bill.status = p[10];
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Số không hợp lệ trong dòng: " + lines[start]);
        }
        for (int i = start + 1; i < end; i++) {
            String line = lines[i].trim();
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }
            try {
                if (line.startsWith("DETAIL|")) {
                    bill.appendDetail(BillDetail.fromFileLine(line));
                    bill.calculateTotal();
                } else if (line.startsWith("PAYMENT|")) {
                    bill.appendPayment(Payment.fromFileLine(line, bill.billId));
                } else {
                    System.out.println(" Bỏ qua dòng không nhận ra trong hóa đơn " + bill.billId + ": " + line);
                }
            } catch (IllegalArgumentException e) {
                System.out.println(" Bỏ qua dòng lỗi trong hóa đơn " + bill.billId + ": " + e.getMessage());
            }
        }
        bill.calculateTotal();
        return bill;
    }

    public void linkPromotion(Promotion promo) {
        if (promo != null && promo.getPromotionId().equalsIgnoreCase(promotionId)) {
            this.promotion = promo;
            calculateTotal();
        }
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

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        if (bookingId == null || bookingId.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã đặt phòng không được để trống");
        }
        this.bookingId = bookingId.trim();
    }

    public String getStaffId() {
        return staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        ensureUnpaid();
        validateDates(issueDate, checkInDate, checkOutDate);
        this.issueDate = issueDate;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setStayDates(LocalDate checkIn, LocalDate checkOut) {
        ensureUnpaid();
        validateDates(issueDate, checkIn, checkOut);
        this.checkInDate = checkIn;
        this.checkOutDate = checkOut;
    }

    public IPayment getPaymentMethod() {
        return paymentMethod;
    }

    public String getPaymentMethodName() {
        for (int i = paymentCount - 1; i >= 0; i--) {
            if (Payment.SUCCESS.equals(payments[i].getStatus())) {
                return payments[i].getMethodName();
            }
        }
        return "";
    }

    public double getRoomAmount() {
        calculateTotal();
        return roomAmount;
    }

    public double getServiceAmount() {
        calculateTotal();
        return serviceAmount;
    }

    public double getDiscountAmount() {
        calculateTotal();
        return discountAmount;
    }

    public double getTotalAmount() {
        return calculateTotal();
    }

    public String getStatus() {
        return status;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public String getPromotionId() {
        return promotionId;
    }

    public int getDetailCount() {
        return detailCount;
    }

    public BillDetail getDetail(int i) {
        if (i < 0 || i >= detailCount) {
            throw new IndexOutOfBoundsException("Chỉ số chi tiết không hợp lệ: " + i);
        }
        return details[i];
    }

    public int getPaymentCount() {
        return paymentCount;
    }

    public Payment getPayment(int i) {
        if (i < 0 || i >= paymentCount) {
            throw new IndexOutOfBoundsException("Chỉ số thanh toán không hợp lệ: " + i);
        }
        return payments[i];
    }
}
