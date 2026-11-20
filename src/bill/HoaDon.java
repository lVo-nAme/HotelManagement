package bill;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import payment.IThanhToan;
import payment.ThanhToan;
import person.NhanVien;

public class HoaDon {
   
    public static final String CHUA_THANH_TOAN = "CHUA_THANH_TOAN";
    public static final String DA_THANH_TOAN = "DA_THANH_TOAN";
    
    public static final DateTimeFormatter DINH_DANG_NGAY =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    private static int tongSoHoaDon = 0;

    private String maHoaDon;
    private String maDatPhong;
    private NhanVien nguoiLap;        
    private String maNguoiLap;        
    private String tenNguoiLap;
    private LocalDate ngayLap;
    private LocalDate ngayNhanPhong;
    private LocalDate ngayTraPhong;
    private IThanhToan phuongThucThanhToan;
    private double tienPhong;
    private double tienDichVu;
    private double tienGiam;
    private double tongTien;
    private String trangThai;

    private KhuyenMai khuyenMai;
    private String maKhuyenMai;         

    private ChiTietHoaDon[] dsChiTiet;
    private int soChiTiet;
    private ThanhToan[] dsThanhToan;
    private int soThanhToan;

    public HoaDon(String maHoaDon, String maDatPhong, NhanVien nguoiLap,
                  LocalDate ngayLap, LocalDate ngayNhanPhong, LocalDate ngayTraPhong) {
        this(maHoaDon, maDatPhong, layMaNguoiLap(nguoiLap), nguoiLap.getHoTen(),
                ngayLap, ngayNhanPhong, ngayTraPhong);
        this.nguoiLap = nguoiLap;
    }

    public HoaDon(String maHoaDon, String maDatPhong, String maNguoiLap, String tenNguoiLap,
                  LocalDate ngayLap, LocalDate ngayNhanPhong, LocalDate ngayTraPhong) {
        setMaHoaDon(maHoaDon);
        setMaDatPhong(maDatPhong);
        if (maNguoiLap == null || maNguoiLap.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã người lập không được để trống");
        }
        this.maNguoiLap = maNguoiLap.trim();
        this.tenNguoiLap = (tenNguoiLap == null) ? "" : tenNguoiLap.trim();
        kiemTraNgay(ngayLap, ngayNhanPhong, ngayTraPhong);
        this.ngayLap = ngayLap;
        this.ngayNhanPhong = ngayNhanPhong;
        this.ngayTraPhong = ngayTraPhong;
        this.trangThai = CHUA_THANH_TOAN;
        this.maKhuyenMai = "";
        this.dsChiTiet = new ChiTietHoaDon[4];
        this.soChiTiet = 0;
        this.dsThanhToan = new ThanhToan[2];
        this.soThanhToan = 0;
    }

    private static String layMaNguoiLap(NhanVien nv) {
        if (nv == null) {
            throw new IllegalArgumentException("Người lập hóa đơn không được null");
        }
        return nv.getMaNguoi();
    }

    private static void kiemTraNgay(LocalDate lap, LocalDate nhan, LocalDate tra) {
        if (lap == null || nhan == null || tra == null) {
            throw new IllegalArgumentException("Các ngày lập / nhận phòng / trả phòng không được null");
        }
        if (tra.isBefore(nhan)) {
            throw new IllegalArgumentException("Ngày trả phòng không được trước ngày nhận phòng");
        }
    }

    public static String taoMaHoaDon() {
        tongSoHoaDon++;
        return String.format("HD%04d", tongSoHoaDon);
    }

    public static void capNhatBoDem(String maDaCo) {
        if (maDaCo == null || !maDaCo.startsWith("HD")) {
            return;
        }
        try {
            int so = Integer.parseInt(maDaCo.substring(2));
            if (so > tongSoHoaDon) {
                tongSoHoaDon = so;
            }
        } catch (NumberFormatException e) {
          
        }
    }

    public static int getTongSoHoaDon() {
        return tongSoHoaDon;
    }

    public static LocalDate docNgay(String s) {
        if (s == null || s.trim().isEmpty()) {
            throw new IllegalArgumentException("Ngày không được để trống");
        }
        try {
            return LocalDate.parse(s.trim(), DINH_DANG_NGAY);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Ngày không hợp lệ (cần dd/MM/yyyy): " + s);
        }
    }

    public static String dinhDangNgay(LocalDate ngay) {
        return ngay == null ? "" : ngay.format(DINH_DANG_NGAY);
    }

    public static String soThanhChuoi(double v) {
        return BigDecimal.valueOf(v).stripTrailingZeros().toPlainString();
    }

