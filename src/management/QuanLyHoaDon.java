package management;

import bill.ChiTietHoaDon;
import bill.HoaDon;
import bill.KhuyenMai;
import bill.ThoiDiemThanhToan;
import fileio.FileHandler;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import payment.IThanhToan;
import util.InputHelper;

public class QuanLyHoaDon {
    private static final int DUNG_LUONG_MAC_DINH = 10;
    private static final String TEN_FILE_HOA_DON = "hoadon.txt";
    private static final String TEN_FILE_KHUYEN_MAI = "khuyenmai.txt";

    private HoaDon[] danhSachHoaDon;
    private KhuyenMai[] danhSachKhuyenMai;
    private int soLuongHoaDon;
    private int soLuongKhuyenMai;

    public QuanLyHoaDon() {
        this(DUNG_LUONG_MAC_DINH);
    }

    public QuanLyHoaDon(int sucChua) {
        int kichThuoc = sucChua > 0 ? sucChua : DUNG_LUONG_MAC_DINH;
        danhSachHoaDon = new HoaDon[kichThuoc];
        danhSachKhuyenMai = new KhuyenMai[kichThuoc];
        soLuongHoaDon = 0;
        soLuongKhuyenMai = 0;
    }
    public int getSoLuongHoaDon() {
        return soLuongHoaDon;
    }

    public HoaDon getHoaDon(int viTri) {
        if (viTri < 0 || viTri >= soLuongHoaDon) {
            throw new IndexOutOfBoundsException("Vị trí hóa đơn không hợp lệ: " + viTri);
        }
        return danhSachHoaDon[viTri];
    }

    public int getSoLuongKhuyenMai() {
        return soLuongKhuyenMai;
    }

    public KhuyenMai getKhuyenMai(int viTri) {
        if (viTri < 0 || viTri >= soLuongKhuyenMai) {
            throw new IndexOutOfBoundsException("Vị trí khuyến mãi không hợp lệ: " + viTri);
        }
        return danhSachKhuyenMai[viTri];
    }

    public void nhapDanhSachHoaDon(int soLuong, String maNhanVien, String tenNhanVien) {
        if (soLuong < 0) {
            throw new IllegalArgumentException("Số lượng hóa đơn không được âm.");
        }
        for (int i = 1; i <= soLuong; i++) {
            System.out.println("\n========== NHẬP HÓA ĐƠN " + i + "/" + soLuong + " ==========");
            HoaDon hoaDon = nhapThongTinHoaDon(maNhanVien, tenNhanVien);
            if (!themHoaDon(hoaDon)) {
                System.out.println("Mã hóa đơn đã tồn tại, không thể thêm.");
            }
        }
    }

    public boolean themHoaDon(HoaDon hoaDon) {
        if (hoaDon == null || timViTriHoaDon(hoaDon.getMaHoaDon()) >= 0) {
            return false;
        }
        moRongMangHoaDon();
        danhSachHoaDon[soLuongHoaDon++] = hoaDon;
        HoaDon.capNhatBoDem(hoaDon.getMaHoaDon());
        return true;
    }

    public void hienThiDanhSachHoaDon() {
        inBangHoaDon(danhSachHoaDon, soLuongHoaDon, "DANH SÁCH HÓA ĐƠN");
    }

    public static void hienThiKetQuaHoaDon(HoaDon[] ketQua) {
        if (ketQua == null) {
            inBangHoaDon(new HoaDon[0], 0, "KẾT QUẢ TÌM KIẾM");
        } else {
            inBangHoaDon(ketQua, ketQua.length, "KẾT QUẢ TÌM KIẾM");
        }
    }

    public boolean hienThiChiTietHoaDon(String maHoaDon) {
        HoaDon hoaDon = timHoaDonTheoMa(maHoaDon);
        if (hoaDon == null) {
            System.out.println("Không tìm thấy hóa đơn " + maHoaDon + ".");
            return false;
        }
        hoaDon.hienThiHoaDon();
        return true;
    }

