package bill;

public enum PaymentTiming {
    ON_USE("Thanh toán ngay khi sử dụng"),
    ON_CHECKOUT("Thanh toán khi trả phòng");

    private final String description;

    PaymentTiming(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
