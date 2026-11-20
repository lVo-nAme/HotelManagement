package management;

import bill.HoaDon;
import bill.KhuyenMai;
import java.time.LocalDate;
import java.util.Scanner;
import payment.IThanhToan;
import payment.ThanhToanChuyenKhoan;
import payment.ThanhToanThe;
import payment.ThanhToanTienMat;
import person.QuanLyNguoi;
import util.InputHelper;


public class MenuChinh {
    private static final String FILE_PHONG = "data_phong.txt";
    private final QuanLyNguoi quanLyNguoi;
    private final QuanLyPhong quanLyPhong;
    private final QuanLyHoaDon quanLyHoaDon;
  // Constructor
    public MenuChinh() {
        this.quanLyNguoi = new QuanLyNguoi();
        this.quanLyPhong = new QuanLyPhong();
        this.quanLyHoaDon = new QuanLyHoaDon();
    }

    public void chay() {
        while (true) {
            int chon = hienMenu("MENU CHÍNH - QUẢN LÝ KHÁCH SẠN", 
                                "Thoát chương trình",
                                "Quản lý người", 
                                "Quản lý phòng",
                                "Quản lý dịch vụ", 
                                "Thanh toán", 
                                "Hóa đơn");
            if (chon == 6) {
                if (xacNhanThoat()) {
                    break;
                }
                continue;
            }
            try {
                switch (chon) {
                    case 1:
                        menuNguoi();
                        break;
                    case 2:
                        menuPhong();
                        break;
                    case 3:
                        menuDichVu();
                        break;
                    case 4:
                        menuThanhToan();
                        break;
                    case 5:
                        menuHoaDon();
                        break;
                    default:
                        break;
                }
            } catch (RuntimeException e) {
                baoLoi(e);
            }
        }
        System.out.println("\n=> Đã thoát chương trình.");
    }

    private boolean xacNhanThoat() {
        System.out.println("=> Dữ liệu chưa ghi ra file sẽ không được lưu.");
        int chon = InputHelper.getIntInRange("Xác nhận thoát (1 - Thoát, 2 - Ở lại): ", 1, 2);
        return chon == 1;
    }

    private void menuNguoi() {
        int chon;
        do {
            chon = hienMenu("QUẢN LÝ NGƯỜI",
                            "Quay lại menu chính",
                            "Quản lý", "Nhân viên",
                            "Khách hàng");
            try {
                switch (chon) {
                    case 1:
                        quanLyNguoi.xuLyChucVuQuanLy();
                        break;
                    case 2:
                        quanLyNguoi.xuLyChucVuNhanVien();
                        break;
                    case 3:
                        quanLyNguoi.xuLyChucVuKhachHang();
                        break;
                    default:
                        break;
                }
            } catch (RuntimeException e) {
                baoLoi(e);
            }
        } while (chon != 4);
    }

    private void menuPhong() {
        int chon;
        do {
            chon = hienMenu("QUẢN LÝ PHÒNG", "Quay lại menu chính",
                    "Xem danh sách phòng và tình trạng phòng",
                    "Thêm phòng",
                    "Sửa thông tin phòng theo mã",
                    "Xóa phòng theo mã",
                    "Tìm kiếm phòng",
                    "Thống kê phòng",
                    "Tính tổng tiền dự kiến",
                    "Ghi dữ liệu phòng ra file",
                    "Đọc dữ liệu phòng từ file");
            try {
                switch (chon) {
                    case 1:
                        quanLyPhong.xuatDanhSach();
                        break;
                    case 2:    
                        quanLyPhong.themPhong(new Scanner(System.in));
                        break;
                    case 3:
                        quanLyPhong.suaPhong(InputHelper.getString("Nhập mã phòng cần sửa: "), new Scanner(System.in));
                        break;
                    case 4:
                        quanLyPhong.xoaPhong(InputHelper.getString("Nhập mã phòng cần xóa: "));
                        break;
                    case 5:
                        quanLyPhong.timKiem(InputHelper.getString("Nhập từ khóa tìm kiếm: "));
                        break;
                    case 6:
                        quanLyPhong.thongKe();
                        break;
                    case 7:
                        quanLyPhong.tinhTongTienDuKien();
                        break;
                    case 8:
                        quanLyPhong.ghiFile(FILE_PHONG);
                        break;
                    case 9:
                        quanLyPhong.docFile(FILE_PHONG);
                        break;
                    default:
                        break;
                }
            } catch (RuntimeException e) {
                baoLoi(e);
            }
        } while (chon != 10);
    }

