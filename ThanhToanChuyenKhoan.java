public class ThanhToanChuyenKhoan implements IThanhToan {
    private String nganHang;
    private String maGiaoDich;

    public ThanhToanChuyenKhoan(String nganHang, String maGiaoDich) {
        this.nganHang = nganHang;
        this.maGiaoDich = maGiaoDich;
    }

    @Override
    public boolean thanhToan(double soTien) {
        if (soTien <= 0 || nganHang == null || nganHang.trim().isEmpty()
                || maGiaoDich == null || maGiaoDich.trim().isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public String layPhuongThuc() {
        return "Thanh toan chuyen khoan";
    }

    @Override
    public void hienThiThongTin() {
        System.out.println("Phuong thuc: " + layPhuongThuc());
        System.out.println("Ngan hang: " + nganHang);
        System.out.println("Ma giao dich: " + maGiaoDich);
    }
}
