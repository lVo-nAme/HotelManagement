package management;

import bill.Bill;
import bill.BillDetail;
import bill.PaymentTiming;
import bill.Promotion;
import fileio.FileHandler;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.Locale;
import payment.IPayment;
import util.InputHelper;

public class BillList {
    private static final int DEFAULT_CAPACITY = 10;
    private static final String BILL_FILE = "bill.txt";
    private static final String PROMOTION_FILE = "promotion.txt";
    private Bill[] bills;
    private Promotion[] promotions;
    private int billCount;
    private int promotionCount;

    public BillList() {
        this(DEFAULT_CAPACITY);
    }
    public BillList(int capacity) {
        int size = (capacity > 0) ? capacity : DEFAULT_CAPACITY;
        this.bills = new Bill[size];
        this.promotions = new Promotion[size];
        this.billCount = 0;
        this.promotionCount = 0;
    }
    public int getBillCount() {
        return billCount;
    }
    public Bill getBill(int index) {
        if (index < 0 || index >= billCount) {
            throw new IndexOutOfBoundsException("Chỉ số hóa đơn không hợp lệ: " + index);
        }
        return bills[index];
    }
    public int getPromotionCount() {
        return promotionCount;
    }
    public Promotion getPromotion(int index) {
        if (index < 0 || index >= promotionCount) {
            throw new IndexOutOfBoundsException("Chỉ số khuyến mãi không hợp lệ: " + index);
        }
        return promotions[index];
    }
    public void inputBills(int n, String staffId, String staffName) {
        bills = new Bill[Math.max(n, DEFAULT_CAPACITY)];
        billCount = 0;
        addBills(n, staffId, staffName);
    }

    public void displayBills() {
        printBillTable(bills, billCount, "DANH SÁCH HÓA ĐƠN");
    }
    public static void displayBills(Bill[] result) {
        printBillTable(result, result.length, "KẾT QUẢ");
    }
    public boolean displayBillDetail(String billId) {
        Bill bill = findBillById(billId);
        if (bill == null) {
            System.out.println(" Lỗi: Không tìm thấy hóa đơn " + billId);
            return false;
        }
        bill.displayBill();
        return true;
    }
    public boolean addBill(Bill bill) {
        if (bill == null || indexOfBill(bill.getBillId()) >= 0) {
            return false;
        }
        if (billCount == bills.length) {
            Bill[] bigger = new Bill[bills.length * 2];
            for (int i = 0; i < billCount; i++) {
                bigger[i] = bills[i];
            }
            bills = bigger;
        }
        bills[billCount++] = bill;
        Bill.updateCounter(bill.getBillId());
        return true;
    }

    public int addBills(int k, String staffId, String staffName) {
        int added = 0;
        for (int i = 1; i <= k; i++) {
            System.out.println("\n=== Nhập hóa đơn thứ " + i + "/" + k + " ===");
            if (addBill(inputBill(staffId, staffName))) {
                added++;
            } else {
                System.out.println("Lỗi: Mã hóa đơn bị trùng.");
            }
        }
        return added;
    }
    public int addBills(Bill[] newBills) {
        int added = 0;
        if (newBills == null) {
            return 0;
        }
        for (int i = 0; i < newBills.length; i++) {
            if (addBill(newBills[i])) {
                added++;
            }
        }
        return added;
    }