    public boolean suaHoaDon(String maHoaDon) {
        HoaDon hoaDon = timHoaDonTheoMa(maHoaDon);
        if (hoaDon == null) {
            System.out.println("Không tìm thấy hóa đơn " + maHoaDon + ".");
            return false;
        }
        if (HoaDon.DA_THANH_TOAN.equals(hoaDon.getTrangThai())) {
            System.out.println("Hóa đơn đã thanh toán nên không thể chỉnh sửa.");
            return false;
        }

        int luaChon;
        do {
            System.out.println("\n---------- SỬA HÓA ĐƠN " + hoaDon.getMaHoaDon() + " ----------");
            System.out.println("1. Đổi mã đặt phòng");
            System.out.println("2. Đổi ngày lập hóa đơn");
            System.out.println("3. Đổi ngày nhận và trả phòng");
            System.out.println("4. Thêm chi tiết hóa đơn");
            System.out.println("5. Sửa số lượng và đơn giá chi tiết");
            System.out.println("6. Xóa chi tiết hóa đơn");
            System.out.println("7. Bỏ khuyến mãi");
            System.out.println("0. Quay lại");
            luaChon = InputHelper.getIntInRange("Chọn chức năng: ", 0, 7);

            try {
                switch (luaChon) {
                    case 1:
                        hoaDon.setMaDatPhong(InputHelper.getString("Nhập mã đặt phòng mới: "));
                        break;
                    case 2:
                        hoaDon.setNgayLap(docNgay("Nhập ngày lập mới (dd/MM/yyyy): "));
                        break;
                    case 3: {
                        LocalDate ngayNhan = docNgay("Nhập ngày nhận phòng mới (dd/MM/yyyy): ");
                        LocalDate ngayTra = docNgay("Nhập ngày trả phòng mới (dd/MM/yyyy): ");
                        hoaDon.datNgayLuuTru(ngayNhan, ngayTra);
                        break;
                    }
                    case 4:
                        hoaDon.themChiTiet(nhapChiTietHoaDon(hoaDon.taoMaChiTiet()));
                        break;
                    case 5: {
                        String maChiTiet = InputHelper.getString("Nhập mã chi tiết cần sửa: ");
                        int soLuong = InputHelper.getIntInRange("Nhập số lượng mới: ", 1, 100000);
                        double donGia = docSoThuc("Nhập đơn giá mới (VND): ", 0, Double.MAX_VALUE);
                        hoaDon.suaChiTiet(maChiTiet, soLuong, donGia);
                        break;
                    }
                    case 6: {
                        String maChiTiet = InputHelper.getString("Nhập mã chi tiết cần xóa: ");
                        if (!hoaDon.xoaChiTiet(maChiTiet)) {
                            System.out.println("Không tìm thấy chi tiết " + maChiTiet + ".");
                        }
                        break;
                    }
                    case 7:
                        hoaDon.boKhuyenMai();
                        System.out.println("Đã bỏ khuyến mãi khỏi hóa đơn.");
                        break;
                    default:
                        break;
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Không thể thực hiện: " + e.getMessage());
            }
        } while (luaChon != 0);
        return true;
    }

    public boolean xoaHoaDon(String maHoaDon) {
        int viTri = timViTriHoaDon(maHoaDon);
        if (viTri < 0) {
            System.out.println("Không tìm thấy hóa đơn " + maHoaDon + ".");
            return false;
        }
        if (HoaDon.DA_THANH_TOAN.equals(danhSachHoaDon[viTri].getTrangThai())) {
            System.out.println("Hóa đơn đã thanh toán nên không thể xóa.");
            return false;
        }
        for (int i = viTri; i < soLuongHoaDon - 1; i++) {
            danhSachHoaDon[i] = danhSachHoaDon[i + 1];
        }
        danhSachHoaDon[--soLuongHoaDon] = null;
        return true;
    }

    public HoaDon timHoaDonTheoMa(String maHoaDon) {
        int viTri = timViTriHoaDon(maHoaDon);
        return viTri < 0 ? null : danhSachHoaDon[viTri];
    }

    public HoaDon timHoaDonTheoMaDatPhong(String maDatPhong) {
        if (maDatPhong == null) {
            return null;
        }
        for (int i = 0; i < soLuongHoaDon; i++) {
            if (danhSachHoaDon[i].getMaDatPhong().equalsIgnoreCase(maDatPhong.trim())) {
                return danhSachHoaDon[i];
            }
        }
        return null;
    }

    public HoaDon[] timKiemHoaDon(String tuKhoa) {
        return timKiemHoaDon(tuKhoa, null, null, null, -1, -1);
    }

    public HoaDon[] timKiemHoaDon(String tuKhoa, String trangThai,
                                  LocalDate tuNgay, LocalDate denNgay,
                                  double tienToiThieu, double tienToiDa) {
        List<HoaDon> ketQua = new ArrayList<>();
        String tuKhoaChuanHoa = chuanHoa(tuKhoa);

        for (int i = 0; i < soLuongHoaDon; i++) {
            HoaDon hoaDon = danhSachHoaDon[i];
            if (trangThai != null && !trangThai.trim().isEmpty()
                    && !hoaDon.getTrangThai().equalsIgnoreCase(trangThai.trim())) {
                continue;
            }
            if (tuNgay != null && hoaDon.getNgayLap().isBefore(tuNgay)) {
                continue;
            }
            if (denNgay != null && hoaDon.getNgayLap().isAfter(denNgay)) {
                continue;
            }
            double tongTien = hoaDon.getTongTien();
            if (tienToiThieu >= 0 && tongTien < tienToiThieu) {
                continue;
            }
            if (tienToiDa >= 0 && tongTien > tienToiDa) {
                continue;
            }
            if (!tuKhoaChuanHoa.isEmpty() && !hoaDonPhuHopTuKhoa(hoaDon, tuKhoaChuanHoa)) {
                continue;
            }
            ketQua.add(hoaDon);
        }
        return ketQua.toArray(new HoaDon[0]);
    }

    public HoaDon[] layDanhSachHoaDonChuaThanhToan() {
        List<HoaDon> ketQua = new ArrayList<>();
        for (int i = 0; i < soLuongHoaDon; i++) {
            if (HoaDon.CHUA_THANH_TOAN.equals(danhSachHoaDon[i].getTrangThai())) {
                ketQua.add(danhSachHoaDon[i]);
            }
        }
        return ketQua.toArray(new HoaDon[0]);
    }

    public void sapXepHoaDonTheoTongTien(boolean tangDan) {
        for (int i = 0; i < soLuongHoaDon - 1; i++) {
            int viTriTotNhat = i;
            for (int j = i + 1; j < soLuongHoaDon; j++) {
                boolean totHon = tangDan
                        ? danhSachHoaDon[j].getTongTien() < danhSachHoaDon[viTriTotNhat].getTongTien()
                        : danhSachHoaDon[j].getTongTien() > danhSachHoaDon[viTriTotNhat].getTongTien();
                if (totHon) {
                    viTriTotNhat = j;
                }
            }
            doiChoHoaDon(i, viTriTotNhat);
        }
    }

    public void sapXepHoaDonTheoNgayLap() {
        for (int i = 0; i < soLuongHoaDon - 1; i++) {
            int viTriSomNhat = i;
            for (int j = i + 1; j < soLuongHoaDon; j++) {
                if (danhSachHoaDon[j].getNgayLap().isBefore(danhSachHoaDon[viTriSomNhat].getNgayLap())) {
                    viTriSomNhat = j;
                }
            }
            doiChoHoaDon(i, viTriSomNhat);
        }
    }

    public boolean thanhToanHoaDon(String maHoaDon, IThanhToan phuongThucThanhToan) {
        HoaDon hoaDon = timHoaDonTheoMa(maHoaDon);
        if (hoaDon == null) {
            System.out.println("Không tìm thấy hóa đơn " + maHoaDon + ".");
            return false;
        }
        if (phuongThucThanhToan == null) {
            System.out.println("Phương thức thanh toán không được để trống.");
            return false;
        }
        try {
            return hoaDon.thanhToan(phuongThucThanhToan);
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Thanh toán không thành công: " + e.getMessage());
            return false;
        }
    }

    public double tinhTongDoanhThu() {
        return tinhTongDoanhThu(null, null);
    }

    public double tinhTongDoanhThu(LocalDate tuNgay, LocalDate denNgay) {
        double tongDoanhThu = 0;
        for (int i = 0; i < soLuongHoaDon; i++) {
            HoaDon hoaDon = danhSachHoaDon[i];
            if (!HoaDon.DA_THANH_TOAN.equals(hoaDon.getTrangThai())) {
                continue;
            }
            if (tuNgay != null && hoaDon.getNgayLap().isBefore(tuNgay)) {
                continue;
            }
            if (denNgay != null && hoaDon.getNgayLap().isAfter(denNgay)) {
                continue;
            }
            tongDoanhThu += hoaDon.getTongTien();
        }
        return tongDoanhThu;
    }

    public double[] thongKeDoanhThuTheoThang(int nam) {
        double[] doanhThu = new double[12];
        int[] soHoaDon = new int[12];
        for (int i = 0; i < soLuongHoaDon; i++) {
            HoaDon hoaDon = danhSachHoaDon[i];
            if (HoaDon.DA_THANH_TOAN.equals(hoaDon.getTrangThai())
                    && hoaDon.getNgayLap().getYear() == nam) {
                int thang = hoaDon.getNgayLap().getMonthValue() - 1;
                doanhThu[thang] += hoaDon.getTongTien();
                soHoaDon[thang]++;
            }
        }

        System.out.println("\n========== DOANH THU THEO THÁNG NĂM " + nam + " ==========");
        double tongNam = 0;
        for (int i = 0; i < 12; i++) {
            System.out.printf("Tháng %02d: %3d hóa đơn | %,18.0f VND%n", i + 1, soHoaDon[i], doanhThu[i]);
            tongNam += doanhThu[i];
        }
        System.out.printf("TỔNG DOANH THU NĂM %d: %,18.0f VND%n", nam, tongNam);
        return doanhThu;
    }

    public void thongKeTheoTrangThai() {
        int daThanhToan = 0;
        int chuaThanhToan = 0;
        double tienDaThanhToan = 0;
        double tienChuaThanhToan = 0;
        for (int i = 0; i < soLuongHoaDon; i++) {
            HoaDon hoaDon = danhSachHoaDon[i];
            if (HoaDon.DA_THANH_TOAN.equals(hoaDon.getTrangThai())) {
                daThanhToan++;
                tienDaThanhToan += hoaDon.getTongTien();
            } else {
                chuaThanhToan++;
                tienChuaThanhToan += hoaDon.getTongTien();
            }
        }
        System.out.println("\n========== THỐNG KÊ THEO TRẠNG THÁI ==========");
        System.out.printf("Đã thanh toán   : %d hóa đơn | %,18.0f VND%n", daThanhToan, tienDaThanhToan);
        System.out.printf("Chưa thanh toán : %d hóa đơn | %,18.0f VND%n", chuaThanhToan, tienChuaThanhToan);
        System.out.printf("Tổng cộng       : %d hóa đơn | %,18.0f VND%n",
                daThanhToan + chuaThanhToan, tienDaThanhToan + tienChuaThanhToan);
    }

    public void thongKeTheoNhanVien() {
        String[] maNhanVien = new String[soLuongHoaDon];
        String[] tenNhanVien = new String[soLuongHoaDon];
        int[] soHoaDon = new int[soLuongHoaDon];
        double[] tongTien = new double[soLuongHoaDon];
        int soNhom = 0;

        for (int i = 0; i < soLuongHoaDon; i++) {
            HoaDon hoaDon = danhSachHoaDon[i];
            String ma = hoaDon.getMaNguoiLap();
            int viTri = -1;
            for (int j = 0; j < soNhom; j++) {
                if (maNhanVien[j].equalsIgnoreCase(ma)) {
                    viTri = j;
                    break;
                }
            }
            if (viTri < 0) {
                viTri = soNhom++;
                maNhanVien[viTri] = ma;
                tenNhanVien[viTri] = hoaDon.getTenNguoiLap();
            }
            soHoaDon[viTri]++;
            tongTien[viTri] += hoaDon.getTongTien();
        }

        System.out.println("\n========== THỐNG KÊ HÓA ĐƠN THEO NHÂN VIÊN ==========");
        if (soNhom == 0) {
            System.out.println("Chưa có dữ liệu.");
            return;
        }
        System.out.printf("%-14s %-24s %10s %18s%n", "Mã nhân viên", "Họ tên", "Số hóa đơn", "Tổng tiền");
        for (int i = 0; i < soNhom; i++) {
            System.out.printf("%-14s %-24.24s %10d %,18.0f%n",
                    maNhanVien[i], tenNhanVien[i], soHoaDon[i], tongTien[i]);
        }
    }

    public void thongKeTienPhongVaDichVu() {
        double tongTienPhong = 0;
        double tongTienDichVu = 0;
        for (int i = 0; i < soLuongHoaDon; i++) {
            if (HoaDon.DA_THANH_TOAN.equals(danhSachHoaDon[i].getTrangThai())) {
                tongTienPhong += danhSachHoaDon[i].getTienPhong();
                tongTienDichVu += danhSachHoaDon[i].getTienDichVu();
            }
        }
        System.out.println("\n========== DOANH THU PHÒNG VÀ DỊCH VỤ ==========");
        System.out.printf("Tiền phòng   : %,18.0f VND%n", tongTienPhong);
        System.out.printf("Tiền dịch vụ : %,18.0f VND%n", tongTienDichVu);
        System.out.printf("Tổng cộng    : %,18.0f VND%n", tongTienPhong + tongTienDichVu);
    }

    public void thongKeTheoPhuongThucThanhToan() {
        String[] tenPhuongThuc = new String[soLuongHoaDon];
        int[] soHoaDon = new int[soLuongHoaDon];
        double[] doanhThu = new double[soLuongHoaDon];
        int soNhom = 0;

        for (int i = 0; i < soLuongHoaDon; i++) {
            HoaDon hoaDon = danhSachHoaDon[i];
            if (!HoaDon.DA_THANH_TOAN.equals(hoaDon.getTrangThai())) {
                continue;
            }
            String ten = hoaDon.getTenPhuongThucThanhToan();
            if (ten == null || ten.trim().isEmpty()) {
                ten = "Chưa xác định";
            }
            int viTri = -1;
            for (int j = 0; j < soNhom; j++) {
                if (tenPhuongThuc[j].equalsIgnoreCase(ten)) {
                    viTri = j;
                    break;
                }
            }
            if (viTri < 0) {
                viTri = soNhom++;
                tenPhuongThuc[viTri] = ten;
            }
            soHoaDon[viTri]++;
            doanhThu[viTri] += hoaDon.getTongTien();
        }

        System.out.println("\n========== DOANH THU THEO PHƯƠNG THỨC THANH TOÁN ==========");
        if (soNhom == 0) {
            System.out.println("Chưa có dữ liệu thanh toán.");
            return;
        }
        System.out.printf("%-26s %10s %18s%n", "Phương thức", "Số hóa đơn", "Doanh thu");
        for (int i = 0; i < soNhom; i++) {
            System.out.printf("%-26.26s %10d %,18.0f%n", tenPhuongThuc[i], soHoaDon[i], doanhThu[i]);
        }
    }
    
    public void hienThiDanhSachKhuyenMai() {
        inBangKhuyenMai(danhSachKhuyenMai, soLuongKhuyenMai, "DANH SÁCH KHUYẾN MÃI");
    }

    public boolean themKhuyenMai(KhuyenMai khuyenMai) {
        if (khuyenMai == null || timViTriKhuyenMai(khuyenMai.getMaKhuyenMai()) >= 0) {
            return false;
        }
        moRongMangKhuyenMai();
        danhSachKhuyenMai[soLuongKhuyenMai++] = khuyenMai;
        return true;
    }

    public void nhapDanhSachKhuyenMai(int soLuong) {
        if (soLuong < 0) {
            throw new IllegalArgumentException("Số lượng khuyến mãi không được âm.");
        }
        for (int i = 1; i <= soLuong; i++) {
            System.out.println("\n========== NHẬP KHUYẾN MÃI " + i + "/" + soLuong + " ==========");
            KhuyenMai khuyenMai = nhapThongTinKhuyenMai();
            if (!themKhuyenMai(khuyenMai)) {
                System.out.println("Mã khuyến mãi đã tồn tại, không thể thêm.");
            }
        }
    }

    public boolean suaKhuyenMai(String maKhuyenMai) {
        KhuyenMai khuyenMai = timKhuyenMaiTheoMa(maKhuyenMai);
        if (khuyenMai == null) {
            System.out.println("Không tìm thấy khuyến mãi " + maKhuyenMai + ".");
            return false;
        }

        int luaChon;
        do {
            System.out.println("\n---------- SỬA KHUYẾN MÃI " + khuyenMai.getMaKhuyenMai() + " ----------");
            System.out.println("1. Đổi tên khuyến mãi");
            System.out.println("2. Đổi phần trăm giảm");
            System.out.println("3. Đổi giá trị hóa đơn tối thiểu");
            System.out.println("4. Đổi số tiền giảm tối đa");
            System.out.println("5. Đổi ngày bắt đầu");
            System.out.println("6. Đổi ngày kết thúc");
            System.out.println("0. Quay lại");
            luaChon = InputHelper.getIntInRange("Chọn chức năng: ", 0, 6);
            try {
                switch (luaChon) {
                    case 1:
                        khuyenMai.setTenKhuyenMai(InputHelper.getString("Nhập tên mới: "));
                        break;
                    case 2:
                        khuyenMai.setPhanTramGiam(docSoThuc("Nhập phần trăm giảm (0-100): ", 0, 100));
                        break;
                    case 3:
                        khuyenMai.setDieuKienToiThieu(docSoThuc("Nhập giá trị hóa đơn tối thiểu: ", 0, Double.MAX_VALUE));
                        break;
                    case 4:
                        khuyenMai.setTienGiamToiDa(docSoThuc("Nhập số tiền giảm tối đa: ", 0, Double.MAX_VALUE));
                        break;
                    case 5:
                        khuyenMai.setNgayBatDau(HoaDon.dinhDangNgay(docNgay("Nhập ngày bắt đầu (dd/MM/yyyy): ")));
                        break;
                    case 6:
                        khuyenMai.setNgayKetThuc(HoaDon.dinhDangNgay(docNgay("Nhập ngày kết thúc (dd/MM/yyyy): ")));
                        break;
                    default:
                        break;
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Không thể cập nhật: " + e.getMessage());
            }
        } while (luaChon != 0);
        return true;
    }

    public boolean xoaKhuyenMai(String maKhuyenMai) {
        int viTri = timViTriKhuyenMai(maKhuyenMai);
        if (viTri < 0) {
            System.out.println("Không tìm thấy khuyến mãi " + maKhuyenMai + ".");
            return false;
        }
        for (int i = 0; i < soLuongHoaDon; i++) {
            if (maKhuyenMai.equalsIgnoreCase(danhSachHoaDon[i].getMaKhuyenMai())) {
                System.out.println("Khuyến mãi đang được sử dụng trong hóa đơn "
                        + danhSachHoaDon[i].getMaHoaDon() + ", không thể xóa.");
                return false;
            }
        }
        for (int i = viTri; i < soLuongKhuyenMai - 1; i++) {
            danhSachKhuyenMai[i] = danhSachKhuyenMai[i + 1];
        }
        danhSachKhuyenMai[--soLuongKhuyenMai] = null;
        return true;
    }

    public KhuyenMai timKhuyenMaiTheoMa(String maKhuyenMai) {
        int viTri = timViTriKhuyenMai(maKhuyenMai);
        return viTri < 0 ? null : danhSachKhuyenMai[viTri];
    }

    public KhuyenMai[] timKiemKhuyenMai(String tuKhoa) {
        String tuKhoaChuanHoa = chuanHoa(tuKhoa);
        List<KhuyenMai> ketQua = new ArrayList<>();
        for (int i = 0; i < soLuongKhuyenMai; i++) {
            KhuyenMai khuyenMai = danhSachKhuyenMai[i];
            if (tuKhoaChuanHoa.isEmpty()
                    || chuanHoa(khuyenMai.getMaKhuyenMai()).contains(tuKhoaChuanHoa)
                    || chuanHoa(khuyenMai.getTenKhuyenMai()).contains(tuKhoaChuanHoa)) {
                ketQua.add(khuyenMai);
            }
        }
        return ketQua.toArray(new KhuyenMai[0]);
    }

    public boolean apDungKhuyenMai(String maHoaDon, String maKhuyenMai) {
        HoaDon hoaDon = timHoaDonTheoMa(maHoaDon);
        KhuyenMai khuyenMai = timKhuyenMaiTheoMa(maKhuyenMai);
        if (hoaDon == null) {
            System.out.println("Không tìm thấy hóa đơn " + maHoaDon + ".");
            return false;
        }
        if (khuyenMai == null) {
            System.out.println("Không tìm thấy khuyến mãi " + maKhuyenMai + ".");
            return false;
        }
        try {
            hoaDon.apDungKhuyenMai(khuyenMai);
            System.out.println("Đã áp dụng khuyến mãi " + maKhuyenMai + " cho hóa đơn " + maHoaDon + ".");
            return true;
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Không thể áp dụng khuyến mãi: " + e.getMessage());
            return false;
        }
    }

    public boolean apDungKhuyenMaiTotNhat(String maHoaDon) {
        HoaDon hoaDon = timHoaDonTheoMa(maHoaDon);
        if (hoaDon == null) {
            System.out.println("Không tìm thấy hóa đơn " + maHoaDon + ".");
            return false;
        }
        KhuyenMai totNhat = null;
        double tienGiamCaoNhat = 0;
        double tongTruocGiam = hoaDon.getTienPhong() + hoaDon.getTienDichVu();
        for (int i = 0; i < soLuongKhuyenMai; i++) {
            KhuyenMai khuyenMai = danhSachKhuyenMai[i];
            if (khuyenMai.kiemTraHieuLuc(hoaDon.getNgayLap())
                    && tongTruocGiam >= khuyenMai.getDieuKienToiThieu()) {
                double tienGiam = khuyenMai.tinhTienGiam(tongTruocGiam);
                if (tienGiam > tienGiamCaoNhat) {
                    tienGiamCaoNhat = tienGiam;
                    totNhat = khuyenMai;
                }
            }
        }
        if (totNhat == null) {
            System.out.println("Không có khuyến mãi phù hợp với hóa đơn " + maHoaDon + ".");
            return false;
        }
        return apDungKhuyenMai(maHoaDon, totNhat.getMaKhuyenMai());
    }

    public void luuDuLieu() {
        luuDanhSachHoaDon(TEN_FILE_HOA_DON);
        luuDanhSachKhuyenMai(TEN_FILE_KHUYEN_MAI);
    }

    public void docDuLieu() {
        docDanhSachKhuyenMai(TEN_FILE_KHUYEN_MAI);
        docDanhSachHoaDon(TEN_FILE_HOA_DON);
    }

    public void luuDanhSachKhuyenMai(String tenFile) {
        String[] duLieu = new String[soLuongKhuyenMai];
        for (int i = 0; i < soLuongKhuyenMai; i++) {
            duLieu[i] = danhSachKhuyenMai[i].toCSV();
        }
        FileHandler.writeLinesToFile(tenFile, duLieu, false);
    }

    public void docDanhSachKhuyenMai(String tenFile) {
        String[] cacDong = FileHandler.readLinesFromFile(tenFile);
        danhSachKhuyenMai = new KhuyenMai[Math.max(DUNG_LUONG_MAC_DINH, cacDong.length)];
        soLuongKhuyenMai = 0;
        for (int i = 0; i < cacDong.length; i++) {
            if (cacDong[i] == null || cacDong[i].trim().isEmpty() || cacDong[i].trim().startsWith("#")) {
                continue;
            }
            try {
                KhuyenMai khuyenMai = KhuyenMai.tuCSV(cacDong[i]);
                if (!themKhuyenMai(khuyenMai)) {
                    System.out.println("Dòng " + (i + 1) + ": mã khuyến mãi bị trùng, đã bỏ qua.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Dòng " + (i + 1) + " không hợp lệ: " + e.getMessage());
            }
        }
    }

    public void luuDanhSachHoaDon(String tenFile) {
        List<String> duLieu = new ArrayList<>();
        for (int i = 0; i < soLuongHoaDon; i++) {
            HoaDon hoaDon = danhSachHoaDon[i];
            duLieu.add(hoaDon.toCSV());
            for (int j = 0; j < hoaDon.getSoChiTiet(); j++) {
                duLieu.add(hoaDon.getChiTiet(j).toCSV());
            }
            for (int j = 0; j < hoaDon.getSoThanhToan(); j++) {
                duLieu.add(hoaDon.getThanhToan(j).toCSV());
            }
        }
        FileHandler.writeLinesToFile(tenFile, duLieu.toArray(new String[0]), false);
    }

    public void docDanhSachHoaDon(String tenFile) {
        String[] cacDong = FileHandler.readLinesFromFile(tenFile);
        danhSachHoaDon = new HoaDon[Math.max(DUNG_LUONG_MAC_DINH, cacDong.length)];
        soLuongHoaDon = 0;
        int viTri = 0;
        while (viTri < cacDong.length) {
            if (cacDong[viTri] == null || cacDong[viTri].trim().isEmpty()
                    || cacDong[viTri].trim().startsWith("#")) {
                viTri++;
                continue;
            }
            if (!cacDong[viTri].startsWith("HoaDon,")) {
                System.out.println("Bỏ qua dòng " + (viTri + 1) + " vì không phải dòng mở đầu hóa đơn.");
                viTri++;
                continue;
            }
            int dongKetThuc = viTri + 1;
            while (dongKetThuc < cacDong.length
                    && (cacDong[dongKetThuc] == null || !cacDong[dongKetThuc].startsWith("HoaDon,"))) {
                dongKetThuc++;
            }
            try {
                HoaDon hoaDon = HoaDon.tuCacDongCSV(cacDong, viTri, dongKetThuc);
                if (!themHoaDon(hoaDon)) {
                    System.out.println("Dòng " + (viTri + 1) + ": mã hóa đơn bị trùng, đã bỏ qua.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Không thể đọc hóa đơn tại dòng " + (viTri + 1) + ": " + e.getMessage());
            }
            viTri = dongKetThuc;
        }
        noiKhuyenMaiVaoHoaDon();
    }

    private void noiKhuyenMaiVaoHoaDon() {
        for (int i = 0; i < soLuongHoaDon; i++) {
            String maKhuyenMai = danhSachHoaDon[i].getMaKhuyenMai();
            if (maKhuyenMai == null || maKhuyenMai.trim().isEmpty()) {
                continue;
            }
            KhuyenMai khuyenMai = timKhuyenMaiTheoMa(maKhuyenMai);
            if (khuyenMai != null) {
                danhSachHoaDon[i].noiKhuyenMai(khuyenMai);
            }
        }
    }

    private HoaDon nhapThongTinHoaDon(String maNhanVien, String tenNhanVien) {
        if (maNhanVien == null || maNhanVien.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã nhân viên lập hóa đơn không được để trống.");
        }
        if (tenNhanVien == null || tenNhanVien.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên nhân viên lập hóa đơn không được để trống.");
        }

        String maHoaDon = HoaDon.taoMaHoaDon();
        System.out.println("Mã hóa đơn tự động: " + maHoaDon);
        String maDatPhong = InputHelper.getString("Nhập mã đặt phòng: ");
        LocalDate ngayLap = docNgay("Nhập ngày lập hóa đơn (dd/MM/yyyy): ");
        LocalDate ngayNhan = docNgay("Nhập ngày nhận phòng (dd/MM/yyyy): ");
        LocalDate ngayTra = docNgay("Nhập ngày trả phòng (dd/MM/yyyy): ");

        HoaDon hoaDon = new HoaDon(maHoaDon, maDatPhong, maNhanVien.trim(), tenNhanVien.trim(),
                ngayLap, ngayNhan, ngayTra);

        int soChiTiet = InputHelper.getIntInRange("Nhập số chi tiết hóa đơn: ", 1, 1000);
        for (int i = 0; i < soChiTiet; i++) {
            System.out.println("\n--- Chi tiết " + (i + 1) + "/" + soChiTiet + " ---");
            hoaDon.themChiTiet(nhapChiTietHoaDon(hoaDon.taoMaChiTiet()));
        }

        if (soLuongKhuyenMai > 0 && docCoKhong("Bạn có muốn áp dụng khuyến mãi không? (C/K): ")) {
            String maKhuyenMai = InputHelper.getString("Nhập mã khuyến mãi: ");
            KhuyenMai khuyenMai = timKhuyenMaiTheoMa(maKhuyenMai);
            if (khuyenMai != null) {
                try {
                    hoaDon.apDungKhuyenMai(khuyenMai);
                } catch (IllegalArgumentException | IllegalStateException e) {
                    System.out.println("Không áp dụng được khuyến mãi: " + e.getMessage());
                }
            } else {
                System.out.println("Không tìm thấy mã khuyến mãi. Hóa đơn được tạo mà không có khuyến mãi.");
            }
        }
        return hoaDon;
    }

    private ChiTietHoaDon nhapChiTietHoaDon(String maChiTiet) {
        int loai = InputHelper.getIntInRange("Loại chi tiết (1 - Tiền phòng, 2 - Dịch vụ): ", 1, 2);
        String loaiChiTiet = loai == 1 ? ChiTietHoaDon.LOAI_TIEN_PHONG : ChiTietHoaDon.LOAI_DICH_VU;
        String maDichVu = null;
        String tenDichVu = null;
        if (loai == 2) {
            maDichVu = InputHelper.getString("Nhập mã dịch vụ: ");
            tenDichVu = InputHelper.getString("Nhập tên dịch vụ: ");
        }

        String moTa = nhapChuoiKhongBatBuoc("Nhập mô tả (có thể bỏ qua bằng dấu -): ");
        int thoiDiem = InputHelper.getIntInRange(
                "Thời điểm thanh toán (1 - Khi sử dụng, 2 - Khi trả phòng): ", 1, 2);
        ThoiDiemThanhToan thoiDiemThanhToan = thoiDiem == 1
                ? ThoiDiemThanhToan.NGAY_SU_DUNG : ThoiDiemThanhToan.KHI_TRA_PHONG;
        int soLuong = InputHelper.getIntInRange(loai == 1 ? "Số đêm: " : "Số lượng: ", 1, 100000);
        double donGia = docSoThuc("Nhập đơn giá (VND): ", 0, Double.MAX_VALUE);
        double thueSuatVAT = docSoThuc("Nhập thuế suất VAT (%): ", 0, 100);

        return new ChiTietHoaDon(maChiTiet, loaiChiTiet, maDichVu, tenDichVu,
                moTa, thoiDiemThanhToan, soLuong, donGia, thueSuatVAT);
    }

    private KhuyenMai nhapThongTinKhuyenMai() {
        String ma = InputHelper.getString("Nhập mã khuyến mãi: ");
        String ten = InputHelper.getString("Nhập tên khuyến mãi: ");
        double phanTram = docSoThuc("Nhập phần trăm giảm (0-100): ", 0, 100);
        double dieuKien = docSoThuc("Nhập giá trị hóa đơn tối thiểu (VND): ", 0, Double.MAX_VALUE);
        double toiDa = docSoThuc("Nhập số tiền giảm tối đa (VND): ", 0, Double.MAX_VALUE);
        LocalDate ngayBatDau = docNgay("Nhập ngày bắt đầu (dd/MM/yyyy): ");
        LocalDate ngayKetThuc;
        do {
            ngayKetThuc = docNgay("Nhập ngày kết thúc (dd/MM/yyyy): ");
            if (ngayKetThuc.isBefore(ngayBatDau)) {
                System.out.println("Ngày kết thúc không được trước ngày bắt đầu. Vui lòng nhập lại.");
            }
        } while (ngayKetThuc.isBefore(ngayBatDau));
        return new KhuyenMai(ma, ten, phanTram, dieuKien, toiDa,
                HoaDon.dinhDangNgay(ngayBatDau), HoaDon.dinhDangNgay(ngayKetThuc));
    }

    private static LocalDate docNgay(String loiNhac) {
        while (true) {
            String chuoiNgay = InputHelper.getString(loiNhac);
            try {
                return HoaDon.docNgay(chuoiNgay);
            } catch (IllegalArgumentException | DateTimeParseException e) {
                System.out.println("Ngày không hợp lệ. Vui lòng nhập theo định dạng dd/MM/yyyy.");
            }
        }
    }

    private static double docSoThuc(String loiNhac, double nhoNhat, double lonNhat) {
        while (true) {
            double giaTri = InputHelper.getDouble(loiNhac);
            if (Double.isFinite(giaTri) && giaTri >= nhoNhat && giaTri <= lonNhat) {
                return giaTri;
            }
            System.out.println("Giá trị phải nằm trong khoảng " + nhoNhat + " đến " + lonNhat + ".");
        }
    }

    private static boolean docCoKhong(String loiNhac) {
        while (true) {
            String luaChon = InputHelper.getString(loiNhac).trim().toLowerCase();
            if (luaChon.equals("c") || luaChon.equals("co") || luaChon.equals("có")) {
                return true;
            }
            if (luaChon.equals("k") || luaChon.equals("khong") || luaChon.equals("không")) {
                return false;
            }
            System.out.println("Vui lòng chỉ nhập C (Có) hoặc K (Không).");
        }
    }

    private static String nhapChuoiKhongBatBuoc(String loiNhac) {
        String giaTri = InputHelper.getString(loiNhac);
        return "-".equals(giaTri) ? "" : giaTri;
    }

    private void moRongMangHoaDon() {
        if (soLuongHoaDon < danhSachHoaDon.length) {
            return;
        }
        HoaDon[] mangMoi = new HoaDon[danhSachHoaDon.length * 2];
        System.arraycopy(danhSachHoaDon, 0, mangMoi, 0, soLuongHoaDon);
        danhSachHoaDon = mangMoi;
    }

    private void moRongMangKhuyenMai() {
        if (soLuongKhuyenMai < danhSachKhuyenMai.length) {
            return;
        }
        KhuyenMai[] mangMoi = new KhuyenMai[danhSachKhuyenMai.length * 2];
        System.arraycopy(danhSachKhuyenMai, 0, mangMoi, 0, soLuongKhuyenMai);
        danhSachKhuyenMai = mangMoi;
    }

    private int timViTriHoaDon(String maHoaDon) {
        if (maHoaDon == null) {
            return -1;
        }
        for (int i = 0; i < soLuongHoaDon; i++) {
            if (danhSachHoaDon[i].getMaHoaDon().equalsIgnoreCase(maHoaDon.trim())) {
                return i;
            }
        }
        return -1;
    }

    private int timViTriKhuyenMai(String maKhuyenMai) {
        if (maKhuyenMai == null) {
            return -1;
        }
        for (int i = 0; i < soLuongKhuyenMai; i++) {
            if (danhSachKhuyenMai[i].getMaKhuyenMai().equalsIgnoreCase(maKhuyenMai.trim())) {
                return i;
            }
        }
        return -1;
    }

    private void doiChoHoaDon(int viTriA, int viTriB) {
        HoaDon tam = danhSachHoaDon[viTriA];
        danhSachHoaDon[viTriA] = danhSachHoaDon[viTriB];
        danhSachHoaDon[viTriB] = tam;
    }

    private boolean hoaDonPhuHopTuKhoa(HoaDon hoaDon, String tuKhoa) {
        if (chuanHoa(hoaDon.getMaHoaDon()).contains(tuKhoa)
                || chuanHoa(hoaDon.getMaDatPhong()).contains(tuKhoa)
                || chuanHoa(hoaDon.getMaNguoiLap()).contains(tuKhoa)
                || chuanHoa(hoaDon.getTenNguoiLap()).contains(tuKhoa)
                || chuanHoa(hoaDon.getTrangThaiChu()).contains(tuKhoa)) {
            return true;
        }
        for (int i = 0; i < hoaDon.getSoChiTiet(); i++) {
            ChiTietHoaDon chiTiet = hoaDon.getChiTiet(i);
            if (chuanHoa(chiTiet.getMaChiTiet()).contains(tuKhoa)
                    || chuanHoa(chiTiet.getTenDichVu()).contains(tuKhoa)
                    || chuanHoa(chiTiet.getMoTa()).contains(tuKhoa)) {
                return true;
            }
        }
        return false;
    }

    private static String chuanHoa(String chuoi) {
        if (chuoi == null) {
            return "";
        }
        String khongDau = Normalizer.normalize(chuoi, Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "");
        return khongDau.toLowerCase().trim();
    }

    private static void inBangHoaDon(HoaDon[] danhSach, int soLuong, String tieuDe) {
        System.out.println("\n================ " + tieuDe + " (" + soLuong + ") ================");
        if (soLuong == 0) {
            System.out.println("Danh sách chưa có dữ liệu.");
            return;
        }
        System.out.printf("%-4s %-12s %-14s %-14s %-12s %16s %-18s%n",
                "STT", "Mã hóa đơn", "Mã đặt phòng", "Ngày lập", "Trạng thái", "Tổng tiền", "Nhân viên lập");
        for (int i = 0; i < soLuong; i++) {
            HoaDon hoaDon = danhSach[i];
            System.out.printf("%-4d %-12s %-14s %-14s %-12s %,16.0f %-18.18s%n",
                    i + 1, hoaDon.getMaHoaDon(), hoaDon.getMaDatPhong(),
                    HoaDon.dinhDangNgay(hoaDon.getNgayLap()), hoaDon.getTrangThaiChu(),
                    hoaDon.getTongTien(), hoaDon.getTenNguoiLap());
        }
        System.out.println("==========================================================================");
    }

    private static void inBangKhuyenMai(KhuyenMai[] danhSach, int soLuong, String tieuDe) {
        System.out.println("\n================ " + tieuDe + " (" + soLuong + ") ================");
        if (soLuong == 0) {
            System.out.println("Danh sách chưa có dữ liệu.");
            return;
        }
        System.out.printf("%-4s %-14s %-24s %10s %18s %18s %-12s %-12s%n",
                "STT", "Mã KM", "Tên khuyến mãi", "% giảm", "Đơn tối thiểu", "Giảm tối đa", "Bắt đầu", "Kết thúc");
        for (int i = 0; i < soLuong; i++) {
            KhuyenMai khuyenMai = danhSach[i];
            System.out.printf("%-4d %-14s %-24.24s %9.1f%% %,18.0f %,18.0f %-12s %-12s%n",
                    i + 1, khuyenMai.getMaKhuyenMai(), khuyenMai.getTenKhuyenMai(),
                    khuyenMai.getPhanTramGiam(), khuyenMai.getDieuKienToiThieu(),
                    khuyenMai.getTienGiamToiDa(), khuyenMai.getNgayBatDau(), khuyenMai.getNgayKetThuc());
        }
        System.out.println("===============================================================================================");
    }
}