    public static String lamSachTruong(String s) {
        if (s == null) {
            return "";
        }
        return s.replace(',', ';').replace('\n', ' ').replace('\r', ' ').trim();
    }

    public double tinhTongTien() {
        tienPhong = 0;
        tienDichVu = 0;
        for (int i = 0; i < soChiTiet; i++) {
            ChiTietHoaDon chiTiet = dsChiTiet[i];
            if (ChiTietHoaDon.LOAI_TIEN_PHONG.equals(chiTiet.getLoaiChiTiet())) {
                tienPhong += chiTiet.tinhTongCong();
            } else {
                tienDichVu += chiTiet.tinhTongCong();
            }
        }
        double truocGiam = tienPhong + tienDichVu;

        if (khuyenMai != null && !DA_THANH_TOAN.equals(trangThai)) {
            tienGiam = khuyenMai.tinhTienGiam(truocGiam);
        }
        if (tienGiam > truocGiam) {
            tienGiam = truocGiam;
        }
        tongTien = truocGiam - tienGiam;
        return tongTien;
    }

    public void apDungKhuyenMai(KhuyenMai km) {
        kiemTraChuaThanhToan();
        if (km == null) {
            throw new IllegalArgumentException("Khuyến mãi không được null");
        }
        if (!km.kiemTraHieuLuc(ngayLap)) {
            throw new IllegalArgumentException("Khuyến mãi " + km.getMaKhuyenMai()
                    + " không còn hiệu lực vào ngày lập hóa đơn");
        }
        tinhTongTien();
        double truocGiam = tienPhong + tienDichVu;
        if (truocGiam < km.getDieuKienToiThieu()) {
            throw new IllegalArgumentException(String.format(
                    "Hóa đơn chưa đạt điều kiện tối thiểu %,.0f VND của khuyến mãi %s",
                    km.getDieuKienToiThieu(), km.getMaKhuyenMai()));
        }
        this.khuyenMai = km;
        this.maKhuyenMai = km.getMaKhuyenMai();
        tinhTongTien();
    }

    public void boKhuyenMai() {
        kiemTraChuaThanhToan();
        khuyenMai = null;
        maKhuyenMai = "";
        tienGiam = 0;
        tinhTongTien();
    }

    public void themChiTiet(ChiTietHoaDon chiTiet) {
        kiemTraChuaThanhToan();
        ghiChiTietVaoMang(chiTiet);
        tinhTongTien();
    }

    public boolean xoaChiTiet(String maChiTiet) {
        kiemTraChuaThanhToan();
        int viTri = timViTriChiTiet(maChiTiet);
        if (viTri < 0) {
            return false;
        }
        for (int i = viTri; i < soChiTiet - 1; i++) {
            dsChiTiet[i] = dsChiTiet[i + 1];
        }
        dsChiTiet[--soChiTiet] = null;
        tinhTongTien();
        return true;
    }

    public void suaChiTiet(String maChiTiet, int soLuongMoi, double donGiaMoi) {
        kiemTraChuaThanhToan();
        ChiTietHoaDon chiTiet = timChiTiet(maChiTiet);
        if (chiTiet == null) {
            throw new IllegalArgumentException("Không có chi tiết " + maChiTiet + " trong hóa đơn");
        }
        chiTiet.setSoLuong(soLuongMoi);
        chiTiet.setDonGia(donGiaMoi);
        tinhTongTien();
    }

    public ChiTietHoaDon timChiTiet(String maChiTiet) {
        int viTri = timViTriChiTiet(maChiTiet);
        return viTri < 0 ? null : dsChiTiet[viTri];
    }

    public String taoMaChiTiet() {
        int k = soChiTiet + 1;
        while (timChiTiet("CT" + k) != null) {
            k++;
        }
        return "CT" + k;
    }

    public boolean thanhToan(IThanhToan pt) {
        if (pt == null) {
            throw new IllegalArgumentException("Phương thức thanh toán không được null");
        }
        kiemTraChuaThanhToan();
        if (soChiTiet == 0) {
            throw new IllegalStateException("Hóa đơn phải có ít nhất một chi tiết trước khi thanh toán");
        }
        tinhTongTien();
        if (tongTien <= 0) { 
            trangThai = DA_THANH_TOAN;
            return true;
        }
        String maThanhToan = "TT-" + maHoaDon + "-" + (soThanhToan + 1);
        ThanhToan giaoDich = new ThanhToan(maThanhToan, maHoaDon, tongTien, pt);
        boolean ketQua = giaoDich.thucHienThanhToan();
        ghiThanhToanVaoMang(giaoDich);
        if (ketQua) {
            phuongThucThanhToan = pt;
            trangThai = DA_THANH_TOAN;
        }
        return ketQua;
    }

