package bill;

import java.time.LocalDate;

public class KhuyenMai {
  
    public static final String CON_HIEU_LUC = "CON_HIEU_LUC";
    public static final String CHUA_BAT_DAU = "CHUA_BAT_DAU";
    public static final String HET_HAN = "HET_HAN";

    private String maKhuyenMai;
    private String tenKhuyenMai;
    private double phanTramGiam;
    private double dieuKienToiThieu;  
    private double tienGiamToiDa; 
    private String ngayBatDau;
    private String ngayKetThuc;

    public KhuyenMai(String maKhuyenMai, String tenKhuyenMai, double phanTramGiam,
                     double dieuKienToiThieu, double tienGiamToiDa,
                     String ngayBatDau, String ngayKetThuc) {
        setMaKhuyenMai(maKhuyenMai);
        setTenKhuyenMai(tenKhuyenMai);
        setPhanTramGiam(phanTramGiam);
        setDieuKienToiThieu(dieuKienToiThieu);
        setTienGiamToiDa(tienGiamToiDa);

        LocalDate batDau = HoaDon.docNgay(ngayBatDau);
        LocalDate ketThuc = HoaDon.docNgay(ngayKetThuc);
        if (ketThuc.isBefore(batDau)) {
            throw new IllegalArgumentException("Ngày kết thúc không được trước ngày bắt đầu");
        }
        this.ngayBatDau = ngayBatDau.trim();
        this.ngayKetThuc = ngayKetThuc.trim();
    }

    public boolean kiemTraHieuLuc(String ngay) {
        return kiemTraHieuLuc(HoaDon.docNgay(ngay));
    }

    public boolean kiemTraHieuLuc(LocalDate ngay) {
        return CON_HIEU_LUC.equals(layTrangThai(ngay));
    }

    public String layTrangThai(LocalDate ngay) {
        if (ngay == null) {
            throw new IllegalArgumentException("Ngày kiểm tra không được null");
        }
        if (ngay.isBefore(HoaDon.docNgay(ngayBatDau))) {
            return CHUA_BAT_DAU;
        }
        if (ngay.isAfter(HoaDon.docNgay(ngayKetThuc))) {
            return HET_HAN;
        }
        return CON_HIEU_LUC;
    }

    public String layTrangThaiChu(LocalDate ngay) {
        String trangThai = layTrangThai(ngay);
        if (CHUA_BAT_DAU.equals(trangThai)) {
            return "Chưa bắt đầu";
        }
        if (HET_HAN.equals(trangThai)) {
            return "Hết hạn";
        }
        return "Còn hiệu lực";
    }

    // Số tiền được giảm cho tổng tiền cho trước.
    // Không đạt điều kiện tối thiểu thì giảm 0; ngược lại giảm theo % nhưng không vượt tienGiamToiDa.
    public double tinhTienGiam(double tongTien) {
        if (tongTien <= 0 || tongTien < dieuKienToiThieu) {
            return 0;
        }
        return Math.min(tongTien * phanTramGiam / 100, tienGiamToiDa);
    }

    public void hienThiThongTin() {
        System.out.printf("[%s] %s | giảm %.0f%% (tối đa %,.0f) | đơn từ %,.0f | %s -> %s%n",
                maKhuyenMai, tenKhuyenMai, phanTramGiam, tienGiamToiDa, dieuKienToiThieu,
                ngayBatDau, ngayKetThuc);
    }

    public String toCSV() {
        return HoaDon.lamSachTruong(maKhuyenMai) + "," + HoaDon.lamSachTruong(tenKhuyenMai) + ","
                + HoaDon.soThanhChuoi(phanTramGiam) + "," + HoaDon.soThanhChuoi(dieuKienToiThieu) + ","
                + HoaDon.soThanhChuoi(tienGiamToiDa) + "," + ngayBatDau + "," + ngayKetThuc;
    }

    public static KhuyenMai tuCSV(String dong) {
        String[] p = dong.split(",", -1);
        if (p.length < 7) {
            throw new IllegalArgumentException("Dòng khuyến mãi thiếu trường: " + dong);
        }
        try {
            return new KhuyenMai(p[0], p[1], Double.parseDouble(p[2]), Double.parseDouble(p[3]),
                    Double.parseDouble(p[4]), p[5], p[6]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Số không hợp lệ trong dòng: " + dong);
        }
    }

    public String getMaKhuyenMai() {
        return maKhuyenMai;
    }

    public void setMaKhuyenMai(String maKhuyenMai) {
        if (maKhuyenMai == null || maKhuyenMai.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã khuyến mãi không được để trống");
        }
        this.maKhuyenMai = maKhuyenMai.trim();
    }

    public String getTenKhuyenMai() {
        return tenKhuyenMai;
    }

    public void setTenKhuyenMai(String tenKhuyenMai) {
        if (tenKhuyenMai == null || tenKhuyenMai.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên khuyến mãi không được để trống");
        }
        this.tenKhuyenMai = tenKhuyenMai.trim();
    }

    public double getPhanTramGiam() {
        return phanTramGiam;
    }

    public void setPhanTramGiam(double phanTramGiam) {
        if (phanTramGiam < 0 || phanTramGiam > 100) {
            throw new IllegalArgumentException("Phần trăm giảm phải từ 0 đến 100");
        }
        this.phanTramGiam = phanTramGiam;
    }

    public double getDieuKienToiThieu() {
        return dieuKienToiThieu;
    }

    public void setDieuKienToiThieu(double dieuKienToiThieu) {
        if (dieuKienToiThieu < 0) {
            throw new IllegalArgumentException("Điều kiện tối thiểu không được âm");
        }
        this.dieuKienToiThieu = dieuKienToiThieu;
    }

    public double getTienGiamToiDa() {
        return tienGiamToiDa;
    }

    public void setTienGiamToiDa(double tienGiamToiDa) {
        if (tienGiamToiDa < 0) {
            throw new IllegalArgumentException("Tiền giảm tối đa không được âm");
        }
        this.tienGiamToiDa = tienGiamToiDa;
    }

    public String getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(String ngayBatDau) {
        LocalDate batDau = HoaDon.docNgay(ngayBatDau);
        if (batDau.isAfter(HoaDon.docNgay(this.ngayKetThuc))) {
            throw new IllegalArgumentException("Ngày bắt đầu không được sau ngày kết thúc");
        }
        this.ngayBatDau = ngayBatDau.trim();
    }

    public String getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(String ngayKetThuc) {
        LocalDate ketThuc = HoaDon.docNgay(ngayKetThuc);
        if (ketThuc.isBefore(HoaDon.docNgay(this.ngayBatDau))) {
            throw new IllegalArgumentException("Ngày kết thúc không được trước ngày bắt đầu");
        }
        this.ngayKetThuc = ngayKetThuc.trim();
    }

    @Override
    public String toString() {
        return String.format("%s - %s (giảm %.0f%%, tối đa %,.0f VND, đơn từ %,.0f VND, %s -> %s)",
                maKhuyenMai, tenKhuyenMai, phanTramGiam, tienGiamToiDa, dieuKienToiThieu,
                ngayBatDau, ngayKetThuc);
    }
}
