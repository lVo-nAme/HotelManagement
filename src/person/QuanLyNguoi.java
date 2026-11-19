package person;

import java.util.Scanner;
import management.CustomerList;

public class QuanLyNguoi {
    private static final int MAX = 100;

    // Danh sách quản lý nhân viên và khách hàng
    private NhanVien[] dsNhanVien;
    private int soLuongNhanVien;

    // Phân hệ quản lý khách hàng
    private CustomerList customerList;

    private Scanner sc;

    public QuanLyNguoi() {
        dsNhanVien = new NhanVien[MAX];
        soLuongNhanVien = 0;

        customerList = new CustomerList();

        sc = new Scanner(System.in);

        khoiTaoDuLieuMau();
    }

    /**
     * Khởi tạo dữ liệu mẫu ban đầu cho nhân sự
     */
    private void khoiTaoDuLieuMau() {
        dsNhanVien[soLuongNhanVien++] = new QuanLy("QL01", "Trần Anh Quân", "0901234567", "079099001234", "anhquan@hotel.com", "Quản lý điều hành", 20000000, "01/01/2020", 5000000, 5);
        dsNhanVien[soLuongNhanVien++] = new NhanVien("NV01", "Nguyễn Văn Hùng", "0987654321", "079098001111", "hungnv@hotel.com", "Lễ tân", 8000000, "10/05/2022");
        dsNhanVien[soLuongNhanVien++] = new NhanVien("NV02", "Phạm Thị Hương", "0978111222", "079098002222", "huongpt@hotel.com", "Kế toán", 9000000, "01/08/2022");
    }