    public void hienThiHoaDon() {
        tinhTongTien();
        System.out.println("=========== HÓA ĐƠN " + maHoaDon + " ===========");
        System.out.println("Mã đặt phòng : " + maDatPhong);
        System.out.println("Người lập    : " + tenNguoiLap + " (" + maNguoiLap + ")");
        System.out.println("Ngày lập     : " + dinhDangNgay(ngayLap));
        System.out.println("Lưu trú      : " + dinhDangNgay(ngayNhanPhong) + " -> " + dinhDangNgay(ngayTraPhong));
        System.out.println("--- Chi tiết ---");
        for (int i = 0; i < soChiTiet; i++) {
            dsChiTiet[i].hienThiChiTiet();
        }
        System.out.println("----------------");
        System.out.printf("Tiền phòng   : %,.0f VND%n", tienPhong);
        System.out.printf("Tiền dịch vụ : %,.0f VND%n", tienDichVu);
        System.out.printf("Giảm giá     : -%,.0f VND%s%n", tienGiam,
                maKhuyenMai.isEmpty() ? "" : " (" + maKhuyenMai + ")");
        System.out.printf("TỔNG TIỀN    : %,.0f VND%n", tongTien);
        System.out.println("Trạng thái   : " + getTrangThaiChu());
        if (soThanhToan > 0) {
            System.out.println("--- Giao dịch ---");
            for (int i = 0; i < soThanhToan; i++) {
                dsThanhToan[i].hienThiThongTin();
            }
        }
        System.out.println("==========================================");
    }
    
    public String getTrangThaiChu() {
        return DA_THANH_TOAN.equals(trangThai) ? "Đã thanh toán" : "Chưa thanh toán";
    }
    
    private int timViTriChiTiet(String maChiTiet) {
        if (maChiTiet == null) {
            return -1;
        }
        for (int i = 0; i < soChiTiet; i++) {
            if (dsChiTiet[i].getMaChiTiet().equalsIgnoreCase(maChiTiet.trim())) {
                return i;
            }
        }
        return -1;
    }

    private void ghiChiTietVaoMang(ChiTietHoaDon chiTiet) {
        if (chiTiet == null) {
            throw new IllegalArgumentException("Chi tiết hóa đơn không được null");
        }
        if (timChiTiet(chiTiet.getMaChiTiet()) != null) {
            throw new IllegalArgumentException("Mã chi tiết " + chiTiet.getMaChiTiet() + " đã tồn tại trong hóa đơn");
        }
        if (soChiTiet == dsChiTiet.length) { 
            ChiTietHoaDon[] mangMoi = new ChiTietHoaDon[dsChiTiet.length * 2];
            for (int i = 0; i < soChiTiet; i++) {
                mangMoi[i] = dsChiTiet[i];
            }
            dsChiTiet = mangMoi;
        }
        dsChiTiet[soChiTiet++] = chiTiet;
    }

    private void ghiThanhToanVaoMang(ThanhToan giaoDich) {
        if (soThanhToan == dsThanhToan.length) {
            ThanhToan[] mangMoi = new ThanhToan[dsThanhToan.length * 2];
            for (int i = 0; i < soThanhToan; i++) {
                mangMoi[i] = dsThanhToan[i];
            }
            dsThanhToan = mangMoi;
        }
        dsThanhToan[soThanhToan++] = giaoDich;
    }

    private void kiemTraChuaThanhToan() {
        if (DA_THANH_TOAN.equals(trangThai)) {
            throw new IllegalStateException("Hóa đơn " + maHoaDon + " đã thanh toán, không thể thay đổi");
        }
    }

    public String toCSV() {
        tinhTongTien();
        return "HoaDon," + lamSachTruong(maHoaDon) + "," + lamSachTruong(maDatPhong) + ","
                + lamSachTruong(maNguoiLap) + "," + lamSachTruong(tenNguoiLap) + ","
                + dinhDangNgay(ngayLap) + "," + dinhDangNgay(ngayNhanPhong) + "," + dinhDangNgay(ngayTraPhong) + ","
                + lamSachTruong(maKhuyenMai) + "," + soThanhChuoi(tienGiam) + "," + trangThai;
    }