    public boolean updateBill(String billId) {
        Bill bill = findBillById(billId);
        if (bill == null) {
            System.out.println("Lỗi: Không tìm thấy hóa đơn " + billId);
            return false;
        }
        if (Bill.PAID.equals(bill.getStatus())) {
            System.out.println("Lỗi: Hóa đơn " + bill.getBillId() + " đã thanh toán, không được sửa.");
            return false;
        }
        int choice;
        do {
            System.out.println("\n--- Sửa hóa đơn " + bill.getBillId() + " ---");
            System.out.println("1. Đổi mã đặt phòng");
            System.out.println("2. Đổi ngày lập");
            System.out.println("3. Đổi ngày nhận / trả phòng");
            System.out.println("4. Thêm một chi tiết");
            System.out.println("5. Sửa số lượng / đơn giá của một chi tiết");
            System.out.println("6. Xoá một chi tiết");
            System.out.println("7. Bỏ khuyến mãi");
            System.out.println("0. Xong");
            choice = InputHelper.getIntInRange("Chọn: ", 0, 7);
            try {
                switch (choice) {
                    case 1: {
                        bill.setBookingId(InputHelper.getString("Mã đặt phòng mới: "));
                        break;
                    }
                    case 2: {
                        bill.setIssueDate(readDate("Ngày lập mới (dd/MM/yyyy): "));
                        break;
                    }
                    case 3: {
                        LocalDate checkIn = readDate("Ngày nhận phòng mới (dd/MM/yyyy): ");
                        LocalDate checkOut = readDate("Ngày trả phòng mới (dd/MM/yyyy): ");
                        bill.setStayDates(checkIn, checkOut);
                        break;
                    }
                    case 4: {
                        bill.addDetail(inputBillDetail(bill.generateDetailId()));
                        break;
                    }
                    case 5: {
                        String detailId = InputHelper.getString("Mã chi tiết cần sửa: ");
                        int quantity = InputHelper.getIntInRange("Số lượng mới: ", 1, 100000);
                        double unitPrice = readDouble("Đơn giá mới (VND): ", 0, Double.MAX_VALUE);
                        bill.updateDetail(detailId, quantity, unitPrice);
                        break;
                    }
                    case 6: {
                        String detailId = InputHelper.getString("Mã chi tiết cần xoá: ");
                        if (!bill.removeDetail(detailId)) {
                            System.out.println(" Lỗi: Không có chi tiết " + detailId);
                        }
                        break;
                    }
                    case 7: {
                        bill.removePromotion();
                        break;
                    }
                    default:
                        break;
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Lỗi: " + e.getMessage());
            }
        } while (choice != 0);
        return true;
    }
    public boolean removeBill(String billId) {
        int index = indexOfBill(billId);
        if (index < 0) {
            System.out.println(" Lỗi: Không tìm thấy hóa đơn " + billId);
            return false;
        }
        if (Bill.PAID.equals(bills[index].getStatus())) {
            System.out.println("️ Lỗi: Hóa đơn " + bills[index].getBillId() + " đã thanh toán, không được xoá.");
            return false;
        }
        for (int i = index; i < billCount - 1; i++) {
            bills[i] = bills[i + 1];
        }
        bills[--billCount] = null;
        return true;
    }
    public Bill findBillById(String billId) {
        int index = indexOfBill(billId);
        return index < 0 ? null : bills[index];
    }

    public Bill findBillByBookingId(String bookingId) {
        if (bookingId == null) {
            return null;
        }
        for (int i = 0; i < billCount; i++) {
            if (bills[i].getBookingId().equalsIgnoreCase(bookingId.trim())) {
                return bills[i];
            }
        }
        return null;
    }
    public Bill[] searchBills(String keyword) {
        return searchBills(keyword, null, null, null, -1, -1);
    }

    public Bill[] searchBills(String keyword, String status, LocalDate fromDate, LocalDate toDate,
                              double minTotal, double maxTotal) {
        boolean hasKeyword = !isBlank(keyword);
        Bill[] temp = new Bill[billCount];
        int[] scores = new int[billCount];
        int matched = 0;
        for (int i = 0; i < billCount; i++) {
            Bill bill = bills[i];
            if (!isBlank(status) && !bill.getStatus().equalsIgnoreCase(status.trim())) {
                continue;
            }
            if (fromDate != null && bill.getIssueDate().isBefore(fromDate)) {
                continue;
            }
            if (toDate != null && bill.getIssueDate().isAfter(toDate)) {
                continue;
            }
            double total = bill.getTotalAmount();
            if (minTotal >= 0 && total < minTotal) {
                continue;
            }
            if (maxTotal >= 0 && total > maxTotal) {
                continue;
            }
            int score = 1;
            if (hasKeyword) {
                score = billMatchScore(bill, keyword);
                if (score == 0) {
                    continue;
                }
            }
            temp[matched] = bill;
            scores[matched] = score;
            matched++;
        }
        for (int i = 1; i < matched; i++) {
            Bill current = temp[i];
            int currentScore = scores[i];
            int j = i - 1;
            while (j >= 0 && scores[j] < currentScore) {
                temp[j + 1] = temp[j];
                scores[j + 1] = scores[j];
                j--;
            }
            temp[j + 1] = current;
            scores[j + 1] = currentScore;
        }
        Bill[] result = new Bill[matched];
        for (int i = 0; i < matched; i++) {
            result[i] = temp[i];
        }
        return result;
    }

    public double getTotalRevenue() {
        return getTotalRevenue(null, null);
    }

    public double getTotalRevenue(LocalDate fromDate, LocalDate toDate) {
        double sum = 0;
        for (int i = 0; i < billCount; i++) {
            Bill bill = bills[i];
            if (!Bill.PAID.equals(bill.getStatus())) {
                continue;
            }
            if (fromDate != null && bill.getIssueDate().isBefore(fromDate)) {
                continue;
            }
            if (toDate != null && bill.getIssueDate().isAfter(toDate)) {
                continue;
            }
            sum += bill.getTotalAmount();
        }
        return sum;
    }
    public double[] statisticsByMonth(int year) {
        double[] revenue = new double[12];
        int[] counts = new int[12];
        for (int i = 0; i < billCount; i++) {
            Bill bill = bills[i];
            if (Bill.PAID.equals(bill.getStatus()) && bill.getIssueDate().getYear() == year) {
                int month = bill.getIssueDate().getMonthValue() - 1;
                revenue[month] += bill.getTotalAmount();
                counts[month]++;
            }
        }
        System.out.println("--- Doanh thu theo tháng, năm " + year + " ---");
        double sum = 0;
        for (int m = 0; m < 12; m++) {
            System.out.printf("Tháng %2d : %3d hóa đơn %,18.0f VND%n", m + 1, counts[m], revenue[m]);
            sum += revenue[m];
        }
        System.out.printf("Cả năm   : %,29.0f VND%n", sum);
        return revenue;
    }
    public StatTable statisticsByPaymentMethod() {
        StatTable table = new StatTable();
        for (int i = 0; i < billCount; i++) {
            if (Bill.PAID.equals(bills[i].getStatus())) {
                table.add(bills[i].getPaymentMethodName(), bills[i].getTotalAmount());
            }
        }
        table.sortByTotalDesc();
        table.print("Doanh thu theo phương thức thanh toán", "Phương thức", "Số HĐ", "Doanh thu");
        return table;
    }

    public StatTable statisticsByStaff() {
        StatTable table = new StatTable();
        for (int i = 0; i < billCount; i++) {
            table.add(bills[i].getStaffId() + " - " + bills[i].getStaffName(), bills[i].getTotalAmount());
        }
        table.sortByTotalDesc();
        table.print("Hóa đơn theo nhân viên lập", "Nhân viên", "Số HĐ", "Tổng tiền");
        return table;
    }

    public StatTable statisticsByStatus() {
        StatTable table = new StatTable();
        table.addGroup("Đã thanh toán");
        table.addGroup("Chưa thanh toán");
        for (int i = 0; i < billCount; i++) {
            table.add(bills[i].getStatusText(), bills[i].getTotalAmount());
        }
        table.print("Hóa đơn theo trạng thái", "Trạng thái", "Số HĐ", "Tổng tiền");
        return table;
    }

    public StatTable statisticsRoomVsService() {
        StatTable table = new StatTable();
        table.addGroup("Tiền phòng");
        table.addGroup("Dịch vụ");
        for (int i = 0; i < billCount; i++) {
            if (Bill.PAID.equals(bills[i].getStatus())) {
                table.add("Tiền phòng", bills[i].getRoomAmount());
                table.add("Dịch vụ", bills[i].getServiceAmount());
            }
        }
        table.print("Tiền phòng và dịch vụ (trước giảm giá)", "Loại thu", "Số HĐ", "Số tiền");
        return table;
    }
    public boolean applyPromotion(String billId, String promotionId) {
        Bill bill = findBillById(billId);
        if (bill == null) {
            System.out.println("Lỗi: Không tìm thấy hóa đơn " + billId);
            return false;
        }
        Promotion promotion = findPromotionById(promotionId);
        if (promotion == null) {
            System.out.println("Lỗi: Không tìm thấy khuyến mãi " + promotionId);
            return false;
        }
        try {
            bill.applyPromotion(promotion);
            return true;
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Lỗi: Không áp dụng được khuyến mãi: " + e.getMessage());
            return false;
        }
    }

    public boolean applyBestPromotion(String billId) {
        Bill bill = findBillById(billId);
        if (bill == null) {
            System.out.println(" Lỗi: Không tìm thấy hóa đơn " + billId);
            return false;
        }
        Promotion best = suggestPromotion(bill.getRoomAmount() + bill.getServiceAmount(), bill.getIssueDate());
        if (best == null) {
            System.out.println(" Lỗi: Không có khuyến mãi nào phù hợp với hóa đơn " + billId);
            return false;
        }
        return applyPromotion(billId, best.getPromotionId());
    }
    public boolean payBill(String billId, IPayment method) {
        Bill bill = findBillById(billId);
        if (bill == null) {
            System.out.println(" Lỗi: Không tìm thấy hóa đơn " + billId);
            return false;
        }
        try {
            return bill.pay(method);
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println(" Lỗi: Không thanh toán được: " + e.getMessage());
            return false;
        }
    }

    public Bill[] getUnpaidBills() {
        return searchBills(null, Bill.UNPAID, null, null, -1, -1);
    }

    public void sortBillsByTotal(boolean ascending) {
        for (int i = 0; i < billCount - 1; i++) {
            int chosen = i;
            for (int j = i + 1; j < billCount; j++) {
                boolean better = ascending ? bills[j].getTotalAmount() < bills[chosen].getTotalAmount()
                        : bills[j].getTotalAmount() > bills[chosen].getTotalAmount();
                if (better) {
                    chosen = j;
                }
            }
            swapBills(i, chosen);
        }
    }
    public void sortBillsByIssueDate() {
        for (int i = 0; i < billCount - 1; i++) {
            int chosen = i;
            for (int j = i + 1; j < billCount; j++) {
                if (bills[j].getIssueDate().isBefore(bills[chosen].getIssueDate())) {
                    chosen = j;
                }
            }
            swapBills(i, chosen);
        }
    }

    public void inputPromotions(int n) {
        promotions = new Promotion[Math.max(n, DEFAULT_CAPACITY)];
        promotionCount = 0;
        addPromotions(n);
    }

    public void displayPromotions() {
        printPromotionTable(promotions, promotionCount, "DANH SÁCH KHUYẾN MÃI");
    }
    public static void displayPromotions(Promotion[] result) {
        printPromotionTable(result, result.length, "KẾT QUẢ");
    }
    public boolean addPromotion(Promotion promotion) {
        if (promotion == null || indexOfPromotion(promotion.getPromotionId()) >= 0) {
            return false;
        }
        if (promotionCount == promotions.length) {
            Promotion[] bigger = new Promotion[promotions.length * 2];
            for (int i = 0; i < promotionCount; i++) {
                bigger[i] = promotions[i];
            }
            promotions = bigger;
        }
        promotions[promotionCount++] = promotion;
        return true;
    }

    public int addPromotions(int k) {
        int added = 0;
        for (int i = 1; i <= k; i++) {
            System.out.println("\n=== Nhập khuyến mãi thứ " + i + "/" + k + " ===");
            if (addPromotion(inputPromotion())) {
                added++;
            } else {
                System.out.println("Lỗi: Mã khuyến mãi bị trùng.");
            }
        }
        return added;
    }
    public boolean updatePromotion(String promotionId) {
        Promotion promotion = findPromotionById(promotionId);
        if (promotion == null) {
            System.out.println("️ Lỗi: Không tìm thấy khuyến mãi " + promotionId);
            return false;
        }
        int choice;
        do {
            System.out.println("\n--- Sửa khuyến mãi " + promotion.getPromotionId() + " ---");
            System.out.println("1. Đổi tên");
            System.out.println("2. Đổi phần trăm giảm");
            System.out.println("3. Đổi điều kiện tối thiểu");
            System.out.println("4. Đổi mức giảm tối đa");
            System.out.println("5. Đổi ngày bắt đầu");
            System.out.println("6. Đổi ngày kết thúc");
            System.out.println("0. Xong");
            choice = InputHelper.getIntInRange("Chọn: ", 0, 6);
            try {
                switch (choice) {
                    case 1: {
                        promotion.setPromotionName(InputHelper.getString("Tên mới: "));
                        break;
                    }
                    case 2: {
                        promotion.setDiscountPercent(readDouble("Phần trăm giảm mới (0-100): ", 0, 100));
                        break;
                    }
                    case 3: {
                        promotion.setMinOrderValue(readDouble("Điều kiện tối thiểu mới (VND): ", 0, Double.MAX_VALUE));
                        break;
                    }
                    case 4: {
                        promotion.setMaxDiscount(readDouble("Mức giảm tối đa mới (VND): ", 0, Double.MAX_VALUE));
                        break;
                    }
                    case 5: {
                        promotion.setStartDate(Bill.formatDate(readDate("Ngày bắt đầu mới (dd/MM/yyyy): ")));
                        break;
                    }
                    case 6: {
                        promotion.setEndDate(Bill.formatDate(readDate("Ngày kết thúc mới (dd/MM/yyyy): ")));
                        break;
                    }
                    default:
                        break;
                }
            } catch (IllegalArgumentException e) {
                System.out.println(" Lỗi: " + e.getMessage());
            }
        } while (choice != 0);
        return true;
    }
    public boolean removePromotion(String promotionId) {
        int index = indexOfPromotion(promotionId);
        if (index < 0) {
            System.out.println(" Lỗi: Không tìm thấy khuyến mãi " + promotionId);
            return false;
        }
        String id = promotions[index].getPromotionId();
        for (int i = 0; i < billCount; i++) {
            if (bills[i].getPromotionId().equalsIgnoreCase(id)) {
                System.out.println("️ Lỗi: Khuyến mãi " + id + " đã được dùng ở hóa đơn "
                        + bills[i].getBillId() + ", không được xoá.");
                return false;
            }
        }
        for (int i = index; i < promotionCount - 1; i++) {
            promotions[i] = promotions[i + 1];
        }
        promotions[--promotionCount] = null;
        return true;
    }
    public Promotion findPromotionById(String promotionId) {
        int index = indexOfPromotion(promotionId);
        return index < 0 ? null : promotions[index];
    }

    public Promotion[] searchPromotions(String keyword) {
        return searchPromotions(keyword, null, null, -1, -1);
    }
    public Promotion[] searchPromotions(String keyword, String status, LocalDate date,
                                        double minPercent, double maxPercent) {
        boolean hasKeyword = !isBlank(keyword);
        LocalDate checkDate = (date != null) ? date : LocalDate.now();
        Promotion[] temp = new Promotion[promotionCount];
        int[] scores = new int[promotionCount];
        int matched = 0;
        for (int i = 0; i < promotionCount; i++) {
            Promotion promotion = promotions[i];
            if (!isBlank(status) && !promotion.getStatus(checkDate).equalsIgnoreCase(status.trim())) {
                continue;
            }
            if (minPercent >= 0 && promotion.getDiscountPercent() < minPercent) {
                continue;
            }
            if (maxPercent >= 0 && promotion.getDiscountPercent() > maxPercent) {
                continue;
            }
            int score = 1;
            if (hasKeyword) {
                score = Math.max(matchScore(promotion.getPromotionId(), keyword, true),
                        matchScore(promotion.getPromotionName(), keyword, true));
                if (score == 0) {
                    continue;
                }
            }
            temp[matched] = promotion;
            scores[matched] = score;
            matched++;
        }
        for (int i = 1; i < matched; i++) {
            Promotion current = temp[i];
            int currentScore = scores[i];
            int j = i - 1;
            while (j >= 0 && scores[j] < currentScore) {
                temp[j + 1] = temp[j];
                scores[j + 1] = scores[j];
                j--;
            }
            temp[j + 1] = current;
            scores[j + 1] = currentScore;
        }
        Promotion[] result = new Promotion[matched];
        for (int i = 0; i < matched; i++) {
            result[i] = temp[i];
        }
        return result;
    }

    public StatTable statisticsPromotionUsage() {
        StatTable table = new StatTable();
        for (int i = 0; i < promotionCount; i++) {
            table.addGroup(promotions[i].getPromotionId());
        }
        for (int i = 0; i < billCount; i++) {
            if (!bills[i].getPromotionId().isEmpty()) {
                table.add(bills[i].getPromotionId(), bills[i].getDiscountAmount());
            }
        }
        table.sortByTotalDesc();
        table.print("Mức độ sử dụng khuyến mãi", "Mã khuyến mãi", "Số HĐ", "Tổng tiền giảm");
        return table;
    }

    public StatTable statisticsPromotionByStatus(LocalDate date) {
        StatTable table = new StatTable();
        table.addGroup("Còn hiệu lực");
        table.addGroup("Chưa bắt đầu");
        table.addGroup("Hết hạn");
        for (int i = 0; i < promotionCount; i++) {
            table.add(promotions[i].getStatusText(date), promotions[i].getMaxDiscount());
        }
        table.print("Khuyến mãi theo trạng thái tại " + Bill.formatDate(date), "Trạng thái", "Số KM", "Tổng mức giảm tối đa");
        return table;
    }

    public Promotion[] getActivePromotions(LocalDate date) {
        return searchPromotions(null, Promotion.ACTIVE, date, -1, -1);
    }
    public Promotion suggestPromotion(double total, LocalDate date) {
        Promotion best = null;
        double bestDiscount = 0;
        for (int i = 0; i < promotionCount; i++) {
            Promotion promotion = promotions[i];
            if (promotion.checkValidity(date)) {
                double discount = promotion.calculateDiscount(total);
                if (discount > bestDiscount) {
                    bestDiscount = discount;
                    best = promotion;
                }
            }
        }
        return best;
    }

    public void saveData() {
        savePromotions(PROMOTION_FILE);
        saveBills(BILL_FILE);
    }

    public void loadData() {
        loadPromotions(PROMOTION_FILE);
        loadBills(BILL_FILE);
    }

    public void savePromotions(String fileName) {
        String[] lines = new String[promotionCount + 1];
        lines[0] = "# promotionId|promotionName|discountPercent|minOrderValue|maxDiscount|startDate|endDate";
        for (int i = 0; i < promotionCount; i++) {
            lines[i + 1] = promotions[i].toFileLine();
        }
        FileHandler.writeLinesToFile(fileName, lines, false);
    }

    public void loadPromotions(String fileName) {
        String[] lines = FileHandler.readLinesFromFile(fileName);
        if (lines.length == 0) {
            return;
        }
        promotions = new Promotion[DEFAULT_CAPACITY];
        promotionCount = 0;
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }
            try {
                if (!addPromotion(Promotion.fromFileLine(line))) {
                    System.out.println(" Dòng " + (i + 1) + ": trùng mã khuyến mãi, bỏ qua.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println(" Dòng " + (i + 1) + " lỗi: " + e.getMessage());
            }
        }
        linkPromotionsToBills();
    }
    public void saveBills(String fileName) {
        int total = 1;
        for (int i = 0; i < billCount; i++) {
            total += 1 + bills[i].getDetailCount() + bills[i].getPaymentCount();
        }
        String[] lines = new String[total];
        int k = 0;
        lines[k++] = "# BILL = hóa đơn, DETAIL = chi tiết, PAYMENT = thanh toán. Các dòng DETAIL/PAYMENT thuộc hóa đơn BILL đứng ngay trước chúng.";
        for (int i = 0; i < billCount; i++) {
            Bill bill = bills[i];
            lines[k++] = bill.toFileLine();
            for (int j = 0; j < bill.getDetailCount(); j++) {
                lines[k++] = bill.getDetail(j).toFileLine();
            }
            for (int j = 0; j < bill.getPaymentCount(); j++) {
                lines[k++] = bill.getPayment(j).toFileLine();
            }
        }
        FileHandler.writeLinesToFile(fileName, lines, false);
    }

    public void loadBills(String fileName) {
        String[] lines = FileHandler.readLinesFromFile(fileName);
        if (lines.length == 0) {
            return;
        }
        bills = new Bill[DEFAULT_CAPACITY];
        billCount = 0;
        int i = 0;
        while (i < lines.length) {
            String line = lines[i].trim();
            if (line.isEmpty() || line.startsWith("#")) {
                i++;
                continue;
            }
            if (!line.startsWith("BILL|")) {
                System.out.println(" Bỏ qua dòng " + (i + 1) + " (không thuộc hóa đơn nào): " + line);
                i++;
                continue;
            }
            int end = i + 1;
            while (end < lines.length && !lines[end].trim().startsWith("BILL|")) {
                end++;
            }
            try {
                Bill bill = Bill.fromFileLines(lines, i, end);
                if (!addBill(bill)) {
                    System.out.println(" Dòng " + (i + 1) + ": trùng mã hóa đơn " + bill.getBillId() + ", bỏ qua.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println(" Dòng " + (i + 1) + " lỗi: " + e.getMessage());
            }
            i = end;
        }
        linkPromotionsToBills();
    }

    private void linkPromotionsToBills() {
        for (int i = 0; i < billCount; i++) {
            String id = bills[i].getPromotionId();
            if (!id.isEmpty() && bills[i].getPromotion() == null) {
                bills[i].linkPromotion(findPromotionById(id));
            }
        }
    }
    private static Bill inputBill(String staffId, String staffName) {
        String billId = Bill.generateBillId();
        System.out.println("Mã hóa đơn (tự sinh): " + billId);
        String bookingId = InputHelper.getString("Mã đặt phòng: ");
        LocalDate issueDate = readDateOrToday("Ngày lập (dd/MM/yyyy, gõ 0 để lấy hôm nay): ");
        LocalDate checkIn = readDate("Ngày nhận phòng (dd/MM/yyyy): ");
        LocalDate checkOut;
        while (true) {
            checkOut = readDate("Ngày trả phòng (dd/MM/yyyy): ");
            if (!checkOut.isBefore(checkIn)) {
                break;
            }
            System.out.println(" Lỗi: Ngày trả phòng không được trước ngày nhận phòng.");
        }
        Bill bill = new Bill(billId, bookingId, staffId, staffName, issueDate, checkIn, checkOut);
        do {
            System.out.println("--- Chi tiết " + bill.generateDetailId() + " ---");
            bill.addDetail(inputBillDetail(bill.generateDetailId()));
        } while (readYesNo("Thêm chi tiết khác? (c/k): "));
        return bill;
    }

    private static BillDetail inputBillDetail(String detailId) {
        int type = InputHelper.getIntInRange("Loại (1 = Tiền phòng, 2 = Dịch vụ): ", 1, 2);
        String detailType = (type == 1) ? BillDetail.TYPE_ROOM : BillDetail.TYPE_SERVICE;
        String serviceId = null;
        String serviceName = null;
        if (type == 2) {
            serviceId = InputHelper.getString("Mã dịch vụ: ");
            serviceName = InputHelper.getString("Tên dịch vụ: ");
        }
        String description = readOptional("Mô tả");
        int timing = InputHelper.getIntInRange("Thời điểm thanh toán (1 = ngay khi sử dụng, 2 = khi trả phòng): ", 1, 2);
        PaymentTiming paymentTiming = (timing == 1) ? PaymentTiming.ON_USE : PaymentTiming.ON_CHECKOUT;
        int quantity = InputHelper.getIntInRange(type == 1 ? "Số đêm: " : "Số lượng: ", 1, 100000);
        double unitPrice = readDouble("Đơn giá (VND): ", 0, Double.MAX_VALUE);
        double vatRate = readDouble("Thuế VAT (%, ví dụ 10): ", 0, 100);
        return new BillDetail(detailId, detailType, serviceId, serviceName, description,
                paymentTiming, quantity, unitPrice, vatRate);
    }

    private static Promotion inputPromotion() {
        String id = InputHelper.getString("Mã khuyến mãi: ");
        String name = InputHelper.getString("Tên khuyến mãi: ");
        double percent = readDouble("Phần trăm giảm (0-100): ", 0, 100);
        double minOrder = readDouble("Tổng tiền tối thiểu để áp dụng (VND): ", 0, Double.MAX_VALUE);
        double maxDiscount = readDouble("Số tiền giảm tối đa (VND): ", 0, Double.MAX_VALUE);
        LocalDate start = readDate("Ngày bắt đầu (dd/MM/yyyy): ");
        LocalDate end;
        while (true) {
            end = readDate("Ngày kết thúc (dd/MM/yyyy): ");
            if (!end.isBefore(start)) {
                break;
            }
            System.out.println(" Lỗi: Ngày kết thúc không được trước ngày bắt đầu.");
        }
        return new Promotion(id, name, percent, minOrder, maxDiscount,
                Bill.formatDate(start), Bill.formatDate(end));
    }

    private static LocalDate readDate(String prompt) {
        while (true) {
            String s = InputHelper.getString(prompt);
            try {
                return Bill.parseDate(s);
            } catch (IllegalArgumentException e) {
                System.out.println(" Lỗi: " + e.getMessage());
            }
        }
    }
    private static LocalDate readDateOrToday(String prompt) {
        while (true) {
            String s = InputHelper.getString(prompt);
            if (s.equals("0")) {
                return LocalDate.now();
            }
            try {
                return Bill.parseDate(s);
            } catch (IllegalArgumentException e) {
                System.out.println("Lỗi: " + e.getMessage());
            }
        }
    }

    private static double readDouble(String prompt, double min, double max) {
        while (true) {
            double value = InputHelper.getDouble(prompt);
            if (value >= min && value <= max) {
                return value;
            }
            if (max == Double.MAX_VALUE) {
                System.out.println("Lỗi: Giá trị phải từ " + min + " trở lên!");
            } else {
                System.out.println("Lỗi: Giá trị phải nằm trong khoảng từ " + min + " đến " + max + "!");
            }
        }
    }

    private static boolean readYesNo(String prompt) {
        while (true) {
            String s = normalize(InputHelper.getString(prompt));
            if (s.equals("c") || s.equals("co") || s.equals("y") || s.equals("yes")) {
                return true;
            }
            if (s.equals("k") || s.equals("khong") || s.equals("n") || s.equals("no")) {
                return false;
            }
            System.out.println("️ Lỗi: Chỉ nhập c (có) hoặc k (không)!");
        }
    }

    private static String readOptional(String label) {
        String s = InputHelper.getString(label + " (nhập - nếu không có): ");
        return s.equals("-") ? "" : s;
    }

    private int indexOfBill(String billId) {
        if (billId == null) {
            return -1;
        }
        for (int i = 0; i < billCount; i++) {
            if (bills[i].getBillId().equalsIgnoreCase(billId.trim())) {
                return i;
            }
        }
        return -1;
    }

    private int indexOfPromotion(String promotionId) {
        if (promotionId == null) {
            return -1;
        }
        for (int i = 0; i < promotionCount; i++) {
            if (promotions[i].getPromotionId().equalsIgnoreCase(promotionId.trim())) {
                return i;
            }
        }
        return -1;
    }

    private void swapBills(int i, int j) {
        if (i != j) {
            Bill temp = bills[i];
            bills[i] = bills[j];
            bills[j] = temp;
        }
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static int billMatchScore(Bill bill, String keyword) {
        String[] fields = {
                bill.getBillId(), bill.getBookingId(), bill.getStaffId(), bill.getStaffName(),
                bill.getStatusText(), bill.getPaymentMethodName(), bill.getPromotionId()
        };
        int best = 0;
        for (int i = 0; i < fields.length; i++) {
            best = Math.max(best, matchScore(fields[i], keyword, true));
        }
        return Math.max(best, matchScore(Bill.formatDate(bill.getIssueDate()), keyword, false));
    }
    private static String removeAccents(String s) {
        if (s == null) {
            return "";
        }
        String t = Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        return t.replace('đ', 'd').replace('Đ', 'D');
    }

    private static String normalize(String s) {
        return removeAccents(s).toLowerCase(Locale.ROOT).replace('_', ' ').trim().replaceAll("\\s+", " ");
    }

    private static int editDistance(String a, String b) {
        int[] previous = new int[b.length() + 1];
        int[] current = new int[b.length() + 1];
        for (int j = 0; j <= b.length(); j++) {
            previous[j] = j;
        }
        for (int i = 1; i <= a.length(); i++) {
            current[0] = i;
            for (int j = 1; j <= b.length(); j++) {
                int cost = (a.charAt(i - 1) == b.charAt(j - 1)) ? 0 : 1;
                current[j] = Math.min(Math.min(current[j - 1] + 1, previous[j] + 1), previous[j - 1] + cost);
            }
            int[] swap = previous;
            previous = current;
            current = swap;
        }
        return previous[b.length()];
    }

    private static int matchScore(String field, String keyword, boolean allowFuzzy) {
        String f = normalize(field);
        String k = normalize(keyword);
        if (f.isEmpty() || k.isEmpty()) {
            return 0;
        }
        if (f.equals(k)) {
            return 100;
        }
        if (f.startsWith(k)) {
            return 80;
        }
        if (f.contains(k)) {
            return 60;
        }

        int threshold = (k.length() <= 3) ? 0 : (k.length() <= 6 ? 1 : 2);
        if (!allowFuzzy || threshold == 0) {
            return 0;
        }
        if (editDistance(f, k) <= threshold) {
            return 40;
        }
        String[] words = f.split(" ");
        for (int i = 0; i < words.length; i++) {
            if (editDistance(words[i], k) <= threshold) {
                return 40;
            }
        }
        return 0;
    }

    private static void printBillTable(Bill[] array, int count, String title) {
        System.out.println("===== " + title + " (" + count + ") =====");
        if (count == 0) {
            System.out.println("(trống)");
            return;
        }
        System.out.printf("%-4s %-9s %-12s %-11s %-16s %15s  %s%n",
                "STT", "Mã HĐ", "Mã đặt phòng", "Ngày lập", "Người lập", "Tổng tiền", "Trạng thái");
        for (int i = 0; i < count; i++) {
            Bill bill = array[i];
            System.out.printf("%-4d %-9s %-12s %-11s %-16.16s %,15.0f  %s%n", i + 1, bill.getBillId(),
                    bill.getBookingId(), Bill.formatDate(bill.getIssueDate()), bill.getStaffName(),
                    bill.getTotalAmount(), bill.getStatusText());
        }
    }

    private static void printPromotionTable(Promotion[] array, int count, String title) {
        System.out.println("===== " + title + " (" + count + ") =====");
        if (count == 0) {
            System.out.println("(trống)");
            return;
        }
        System.out.printf("%-4s %-10s %-22s %6s %14s %14s  %-11s %-11s%n",
                "STT", "Mã KM", "Tên", "% giảm", "Từ (VND)", "Tối đa (VND)", "Bắt đầu", "Kết thúc");
        for (int i = 0; i < count; i++) {
            Promotion p = array[i];
            System.out.printf("%-4d %-10s %-22.22s %6.1f %,14.0f %,14.0f  %-11s %-11s%n", i + 1,
                    p.getPromotionId(), p.getPromotionName(), p.getDiscountPercent(), p.getMinOrderValue(),
                    p.getMaxDiscount(), p.getStartDate(), p.getEndDate());
        }
    }
    public static class StatTable {
        private String[] keys = new String[8];
        private int[] counts = new int[8];
        private double[] totals = new double[8];
        private int size = 0;

        public StatTable() {
            this.size = 0;
        }

        public void addGroup(String key) {
            findOrCreate(key);
        }

        public void add(String key, double value) {
            int i = findOrCreate(key);
            counts[i]++;
            totals[i] += value;
        }

        private int findOrCreate(String key) {
            if (key == null || key.trim().isEmpty()) {
                key = "(không có)";
            }
            for (int i = 0; i < size; i++) {
                if (keys[i].equals(key)) {
                    return i;
                }
            }
            if (size == keys.length) {
                String[] newKeys = new String[keys.length * 2];
                int[] newCounts = new int[keys.length * 2];
                double[] newTotals = new double[keys.length * 2];
                for (int i = 0; i < size; i++) {
                    newKeys[i] = keys[i];
                    newCounts[i] = counts[i];
                    newTotals[i] = totals[i];
                }
                keys = newKeys;
                counts = newCounts;
                totals = newTotals;
            }
            keys[size] = key;
            counts[size] = 0;
            totals[size] = 0;
            return size++;
        }

        public void sortByTotalDesc() {
            for (int i = 0; i < size - 1; i++) {
                int max = i;
                for (int j = i + 1; j < size; j++) {
                    if (totals[j] > totals[max]) {
                        max = j;
                    }
                }
                if (max != i) {
                    String k = keys[i];
                    keys[i] = keys[max];
                    keys[max] = k;
                    int c = counts[i];
                    counts[i] = counts[max];
                    counts[max] = c;
                    double t = totals[i];
                    totals[i] = totals[max];
                    totals[max] = t;
                }
            }
        }
        public int size() {
            return size;
        }

        public String getKey(int i) {
            return keys[i];
        }

        public int getCount(int i) {
            return counts[i];
        }

        public double getTotal(int i) {
            return totals[i];
        }

        public double grandTotal() {
            double sum = 0;
            for (int i = 0; i < size; i++) {
                sum += totals[i];
            }
            return sum;
        }

        public void print(String title, String keyHeader, String countHeader, String totalHeader) {
            System.out.println("--- " + title + " ---");
            if (size == 0) {
                System.out.println("(không có dữ liệu)");
                return;
            }
            System.out.printf("%-26s %10s %18s%n", keyHeader, countHeader, totalHeader);
            int totalCount = 0;
            for (int i = 0; i < size; i++) {
                System.out.printf("%-26.26s %10d %,18.0f%n", keys[i], counts[i], totals[i]);
                totalCount += counts[i];
            }
            System.out.printf("%-26s %10d %,18.0f%n", "TỔNG", totalCount, grandTotal());
        }
    }
}
