package payment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ThanhToan {

    public static final String CHO_XU_LY = "CHO_XU_LY";
    public static final String THANH_CONG = "THANH_CONG";
    public static final String THAT_BAI = "THAT_BAI";

    private static final DateTimeFormatter DINH_DANG_NGAY = DateTimeFormatter.ofPattern("dd/MM/uuuu");

    private String maThanhToan;
    private String maHoaDon;
    private double soTien;
    private String ngayThanhToan;
    private String trangThai;
    private IThanhToan phuongThuc;  
    private String tenPhuongThuc; 

    public ThanhToan(String maThanhToan, String maHoaDon, double soTien, IThanhToan phuongThuc) {
        setMaThanhToan(maThanhToan);
        setMaHoaDon(maHoaDon);
        setSoTien(soTien);
        setPhuongThuc(phuongThuc);
        this.trangThai = CHO_XU_LY;
    }

    public ThanhToan(String maThanhToan, String maHoaDon, double soTien,
                     String ngayThanhToan, String trangThai, String tenPhuongThuc) {
        setMaThanhToan(maThanhToan);
        setMaHoaDon(maHoaDon);
        setSoTien(soTien);
        if (!CHO_XU_LY.equals(trangThai) && !THANH_CONG.equals(trangThai) && !THAT_BAI.equals(trangThai)) {
            throw new IllegalArgumentException("Trạng thái thanh toán không hợp lệ: " + trangThai);
        }
        this.trangThai = trangThai;
        if (ngayThanhToan == null || ngayThanhToan.trim().isEmpty()) {
            this.ngayThanhToan = null;
        } else {
            this.ngayThanhToan = ngayThanhToan.trim();
        }
        this.tenPhuongThuc = (tenPhuongThuc == null) ? "" : tenPhuongThuc;
    }

    public boolean thucHienThanhToan() {
        if (THANH_CONG.equals(trangThai) || phuongThuc == null) {
            return false; 
        }
        boolean ketQua = phuongThuc.thanhToan(soTien);
        if (ketQua) {
            ngayThanhToan = LocalDate.now().format(DINH_DANG_NGAY);
            capNhatTrangThai();
        } else {
            trangThai = THAT_BAI;
        }
        return ketQua;
    }

    // Đồng bộ trạng thái theo dữ liệu: đã có ngày thanh toán thì THANH_CONG, chưa thì CHO_XU_LY (giữ THAT_BAI nếu đã thất bại)
    public void capNhatTrangThai() {
        if (ngayThanhToan != null) {
            trangThai = THANH_CONG;
        } else if (!THAT_BAI.equals(trangThai)) {
            trangThai = CHO_XU_LY;
        }
    }

    public void hienThiThongTin() {
        System.out.printf("  [%s] Hóa đơn %s | %,.0f VND | %s | Ngày: %s | Phương thức: %s%n",
                maThanhToan, maHoaDon, soTien, trangThai,
                ngayThanhToan == null ? "(chưa thanh toán)" : ngayThanhToan, tenPhuongThuc);
    }

    public String toCSV() {
        return "ThanhToan," + lamSach(maThanhToan) + "," + soThanhChuoi(soTien) + ","
                + (ngayThanhToan == null ? "" : ngayThanhToan) + "," + trangThai + "," + lamSach(tenPhuongThuc);
    }

    public static ThanhToan tuCSV(String dong, String maHoaDon) {
        String[] p = dong.split(",", -1);
        if (p.length < 6 || !p[0].equals("ThanhToan")) {
            throw new IllegalArgumentException("Dòng thanh toán sai định dạng: " + dong);
        }
        try {
            return new ThanhToan(p[1], maHoaDon, Double.parseDouble(p[2]), p[3], p[4], p[5]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Số không hợp lệ trong dòng: " + dong);
        }
    }

    private static String lamSach(String s) {
        if (s == null) {
            return "";
        }
        return s.replace(',', ';').replace('\n', ' ').replace('\r', ' ').trim();
    }

    private static String soThanhChuoi(double v) {
        return BigDecimal.valueOf(v).stripTrailingZeros().toPlainString();
    }

    public String getMaThanhToan() {
        return maThanhToan;
    }

    public void setMaThanhToan(String maThanhToan) {
        if (maThanhToan == null || maThanhToan.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã thanh toán không được để trống");
        }
        this.maThanhToan = maThanhToan.trim();
    }

    public String getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(String maHoaDon) {
        if (maHoaDon == null || maHoaDon.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã hóa đơn không được để trống");
        }
        this.maHoaDon = maHoaDon.trim();
    }

    public double getSoTien() {
        return soTien;
    }

    public void setSoTien(double soTien) {
        if (soTien <= 0) {
            throw new IllegalArgumentException("Số tiền thanh toán phải lớn hơn 0");
        }
        this.soTien = soTien;
    }

    public String getNgayThanhToan() {
        return ngayThanhToan;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public IThanhToan getPhuongThuc() {
        return phuongThuc;
    }

    public void setPhuongThuc(IThanhToan phuongThuc) {
        if (phuongThuc == null) {
            throw new IllegalArgumentException("Phương thức thanh toán không được null");
        }
        this.phuongThuc = phuongThuc;
        this.tenPhuongThuc = phuongThuc.layPhuongThuc();
    }

    public String getTenPhuongThuc() {
        return tenPhuongThuc;
    }
}
