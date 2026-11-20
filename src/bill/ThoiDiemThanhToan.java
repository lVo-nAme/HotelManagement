package bill;

public enum ThoiDiemThanhToan {
    NGAY_SU_DUNG("Thanh toán ngay khi sử dụng"),
    KHI_TRA_PHONG("Thanh toán khi trả phòng");

    private final String moTa;

    ThoiDiemThanhToan(String moTa) {
        this.moTa = moTa;
    }

    public String getMoTa() {
        return moTa;
    }
}
