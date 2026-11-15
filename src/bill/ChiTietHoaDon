package bill;

public class ChiTietHoaDon {
    public static final String LOAI_TIEN_PHONG = "TIEN_PHONG";
    public static final String LOAI_DICH_VU = "DICH_VU";

    private String maChiTiet;
    private String loaiChiTiet;
    private String maDichVu; 
    private String tenDichVu; 
    private String moTa;
    private ThoiDiemThanhToan thoiDiemThanhToan;
    private int soLuong;
    private double donGia;
    private double thueSuatVAT;
    private double tienThue;  
    private double thanhTien;  

    public ChiTietHoaDon(String maChiTiet, String loaiChiTiet, String maDichVu, String tenDichVu,
                         String moTa, ThoiDiemThanhToan thoiDiemThanhToan,
                         int soLuong, double donGia, double thueSuatVAT) {
        setMaChiTiet(maChiTiet);
        setLoaiChiTiet(loaiChiTiet);
        this.maDichVu = maDichVu;
        this.tenDichVu = tenDichVu;
       
        if (LOAI_DICH_VU.equals(this.loaiChiTiet) && (maDichVu == null || maDichVu.trim().isEmpty())) {
            throw new IllegalArgumentException("Chi tiết loại DICH_VU phải có mã dịch vụ");
        }
        this.moTa = moTa;
        setThoiDiemThanhToan(thoiDiemThanhToan);
     
        kiemTraSoLuong(soLuong);
        kiemTraDonGia(donGia);
        kiemTraThue(thueSuatVAT);
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.thueSuatVAT = thueSuatVAT;
        tinhTongCong();
    }

    // Thành tiền chưa gồm thuế = số lượng x đơn giá
    public double tinhThanhTien() {
        thanhTien = soLuong * donGia;
        return thanhTien;
    }

    // Tiền thuế VAT trên thành tiền
    public double tinhThue() {
        tienThue = tinhThanhTien() * thueSuatVAT / 100;
        return tienThue;
    }

    // Tổng cộng = thành tiền + thuế
    public double tinhTongCong() {
        return tinhThanhTien() + tinhThue();
    }

    public void hienThiChiTiet() {
        String ten = LOAI_DICH_VU.equals(loaiChiTiet) && tenDichVu != null ? tenDichVu : "Tiền phòng";
        System.out.printf("  [%s] %s%s%n", maChiTiet, ten,
                (moTa == null || moTa.trim().isEmpty()) ? "" : " (" + moTa + ")");
        System.out.printf("      %d x %,.0f = %,.0f | VAT %.1f%% = %,.0f | Cộng: %,.0f VND | %s%n",
                soLuong, donGia, thanhTien, thueSuatVAT, tienThue, tinhTongCong(),
                thoiDiemThanhToan.getMoTa());
    }

    public String toCSV() {
        return "ChiTiet," + HoaDon.lamSachTruong(maChiTiet) + "," + loaiChiTiet + ","
                + HoaDon.lamSachTruong(maDichVu) + "," + HoaDon.lamSachTruong(tenDichVu) + ","
                + HoaDon.lamSachTruong(moTa) + "," + thoiDiemThanhToan.name() + ","
                + soLuong + "," + HoaDon.soThanhChuoi(donGia) + "," + HoaDon.soThanhChuoi(thueSuatVAT);
    }

    public static ChiTietHoaDon tuCSV(String dong) {
        String[] p = dong.split(",", -1);
        if (p.length < 10 || !p[0].equals("ChiTiet")) {
            throw new IllegalArgumentException("Dòng chi tiết sai định dạng: " + dong);
        }
        try {
            return new ChiTietHoaDon(p[1], p[2], p[3].isEmpty() ? null : p[3], p[4].isEmpty() ? null : p[4], p[5],
                    ThoiDiemThanhToan.valueOf(p[6]), Integer.parseInt(p[7]),
                    Double.parseDouble(p[8]), Double.parseDouble(p[9]));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Số không hợp lệ trong dòng: " + dong);
        }
    }

    private static void kiemTraSoLuong(int soLuong) {
        if (soLuong <= 0) {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
        }
    }

    private static void kiemTraDonGia(double donGia) {
        if (donGia < 0) {
            throw new IllegalArgumentException("Đơn giá không được âm");
        }
    }

    private static void kiemTraThue(double thueSuatVAT) {
        if (thueSuatVAT < 0 || thueSuatVAT > 100) {
            throw new IllegalArgumentException("Thuế suất VAT phải từ 0 đến 100 (%)");
        }
    }

    public String getMaChiTiet() {
        return maChiTiet;
    }

    public void setMaChiTiet(String maChiTiet) {
        if (maChiTiet == null || maChiTiet.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã chi tiết không được để trống");
        }
        this.maChiTiet = maChiTiet.trim();
    }

    public String getLoaiChiTiet() {
        return loaiChiTiet;
    }

    public void setLoaiChiTiet(String loaiChiTiet) {
        if (!LOAI_TIEN_PHONG.equals(loaiChiTiet) && !LOAI_DICH_VU.equals(loaiChiTiet)) {
            throw new IllegalArgumentException("Loại chi tiết chỉ được là TIEN_PHONG hoặc DICH_VU");
        }
        this.loaiChiTiet = loaiChiTiet;
    }

    public String getMaDichVu() {
        return maDichVu;
    }

    public void setMaDichVu(String maDichVu) {
        this.maDichVu = maDichVu;
    }

    public String getTenDichVu() {
        return tenDichVu;
    }

    public void setTenDichVu(String tenDichVu) {
        this.tenDichVu = tenDichVu;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public ThoiDiemThanhToan getThoiDiemThanhToan() {
        return thoiDiemThanhToan;
    }

    public void setThoiDiemThanhToan(ThoiDiemThanhToan thoiDiemThanhToan) {
        if (thoiDiemThanhToan == null) {
            throw new IllegalArgumentException("Thời điểm thanh toán không được null");
        }
        this.thoiDiemThanhToan = thoiDiemThanhToan;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        kiemTraSoLuong(soLuong);
        this.soLuong = soLuong;
        tinhTongCong();
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        kiemTraDonGia(donGia);
        this.donGia = donGia;
        tinhTongCong();
    }

    public double getThueSuatVAT() {
        return thueSuatVAT;
    }

    public void setThueSuatVAT(double thueSuatVAT) {
        kiemTraThue(thueSuatVAT);
        this.thueSuatVAT = thueSuatVAT;
        tinhTongCong();
    }

    public double getTienThue() {
        return tienThue;
    }

    public double getThanhTien() {
        return thanhTien;
    }
}
