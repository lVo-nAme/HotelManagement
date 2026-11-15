package bill;

import java.time.LocalDate;

public class Promotion {
    public static final String ACTIVE = "Hiệu lực";
    public static final String UPCOMING = "Sắp bắt đầu";
    public static final String EXPIRED = "Hết hạn";

    private String promotionId;
    private String promotionName;
    private double discountPercent;
    private double minOrderValue;
    private double maxDiscount;
    private String startDate;
    private String endDate;

    public Promotion(String promotionId, String promotionName, double discountPercent,
                     double minOrderValue, double maxDiscount, String startDate, String endDate) {
        setPromotionId(promotionId);
        setPromotionName(promotionName);
        setDiscountPercent(discountPercent);
        setMinOrderValue(minOrderValue);
        setMaxDiscount(maxDiscount);
        LocalDate start = Bill.parseDate(startDate);
        LocalDate end = Bill.parseDate(endDate);
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("Ngày kết thúc không được trước ngày bắt đầu");
        }
        this.startDate = startDate.trim();
        this.endDate = endDate.trim();
    }

    public boolean checkValidity(String date) {
        return checkValidity(Bill.parseDate(date));
    }

    public boolean checkValidity(LocalDate date) {
        return ACTIVE.equals(getStatus(date));
    }

    public String getStatus(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Ngày kiểm tra không được null");
        }
        if (date.isBefore(Bill.parseDate(startDate))) {
            return UPCOMING;
        }
        if (date.isAfter(Bill.parseDate(endDate))) {
            return EXPIRED;
        }
        return ACTIVE;
    }

    public String getStatusText(LocalDate date) {
        String status = getStatus(date);
        if (UPCOMING.equals(status)) {
            return "Chưa bắt đầu";
        }
        if (EXPIRED.equals(status)) {
            return "Hết hạn";
        }
        return "Còn hiệu lực";
    }

    public double calculateDiscount(double total) {
        if (total <= 0 || total < minOrderValue) {
            return 0;
        }
        return Math.min(total * discountPercent / 100, maxDiscount);
    }

    public void display() {
        System.out.printf("[%s] %s | giảm %.0f%% (tối đa %,.0f) | đơn từ %,.0f | %s -> %s%n",
                promotionId, promotionName, discountPercent, maxDiscount, minOrderValue,
                startDate, endDate);
    }

    public String toFileLine() {
        return Bill.cleanField(promotionId) + "|" + Bill.cleanField(promotionName) + "|"
                + Bill.numberToString(discountPercent) + "|" + Bill.numberToString(minOrderValue) + "|"
                + Bill.numberToString(maxDiscount) + "|" + startDate + "|" + endDate;
    }

    public static Promotion fromFileLine(String line) {
        String[] p = line.split("\\|", -1);
        if (p.length < 7) {
            throw new IllegalArgumentException("Dòng khuyến mãi thiếu trường: " + line);
        }
        try {
            return new Promotion(p[0], p[1], Double.parseDouble(p[2]), Double.parseDouble(p[3]),
                    Double.parseDouble(p[4]), p[5], p[6]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Số không hợp lệ trong dòng: " + line);
        }
    }

    public String getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(String promotionId) {
        if (promotionId == null || promotionId.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã khuyến mãi không được để trống");
        }
        this.promotionId = promotionId.trim();
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        if (promotionName == null || promotionName.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên khuyến mãi không được để trống");
        }
        this.promotionName = promotionName.trim();
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(double discountPercent) {
        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException("Phần trăm giảm phải từ 0 đến 100");
        }
        this.discountPercent = discountPercent;
    }

    public double getMinOrderValue() {
        return minOrderValue;
    }

    public void setMinOrderValue(double minOrderValue) {
        if (minOrderValue < 0) {
            throw new IllegalArgumentException("Điều kiện tối thiểu không được âm");
        }
        this.minOrderValue = minOrderValue;
    }

    public double getMaxDiscount() {
        return maxDiscount;
    }

    public void setMaxDiscount(double maxDiscount) {
        if (maxDiscount < 0) {
            throw new IllegalArgumentException("Tiền giảm tối đa không được âm");
        }
        this.maxDiscount = maxDiscount;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        LocalDate start = Bill.parseDate(startDate);
        if (start.isAfter(Bill.parseDate(this.endDate))) {
            throw new IllegalArgumentException("Ngày bắt đầu không được sau ngày kết thúc");
        }
        this.startDate = startDate.trim();
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        LocalDate end = Bill.parseDate(endDate);
        if (end.isBefore(Bill.parseDate(this.startDate))) {
            throw new IllegalArgumentException("Ngày kết thúc không được trước ngày bắt đầu");
        }
        this.endDate = endDate.trim();
    }

    @Override
    public String toString() {
        return String.format("%s - %s (giảm %.0f%%, tối đa %,.0f VND, đơn từ %,.0f VND, %s -> %s)",
                promotionId, promotionName, discountPercent, maxDiscount, minOrderValue,
                startDate, endDate);
    }
}