    // =========================================================================
    // 1. HÀM KIỂM TRA CHỨC VỤ (Khi chạy Main, yêu cầu chọn Role)
    // =========================================================================
    public void kiemTraChucVu() {
        int chon = -1;
        do {
            System.out.println("\n========================================");
            System.out.println("         HỆ THỐNG QUẢN LÝ CON NGƯỜI     ");
            System.out.println("========================================");
            System.out.println("Vui lòng chọn vai trò (Role):");
            System.out.println("1. Quản lý");
            System.out.println("2. Nhân viên");
            System.out.println("3. Khách hàng");
            System.out.println("0. Thoát");
            System.out.println("----------------------------------------");
            System.out.print("Chọn chức vụ: ");

            try {
                chon = Integer.parseInt(sc.nextLine().trim());
                switch (chon) {
                    case 1:
                        xuLyChucVuQuanLy();
                        break;
                    case 2:
                        xuLyChucVuNhanVien();
                        break;
                    case 3:
                        xuLyChucVuKhachHang();
                        break;
                    case 0:
                        System.out.println("Tạm biệt! Đã thoát chương trình.");
                        break;
                    default:
                        System.out.println("Lựa chọn không hợp lệ, vui lòng chọn lại!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập vào một số nguyên!");
            }
        } while (chon != 0);
    }

    // =========================================================================
    // 2. CÁC TÍNH NĂNG QUẢN LÝ CON NGƯỜI:
    // =========================================================================

    /**
     * Tính năng 1: Thêm người (Nhân viên / Khách hàng / Quản lý)
     */
    public void themNguoi() {
        System.out.println("\n--- THÊM NGƯỜI MỚI ---");
        System.out.println("1. Thêm Quản lý");
        System.out.println("2. Thêm Nhân viên");
        System.out.println("3. Thêm Khách hàng vào CustomerList");
        System.out.print("Chọn loại người muốn thêm: ");
        int loai = -1;
        try {
            loai = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Lựa chọn không hợp lệ!");
            return;
        }

        if (loai == 1) {
            if (soLuongNhanVien >= MAX) {
                System.out.println("Danh sách nhân sự đã đầy!");
                return;
            }
            QuanLy ql = new QuanLy();
            ql.nhapThongTin();
            dsNhanVien[soLuongNhanVien++] = ql;
            System.out.println("=> Đã thêm Quản lý [" + ql.getHoTen() + "] thành công!");
        } else if (loai == 2) {
            if (soLuongNhanVien >= MAX) {
                System.out.println("Danh sách nhân sự đã đầy!");
                return;
            }
            NhanVien nv = new NhanVien();
            nv.nhapThongTin();
            dsNhanVien[soLuongNhanVien++] = nv;
            System.out.println("=> Đã thêm Nhân viên [" + nv.getHoTen() + "] thành công!");
        } else if (loai == 3) {
            KhachHang kh = new KhachHang();
            kh.nhapThongTin();
            customerList.luuCustomerKhiDatPhong(kh);
        } else {
            System.out.println("Lựa chọn loại người không hợp lệ!");
        }
    }

    /**
     * Tính năng 2: Sửa thông tin người theo Mã định danh
     */
    public void suaThongTin() {
        System.out.print("\nNhập mã người cần sửa thông tin (Mã NV/QL hoặc Mã KH): ");
        String ma = sc.nextLine().trim();

        // Tìm trong danh sách nhân viên / quản lý
        for (int i = 0; i < soLuongNhanVien; i++) {
            if (dsNhanVien[i].getMaNguoi().equalsIgnoreCase(ma)) {
                System.out.println("Tìm thấy nhân sự: " + dsNhanVien[i].getHoTen());
                System.out.print("Nhập họ tên mới: ");
                dsNhanVien[i].setHoTen(sc.nextLine());
                System.out.print("Nhập số điện thoại mới: ");
                dsNhanVien[i].setSoDienThoai(sc.nextLine());
                System.out.print("Nhập CCCD mới: ");
                dsNhanVien[i].setCccd(sc.nextLine());
                System.out.print("Nhập email mới: ");
                dsNhanVien[i].setEmail(sc.nextLine());
                System.out.print("Nhập chức vụ mới: ");
                dsNhanVien[i].setChucVu(sc.nextLine());
                System.out.print("Nhập mức lương mới (VNĐ): ");
                try {
                    dsNhanVien[i].setLuong(Double.parseDouble(sc.nextLine().trim()));
                } catch (NumberFormatException ignored) {}

                if (dsNhanVien[i] instanceof QuanLy) {
                    QuanLy ql = (QuanLy) dsNhanVien[i];
                    System.out.print("Nhập phụ cấp mới (VNĐ): ");
                    try {
                        ql.setPhuCap(Double.parseDouble(sc.nextLine().trim()));
                    } catch (NumberFormatException ignored) {}
                }
                System.out.println("=> Cập nhật thông tin nhân viên thành công!");
                return;
            }
        }

        // Tìm trong customerList
        KhachHang kh = customerList.timCustomer(ma);
        if (kh != null) {
            customerList.suaCustomer(ma, sc);
            return;
        }

        System.out.println("=> Không tìm thấy người có mã [" + ma + "]!");
    }

    /**
     * Tính năng 3: Xóa người theo Mã định danh
     */
    public void xoaNguoi() {
        System.out.print("\nNhập mã người cần xóa (Mã NV hoặc Mã KH): ");
        String ma = sc.nextLine().trim();

        // Kiểm tra xóa trong danh sách nhân viên
        for (int i = 0; i < soLuongNhanVien; i++) {
            if (dsNhanVien[i].getMaNguoi().equalsIgnoreCase(ma)) {
                for (int j = i; j < soLuongNhanVien - 1; j++) {
                    dsNhanVien[j] = dsNhanVien[j + 1];
                }
                dsNhanVien[--soLuongNhanVien] = null;
                System.out.println("=> Đã xóa nhân viên có mã [" + ma + "] thành công!");
                return;
            }
        }

        // Kiểm tra xóa trong customerList
        boolean daXoa = customerList.xoaCustomer(ma);
        if (!daXoa) {
            System.out.println("=> Không tìm thấy người có mã [" + ma + "] trong hệ thống!");
        }
    }

    /**
     * Tính năng 4: Tìm kiếm theo mã hoặc họ tên
     */
    public void timKiem() {
        System.out.print("\nNhập từ khóa tìm kiếm (Mã hoặc Họ tên): ");
        String tuKhoa = sc.nextLine().trim().toLowerCase();

        System.out.println("\n--- KẾT QUẢ TÌM KIẾM NHÂN SỰ ---");
        boolean timThayNV = false;
        for (int i = 0; i < soLuongNhanVien; i++) {
            NhanVien nv = dsNhanVien[i];
            if (nv.getMaNguoi().toLowerCase().contains(tuKhoa) || nv.getHoTen().toLowerCase().contains(tuKhoa)) {
                nv.hienThiThongTin();
                timThayNV = true;
            }
        }
        if (!timThayNV) {
            System.out.println("=> Không có nhân viên nào phù hợp!");
        }

        // Tìm kiếm trong CustomerList
        customerList.timKiem(tuKhoa);
    }

    /**
     * Tính năng 5: Xem thông tin nhân viên và bảng lương
     */
    public void xemThongTinNhanVienVaBangLuong() {
        System.out.println("\n====================== BẢNG LƯƠNG & THÔNG TIN NHÂN VIÊN ======================");
        if (soLuongNhanVien == 0) {
            System.out.println("Danh sách nhân viên trống!");
            return;
        }
        System.out.printf("%-8s | %-20s | %-18s | %-14s | %-14s\n",
                "Mã NV", "Họ Tên", "Chức Vụ", "Lương Cơ Bản", "Thực Nhận");
        System.out.println("--------------------------------------------------------------------------------");
        for (int i = 0; i < soLuongNhanVien; i++) {
            NhanVien nv = dsNhanVien[i];
            System.out.printf("%-8s | %-20s | %-18s | %,14.0f | %,14.0f VNĐ\n",
                    nv.getMaNguoi(), nv.getHoTen(), nv.getChucVu(), nv.getLuong(), nv.tinhLuong());
        }
        System.out.println("================================================================================");
    }

    // =========================================================================
    // 3. MENU CHO TỪNG VAI TRÒ
    // =========================================================================

    /**
     * Menu Quản lý
     */
    public void xuLyChucVuQuanLy() {
        QuanLy ql = null;
        for (int i = 0; i < soLuongNhanVien; i++) {
            if (dsNhanVien[i] instanceof QuanLy) {
                ql = (QuanLy) dsNhanVien[i];
                break;
            }
        }
        if (ql == null) {
            ql = new QuanLy("QL01", "Trần Anh Quân", "0901234567", "079099001234", "anhquan@hotel.com", "Quản lý điều hành", 20000000, "01/01/2020", 5000000, 5);
        }

        int chon = -1;
        do {
            System.out.println("\n========== CHỨC VỤ: QUẢN LÝ ==========");
            System.out.println("1. Xem thông tin nhân viên và bảng lương");
            System.out.println("2. Quản lý / Tra cứu khách hàng (CustomerList)");
            System.out.println("3. Thêm người (Quản lý / Nhân viên)");
            System.out.println("4. Sửa thông tin người");
            System.out.println("5. Xóa người");
            System.out.println("6. Tìm kiếm");
            System.out.println("7. Duyệt khuyến mãi (duyetKhuyenMai)");
            System.out.println("8. Xem báo cáo doanh thu (xemBaoCaoDoanhThu)");
            System.out.println("0. Quay lại chọn Role");
            System.out.println("--------------------------------------");
            System.out.print("Chọn chức năng: ");

            try {
                chon = Integer.parseInt(sc.nextLine().trim());
                switch (chon) {
                    case 1:
                        xemThongTinNhanVienVaBangLuong();
                        break;
                    case 2:
                        // Khi chọn quản lý sẽ vào phần customerList này để tra
                        customerList.menuCustomerList(sc);
                        break;
                    case 3:
                        themNguoi();
                        break;
                    case 4:
                        suaThongTin();
                        break;
                    case 5:
                        xoaNguoi();
                        break;
                    case 6:
                        timKiem();
                        break;
                    case 7:
                        ql.duyetKhuyenMai();
                        break;
                    case 8:
                        ql.xemBaoCaoDoanhThu();
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Lựa chọn không hợp lệ!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số!");
            }
        } while (chon != 0);
    }

    /**
     * Menu Nhân viên
     */
    public void xuLyChucVuNhanVien() {
        int chon = -1;
        do {
            System.out.println("\n========== CHỨC VỤ: NHÂN VIÊN ==========");
            System.out.println("1. Xem thông tin nhân viên và bảng lương");
            System.out.println("2. Tra cứu khách hàng (CustomerList)");
            System.out.println("3. Thêm người");
            System.out.println("4. Sửa thông tin");
            System.out.println("5. Tìm kiếm");
            System.out.println("0. Quay lại chọn Role");
            System.out.println("----------------------------------------");
            System.out.print("Chọn chức năng: ");

            try {
                chon = Integer.parseInt(sc.nextLine().trim());
                switch (chon) {
                    case 1:
                        xemThongTinNhanVienVaBangLuong();
                        break;
                    case 2:
                        customerList.menuCustomerList(sc);
                        break;
                    case 3:
                        themNguoi();
                        break;
                    case 4:
                        suaThongTin();
                        break;
                    case 5:
                        timKiem();
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Lựa chọn không hợp lệ!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số!");
            }
        } while (chon != 0);
    }

    /**
     * Menu Khách hàng
     */
    public void xuLyChucVuKhachHang() {
        int chon = -1;
        do {
            System.out.println("\n========== CHỨC VỤ: KHÁCH HÀNG ==========");
            System.out.println("1. Xem thông tin khách hàng của tôi");
            System.out.println("2. Cập nhật điểm tích lũy (capNhatDiemTichLuy)");
            System.out.println("0. Quay lại chọn Role");
            System.out.println("-----------------------------------------");
            System.out.print("Chọn chức năng: ");

            try {
                chon = Integer.parseInt(sc.nextLine().trim());
                switch (chon) {
                    case 1:
                        if (customerList.getSoLuong() > 0) {
                            customerList.getDanhSach()[0].hienThiThongTin();
                        } else {
                            System.out.println("Chưa có thông tin khách hàng!");
                        }
                        break;
                    case 2:
                        if (customerList.getSoLuong() > 0) {
                            System.out.print("Nhập số điểm cần cộng/trừ: ");
                            try {
                                int diem = Integer.parseInt(sc.nextLine().trim());
                                customerList.getDanhSach()[0].capNhatDiemTichLuy(diem);
                            } catch (NumberFormatException e) {
                                System.out.println("Điểm phải là số nguyên!");
                            }
                        }
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Lựa chọn không hợp lệ!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số!");
            }
        } while (chon != 0);
    }

    public CustomerList getCustomerList() {
        return customerList;
    }
}