    private void menuDichVu() {
        System.out.println("");

    private void menuThanhToan() {
        int chon;
        do {
            chon = hienMenu("THANH TOÁN", "Quay lại menu chính",
                    "Xem các hóa đơn chưa thanh toán",
                    "Thanh toán một hóa đơn",
                    "Xem chi tiết và các giao dịch của một hóa đơn");
            try {
                switch (chon) {
                    case 1:
                        QuanLyHoaDon.hienThiKetQuaHoaDon(quanLyHoaDon.layDanhSachHoaDonChuaThanhToan());
                        break;
                    case 2:
                        thanhToanMotHoaDon();
                        break;
                    case 3:
                        quanLyHoaDon.hienThiChiTietHoaDon(InputHelper.getString("Nhập mã hóa đơn: "));
                        break;
                    default:
                        break;
                }
            } catch (RuntimeException e) {
                baoLoi(e);
            }
        } while (chon != 4);
    }

    // Hiển thị hóa đơn, hỏi phương thức và thông tin của nó, rồi gọi QuanLyHoaDon.thanhToanHoaDon(...)
    private void thanhToanMotHoaDon() {
        String maHoaDon = InputHelper.getString("Nhập mã hóa đơn cần thanh toán: ");
        if (!quanLyHoaDon.hienThiChiTietHoaDon(maHoaDon)) {
            return;
        }
        IThanhToan phuongThuc = chonPhuongThuc();
        if (quanLyHoaDon.thanhToanHoaDon(maHoaDon, phuongThuc)) {
            System.out.println("=> Thanh toán thành công.");
        } else {
            System.out.println("=> Thanh toán không thành công (xem thông báo phía trên).");
        }
    }

    private IThanhToan chonPhuongThuc() {
        int loai = InputHelper.getIntInRange("Chọn phương thức (1 - Tiền mặt, 2 - Chuyển khoản, 3 - Thẻ): ", 1, 3);
        if (loai == 1) {
            return new ThanhToanTienMat(InputHelper.getDouble("Số tiền khách đưa (VND): "));
        }
        if (loai == 2) {
            String nganHang = InputHelper.getString("Tên ngân hàng: ");
            String maGiaoDich = InputHelper.getString("Mã giao dịch: ");
            return new ThanhToanChuyenKhoan(nganHang, maGiaoDich);
        }
        String loaiThe = InputHelper.getString("Loại thẻ: ");
        String soThe = InputHelper.getString("Số thẻ rút gọn: ");
        String maGiaoDich = InputHelper.getString("Mã giao dịch: ");
        return new ThanhToanThe(loaiThe, soThe, maGiaoDich);
    }

    private void menuHoaDon() {
        int chon;
        do {
            chon = hienMenu("HÓA ĐƠN", "Quay lại menu chính",
                    "Quản lý hóa đơn",
                    "Quản lý khuyến mãi",
                    "Thống kê doanh thu",
                    "Ghi / đọc file hóa đơn và khuyến mãi");
            try {
                switch (chon) {
                    case 1:
                        menuQuanLyHoaDon();
                        break;
                    case 2:
                        menuKhuyenMai();
                        break;
                    case 3:
                        menuThongKe();
                        break;
                    case 4:
                        menuFileHoaDon();
                        break;
                    default:
                        break;
                }
            } catch (RuntimeException e) {
                baoLoi(e);
            }
        } while (chon != 5);
    }

    private void menuQuanLyHoaDon() {
        int chon;
        do {
            chon = hienMenu("QUẢN LÝ HÓA ĐƠN", "Quay lại menu hóa đơn",
                    "Xem danh sách hóa đơn",
                    "Xem chi tiết một hóa đơn",
                    "Lập hóa đơn mới",
                    "Sửa hóa đơn",
                    "Xóa hóa đơn",
                    "Tìm kiếm hóa đơn",
                    "Xem các hóa đơn chưa thanh toán",
                    "Sắp xếp danh sách hóa đơn");
            try {
                switch (chon) {
                    case 1:
                        quanLyHoaDon.hienThiDanhSachHoaDon();
                        break;
                    case 2:
                        quanLyHoaDon.hienThiChiTietHoaDon(InputHelper.getString("Nhập mã hóa đơn: "));
                        break;
                    case 3:
                        lapHoaDon();
                        break;
                    case 4:
                        quanLyHoaDon.suaHoaDon(InputHelper.getString("Nhập mã hóa đơn cần sửa: "));
                        break;
                    case 5:
                        if (quanLyHoaDon.xoaHoaDon(InputHelper.getString("Nhập mã hóa đơn cần xóa: "))) {
                            System.out.println("=> Đã xóa hóa đơn.");
                        }
                        break;
                    case 6:
                        menuTimKiemHoaDon();
                        break;
                    case 7:
                        QuanLyHoaDon.hienThiKetQuaHoaDon(quanLyHoaDon.layDanhSachHoaDonChuaThanhToan());
                        break;
                    case 8:
                        menuSapXepHoaDon();
                        break;
                    default:
                        break;
                }
            } catch (RuntimeException e) {
                baoLoi(e);
            }
        } while (chon != 9);
    }

    // quản lý hóa đơn cần số lượng, mã và tên nhân viên lập; không có hàm tra cứu nhân viên nên cần nhập hai giá trị này.
    private void lapHoaDon() {
        int soLuong = InputHelper.getIntInRange("Số hóa đơn cần lập: ", 1, 100);
        String maNhanVien = InputHelper.getString("Mã nhân viên lập hóa đơn: ");
        String tenNhanVien = InputHelper.getString("Tên nhân viên lập hóa đơn: ");
        quanLyHoaDon.nhapDanhSachHoaDon(soLuong, maNhanVien, tenNhanVien);
    }

    private void menuTimKiemHoaDon() {
        int chon;
        do {
            chon = hienMenu("TÌM KIẾM HÓA ĐƠN", "Quay lại",
                    "Theo mã hóa đơn",
                    "Theo mã đặt phòng",
                    "Theo từ khóa (gần đúng)",
                    "Tìm nhiều khóa (trạng thái, ngày lập, tổng tiền)");
            try {
                switch (chon) {
                    case 1:
                        hienMotHoaDon(quanLyHoaDon.timHoaDonTheoMa(InputHelper.getString("Nhập mã hóa đơn: ")));
                        break;
                    case 2:
                        hienMotHoaDon(quanLyHoaDon.timHoaDonTheoMaDatPhong(InputHelper.getString("Nhập mã đặt phòng: ")));
                        break;
                    case 3:
                        QuanLyHoaDon.hienThiKetQuaHoaDon(quanLyHoaDon.timKiemHoaDon(InputHelper.getString("Nhập từ khóa: ")));
                        break;
                    case 4:
                        timHoaDonNhieuKhoa();
                        break;
                    default:
                        break;
                }
            } catch (RuntimeException e) {
                baoLoi(e);
            }
        } while (chon != 5);
    }

    private void hienMotHoaDon(HoaDon hoaDon) {
        if (hoaDon == null) {
            System.out.println("=> Không tìm thấy hóa đơn.");
        } else {
            hoaDon.hienThiHoaDon();
        }
    }

    // Hỏi từng khóa 
    private void timHoaDonNhieuKhoa() {
        String tuKhoa = docTuyChon("Từ khóa (mã hóa đơn, mã đặt phòng, nhân viên...)");
        int tt = InputHelper.getIntInRange("Trạng thái (1 - Tất cả, 2 - Chưa thanh toán, 3 - Đã thanh toán): ", 1, 3);
        String trangThai = (tt == 2) ? HoaDon.CHUA_THANH_TOAN : (tt == 3 ? HoaDon.DA_THANH_TOAN : null);
        LocalDate tuNgay = docNgayTuyChon("Từ ngày lập (dd/MM/yyyy)");
        LocalDate denNgay = docNgayTuyChon("Đến ngày lập (dd/MM/yyyy)");
        double tienToiThieu = InputHelper.getDouble("Tổng tiền tối thiểu (nhập -1 nếu bỏ qua): ");
        double tienToiDa = InputHelper.getDouble("Tổng tiền tối đa (nhập -1 nếu bỏ qua): ");
        QuanLyHoaDon.hienThiKetQuaHoaDon(
                quanLyHoaDon.timKiemHoaDon(tuKhoa, trangThai, tuNgay, denNgay, tienToiThieu, tienToiDa));
    }

    private void menuSapXepHoaDon() {
        int chon = hienMenu("SẮP XẾP HÓA ĐƠN", "Quay lại",
                "Theo tổng tiền tăng dần",
                "Theo tổng tiền giảm dần",
                "Theo ngày lập (cũ trước, mới sau)");
        if (chon == 1) {
            quanLyHoaDon.sapXepHoaDonTheoTongTien(true);
        } else if (chon == 2) {
            quanLyHoaDon.sapXepHoaDonTheoTongTien(false);
        } else if (chon == 3) {
            quanLyHoaDon.sapXepHoaDonTheoNgayLap();
        } else {
            return;
        }
        quanLyHoaDon.hienThiDanhSachHoaDon();
    }

    private void menuKhuyenMai() {
        int chon;
        do {
            chon = hienMenu("QUẢN LÝ KHUYẾN MÃI", "Quay lại menu hóa đơn",
                    "Xem danh sách khuyến mãi",
                    "Thêm khuyến mãi",
                    "Sửa khuyến mãi",
                    "Xóa khuyến mãi",
                    "Tìm kiếm khuyến mãi",
                    "Áp dụng khuyến mãi cho một hóa đơn",
                    "Tự chọn khuyến mãi tốt nhất cho một hóa đơn");
            try {
                switch (chon) {
                    case 1:
                        quanLyHoaDon.hienThiDanhSachKhuyenMai();
                        break;
                    case 2:
                        quanLyHoaDon.nhapDanhSachKhuyenMai(InputHelper.getIntInRange("Số khuyến mãi cần nhập: ", 1, 100));
                        break;
                    case 3:
                        quanLyHoaDon.suaKhuyenMai(InputHelper.getString("Nhập mã khuyến mãi cần sửa: "));
                        break;
                    case 4:
                        if (quanLyHoaDon.xoaKhuyenMai(InputHelper.getString("Nhập mã khuyến mãi cần xóa: "))) {
                            System.out.println("=> Đã xóa khuyến mãi.");
                        }
                        break;
                    case 5:
                        timKhuyenMai();
                        break;
                    case 6:
                        String maHoaDon = InputHelper.getString("Nhập mã hóa đơn: ");
                        String maKhuyenMai = InputHelper.getString("Nhập mã khuyến mãi: ");
                        quanLyHoaDon.apDungKhuyenMai(maHoaDon, maKhuyenMai);
                        break;
                    case 7:
                        quanLyHoaDon.apDungKhuyenMaiTotNhat(InputHelper.getString("Nhập mã hóa đơn: "));
                        break;
                    default:
                        break;
                }
            } catch (RuntimeException e) {
                baoLoi(e);
            }
        } while (chon != 8);
    }

    private void timKhuyenMai() {
        KhuyenMai[] ketQua = quanLyHoaDon.timKiemKhuyenMai(InputHelper.getString("Nhập từ khóa: "));
        if (ketQua.length == 0) {
            System.out.println("=> Không tìm thấy khuyến mãi phù hợp.");
            return;
        }
        System.out.println("=> Tìm thấy " + ketQua.length + " khuyến mãi:");
        for (int i = 0; i < ketQua.length; i++) {
            ketQua[i].hienThiThongTin();
        }
    }

    private void menuThongKe() {
        int chon;
        do {
            chon = hienMenu("THỐNG KÊ DOANH THU", "Quay lại menu hóa đơn",
                    "Tổng doanh thu",
                    "Doanh thu theo khoảng ngày lập",
                    "Doanh thu theo tháng của một năm",
                    "Thống kê theo trạng thái hóa đơn",
                    "Thống kê theo nhân viên lập hóa đơn",
                    "Thống kê tiền phòng và dịch vụ",
                    "Thống kê theo phương thức thanh toán");
            try {
                switch (chon) {
                    case 1:
                        System.out.printf("=> Tổng doanh thu (hóa đơn đã thanh toán): %,.0f VND%n",
                                quanLyHoaDon.tinhTongDoanhThu());
                        break;
                    case 2:
                        LocalDate tuNgay = docNgayTuyChon("Từ ngày lập (dd/MM/yyyy)");
                        LocalDate denNgay = docNgayTuyChon("Đến ngày lập (dd/MM/yyyy)");
                        System.out.printf("=> Doanh thu trong khoảng ngày: %,.0f VND%n",
                                quanLyHoaDon.tinhTongDoanhThu(tuNgay, denNgay));
                        break;
                    case 3:
                        quanLyHoaDon.thongKeDoanhThuTheoThang(InputHelper.getIntInRange("Nhập năm: ", 1900, 2100));
                        break;
                    case 4:
                        quanLyHoaDon.thongKeTheoTrangThai();
                        break;
                    case 5:
                        quanLyHoaDon.thongKeTheoNhanVien();
                        break;
                    case 6:
                        quanLyHoaDon.thongKeTienPhongVaDichVu();
                        break;
                    case 7:
                        quanLyHoaDon.thongKeTheoPhuongThucThanhToan();
                        break;
                    default:
                        break;
                }
            } catch (RuntimeException e) {
                baoLoi(e);
            }
        } while (chon != 8);
    }

    private void menuFileHoaDon() {
        int chon;
        do {
            chon = hienMenu("GHI / ĐỌC FILE HÓA ĐƠN VÀ KHUYẾN MÃI", "Quay lại menu hóa đơn",
                    "Ghi dữ liệu ra file",
                    "Đọc dữ liệu từ file");
            try {
                if (chon == 1) {
                    quanLyHoaDon.luuDuLieu();
                } else if (chon == 2) {
                    quanLyHoaDon.docDuLieu();
                }
            } catch (RuntimeException e) {
                baoLoi(e);
            }
        } while (chon != 3);
    }

    private static int hienMenu(String tieuDe, String nhanQuayLai, String... cacMuc) {
        System.out.println("\n========== " + tieuDe + " ==========");
        for (int i = 0; i < cacMuc.length; i++) {
            System.out.println((i + 1) + ". " + cacMuc[i]);
        }
        System.out.println((cacMuc.length + 1) + ". " + nhanQuayLai);
        return InputHelper.getIntInRange("Chọn chức năng: ", 1, cacMuc.length + 1);
    }

    // InputHelper.getString không cho bỏ trống, nên với khóa tuỳ chọn thì gõ "-" để bỏ qua
    private static String docTuyChon(String nhan) {
        String s = InputHelper.getString(nhan + " (nhập - nếu bỏ qua): ");
        return s.equals("-") ? "" : s;
    }

    private static LocalDate docNgayTuyChon(String nhan) {
        while (true) {
            String s = InputHelper.getString(nhan + " (nhập - nếu bỏ qua): ");
            if (s.equals("-")) {
                return null;
            }
            try {
                return HoaDon.docNgay(s);
            } catch (IllegalArgumentException e) {
                System.out.println("Lỗi: " + e.getMessage());
            }
        }
    }
}