    public static HoaDon tuCacDongCSV(String[] cacDong, int tu, int den) {
        String[] p = cacDong[tu].trim().split(",", -1);
        if (p.length < 11 || !p[0].equals("HoaDon")) {
            throw new IllegalArgumentException("Dòng hóa đơn sai định dạng: " + cacDong[tu]);
        }
        if (!CHUA_THANH_TOAN.equals(p[10]) && !DA_THANH_TOAN.equals(p[10])) {
            throw new IllegalArgumentException("Trạng thái hóa đơn không hợp lệ: " + p[10]);
        }
        HoaDon hoaDon;
        try {
            hoaDon = new HoaDon(p[1], p[2], p[3], p[4], docNgay(p[5]), docNgay(p[6]), docNgay(p[7]));
            hoaDon.maKhuyenMai = p[8];
            hoaDon.tienGiam = Double.parseDouble(p[9]);
            hoaDon.trangThai = p[10];
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Số không hợp lệ trong dòng: " + cacDong[tu]);
        }
        for (int i = tu + 1; i < den; i++) {
            String dong = cacDong[i].trim();
            if (dong.isEmpty() || dong.startsWith("#")) {
                continue;
            }
            try {
                if (dong.startsWith("ChiTiet,")) {
                    hoaDon.ghiChiTietVaoMang(ChiTietHoaDon.tuCSV(dong)); 
                    hoaDon.tinhTongTien();
                } else if (dong.startsWith("ThanhToan,")) {
                    hoaDon.ghiThanhToanVaoMang(ThanhToan.tuCSV(dong, hoaDon.maHoaDon));
                } else {
                    System.out.println("=> Bỏ qua dòng không nhận ra trong hóa đơn " + hoaDon.maHoaDon + ": " + dong);
                }
            } catch (IllegalArgumentException e) {
                System.out.println("=> Bỏ qua dòng lỗi trong hóa đơn " + hoaDon.maHoaDon + ": " + e.getMessage());
            }
        }
        hoaDon.tinhTongTien();
        return hoaDon;
    }

    public void noiKhuyenMai(KhuyenMai km) {
        if (km != null && km.getMaKhuyenMai().equalsIgnoreCase(maKhuyenMai)) {
            this.khuyenMai = km;
            tinhTongTien();
        }
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

    public String getMaDatPhong() {
        return maDatPhong;
    }

    public void setMaDatPhong(String maDatPhong) {
        if (maDatPhong == null || maDatPhong.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã đặt phòng không được để trống");
        }
        this.maDatPhong = maDatPhong.trim();
    }

    public NhanVien getNguoiLap() {
        return nguoiLap;
    }

    public String getMaNguoiLap() {
        return maNguoiLap;
    }

    public String getTenNguoiLap() {
        return tenNguoiLap;
    }

    public LocalDate getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(LocalDate ngayLap) {
        kiemTraChuaThanhToan();
        kiemTraNgay(ngayLap, ngayNhanPhong, ngayTraPhong);
        this.ngayLap = ngayLap;
    }

    public LocalDate getNgayNhanPhong() {
        return ngayNhanPhong;
    }

    public LocalDate getNgayTraPhong() {
        return ngayTraPhong;
    }

    public void datNgayLuuTru(LocalDate nhan, LocalDate tra) {
        kiemTraChuaThanhToan();
        kiemTraNgay(ngayLap, nhan, tra);
        this.ngayNhanPhong = nhan;
        this.ngayTraPhong = tra;
    }

    public IThanhToan getPhuongThucThanhToan() {
        return phuongThucThanhToan;
    }

    public String getTenPhuongThucThanhToan() {
        for (int i = soThanhToan - 1; i >= 0; i--) {
            if (ThanhToan.THANH_CONG.equals(dsThanhToan[i].getTrangThai())) {
                return dsThanhToan[i].getTenPhuongThuc();
            }
        }
        return "";
    }

    public double getTienPhong() {
        tinhTongTien();
        return tienPhong;
    }

    public double getTienDichVu() {
        tinhTongTien();
        return tienDichVu;
    }

    public double getTienGiam() {
        tinhTongTien();
        return tienGiam;
    }

    public double getTongTien() {
        return tinhTongTien();
    }

    public String getTrangThai() {
        return trangThai;
    }

    public KhuyenMai getKhuyenMai() {
        return khuyenMai;
    }

    public String getMaKhuyenMai() {
        return maKhuyenMai;
    }

    public int getSoChiTiet() {
        return soChiTiet;
    }

    public ChiTietHoaDon getChiTiet(int i) {
        if (i < 0 || i >= soChiTiet) {
            throw new IndexOutOfBoundsException("Chỉ số chi tiết không hợp lệ: " + i);
        }
        return dsChiTiet[i];
    }

    public int getSoThanhToan() {
        return soThanhToan;
    }

    public ThanhToan getThanhToan(int i) {
        if (i < 0 || i >= soThanhToan) {
            throw new IndexOutOfBoundsException("Chỉ số thanh toán không hợp lệ: " + i);
        }
        return dsThanhToan[i];
    }
}
