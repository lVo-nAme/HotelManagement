package person;

import fileio.FileHandler;
import java.util.Scanner;

public class QuanLyNguoi {
    private static final int MAX = 100;

    // Danh sách quản lý nhân viên và khách hàng
    private NhanVien[] dsNhanVien;
    private int soLuongNhanVien;

    private KhachHang[] dsKhachHang;
    private int soLuongKhachHang;

    private Scanner sc;

    public QuanLyNguoi() {
        dsNhanVien = new NhanVien[MAX];
        soLuongNhanVien = 0;

        dsKhachHang = new KhachHang[MAX];
        soLuongKhachHang = 0;

        sc = new Scanner(System.in);

        khoiTaoDuLieuMau();
    }

    /**
     * Khởi tạo dữ liệu mẫu ban đầu
     */
    private void khoiTaoDuLieuMau() {
        // Quản lý và nhân viên mẫu
        dsNhanVien[soLuongNhanVien++] = new QuanLy("QL01", "Trần Anh Quân", "0901234567", "079099001234", "anhquan@hotel.com", "Quản lý điều hành", 20000000, "01/01/2020", 5000000, 5);
        dsNhanVien[soLuongNhanVien++] = new NhanVien("NV01", "Nguyễn Văn Hùng", "0987654321", "079098001111", "hungnv@hotel.com", "Lễ tân", 8000000, "10/05/2022");
        dsNhanVien[soLuongNhanVien++] = new NhanVien("NV02", "Phạm Thị Hương", "0978111222", "079098002222", "huongpt@hotel.com", "Kế toán", 9000000, "01/08/2022");

        // Khách hàng mẫu
        dsKhachHang[soLuongKhachHang++] = new KhachHang("KH01", "Nguyễn Hoàng Nam", "0911223344", "079199001234", "namnh@gmail.com", "Quận 1, TP.HCM", "Việt Nam", 150);
        dsKhachHang[soLuongKhachHang++] = new KhachHang("KH02", "John Smith", "0922334455", "P01234567", "johnsmith@gmail.com", "California", "Hoa Kỳ", 300);
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
    //    - Thêm người
    //    - Sửa thông tin
    //    - Xóa người
    //    - Tìm kiếm
    //    - Xem thông tin nhân viên và bảng lương
    //    - Xem thông tin khách hàng
    // =========================================================================

    /**
     * Tính năng 1: Thêm người (Nhân viên / Khách hàng / Quản lý)
     */
    public void themNguoi() {
        System.out.println("\n--- THÊM NGƯỜI MỚI ---");
        System.out.println("1. Thêm Quản lý");
        System.out.println("2. Thêm Nhân viên");
        System.out.println("3. Thêm Khách hàng");
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
            if (soLuongKhachHang >= MAX) {
                System.out.println("Danh sách khách hàng đã đầy!");
                return;
            }
            KhachHang kh = new KhachHang();
            kh.nhapThongTin();
            dsKhachHang[soLuongKhachHang++] = kh;
            System.out.println("=> Đã thêm Khách hàng [" + kh.getHoTen() + "] thành công!");
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
                System.out.println("=> Cập nhật thông tin thành công!");
                return;
            }
        }

        // Tìm trong danh sách khách hàng
        for (int i = 0; i < soLuongKhachHang; i++) {
            if (dsKhachHang[i].getMaNguoi().equalsIgnoreCase(ma)) {
                System.out.println("Tìm thấy khách hàng: " + dsKhachHang[i].getHoTen());
                System.out.print("Nhập họ tên mới: ");
                dsKhachHang[i].setHoTen(sc.nextLine());
                System.out.print("Nhập số điện thoại mới: ");
                dsKhachHang[i].setSoDienThoai(sc.nextLine());
                System.out.print("Nhập CCCD mới: ");
                dsKhachHang[i].setCccd(sc.nextLine());
                System.out.print("Nhập email mới: ");
                dsKhachHang[i].setEmail(sc.nextLine());
                System.out.print("Nhập địa chỉ mới: ");
                dsKhachHang[i].setDiaChi(sc.nextLine());
                System.out.print("Nhập quốc tịch mới: ");
                dsKhachHang[i].setQuocTich(sc.nextLine());
                System.out.println("=> Cập nhật thông tin khách hàng thành công!");
                return;
            }
        }

        System.out.println("=> Không tìm thấy người có mã [" + ma + "]!");
    }

    /**
     * Tính năng 3: Xóa người theo Mã định danh
     */
    public void xoaNguoi() {
        System.out.print("\nNhập mã người cần xóa: ");
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

        // Kiểm tra xóa trong danh sách khách hàng
        for (int i = 0; i < soLuongKhachHang; i++) {
            if (dsKhachHang[i].getMaNguoi().equalsIgnoreCase(ma)) {
                for (int j = i; j < soLuongKhachHang - 1; j++) {
                    dsKhachHang[j] = dsKhachHang[j + 1];
                }
                dsKhachHang[--soLuongKhachHang] = null;
                System.out.println("=> Đã xóa khách hàng có mã [" + ma + "] thành công!");
                return;
            }
        }

        System.out.println("=> Không tìm thấy người có mã [" + ma + "] để xóa!");
    }

    /**
     * Tính năng 4: Tìm kiếm theo mã hoặc họ tên
     */
    public void timKiem() {
        System.out.print("\nNhập từ khóa tìm kiếm (Mã hoặc Họ tên): ");
        String tuKhoa = sc.nextLine().trim().toLowerCase();
        boolean timThay = false;

        System.out.println("\n--- KẾT QUẢ TÌM KIẾM ---");
        // Tìm trong nhân viên
        for (int i = 0; i < soLuongNhanVien; i++) {
            NhanVien nv = dsNhanVien[i];
            if (nv.getMaNguoi().toLowerCase().contains(tuKhoa) || nv.getHoTen().toLowerCase().contains(tuKhoa)) {
                nv.hienThiThongTin();
                timThay = true;
            }
        }

        // Tìm trong khách hàng
        for (int i = 0; i < soLuongKhachHang; i++) {
            KhachHang kh = dsKhachHang[i];
            if (kh.getMaNguoi().toLowerCase().contains(tuKhoa) || kh.getHoTen().toLowerCase().contains(tuKhoa)) {
                kh.hienThiThongTin();
                timThay = true;
            }
        }

        if (!timThay) {
            System.out.println("=> Không tìm thấy kết quả nào phù hợp!");
        }
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

    /**
     * Tính năng 6: Xem thông tin khách hàng
     */
    public void xemThongTinKhachHang() {
        System.out.println("\n================================ DANH SÁCH KHÁCH HÀNG ================================");
        if (soLuongKhachHang == 0) {
            System.out.println("Danh sách khách hàng trống!");
            return;
        }
        System.out.printf("%-8s | %-20s | %-12s | %-14s | %-16s | %-10s | %-8s\n",
                "Mã KH", "Họ Tên", "SĐT", "CCCD", "Địa Chỉ", "Quốc Tịch", "Điểm");
        System.out.println("--------------------------------------------------------------------------------------");
        for (int i = 0; i < soLuongKhachHang; i++) {
            KhachHang kh = dsKhachHang[i];
            System.out.printf("%-8s | %-20s | %-12s | %-14s | %-16s | %-10s | %-8d\n",
                    kh.getMaNguoi(), kh.getHoTen(), kh.getSoDienThoai(), kh.getCccd(), kh.getDiaChi(), kh.getQuocTich(), kh.getDiemTichLuy());
        }
        System.out.println("======================================================================================");
    }

    /**
     * Tính năng 7: Ghi file khách hàng (data/customers.txt)
     */
    public void ghiFileKhachHang() {
        String[] lines = new String[soLuongKhachHang];
        for (int i = 0; i < soLuongKhachHang; i++) {
            KhachHang kh = dsKhachHang[i];
            lines[i] = kh.getMaNguoi() + ";" + kh.getHoTen() + ";" + kh.getSoDienThoai() + ";"
                    + kh.getCccd() + ";" + kh.getEmail() + ";" + kh.getDiaChi() + ";"
                    + kh.getQuocTich() + ";" + kh.getDiemTichLuy();
        }
        FileHandler.writeLinesToFile("customers.txt", lines, false);
    }

    /**
     * Tính năng 8: Đọc file khách hàng (data/customers.txt)
     */
    public void docFileKhachHang() {
        String[] lines = FileHandler.readLinesFromFile("customers.txt");
        if (lines == null || lines.length == 0) {
            System.out.println("=> File customers.txt trống hoặc chưa có dữ liệu!");
            return;
        }
        soLuongKhachHang = 0;
        for (int i = 0; i < lines.length; i++) {
            if (lines[i] == null || lines[i].trim().isEmpty()) {
                continue;
            }
            String[] parts = lines[i].split(";", -1);
            if (parts.length >= 8 && soLuongKhachHang < MAX) {
                int diem = 0;
                try {
                    diem = Integer.parseInt(parts[7].trim());
                } catch (NumberFormatException ignored) {}
                dsKhachHang[soLuongKhachHang++] = new KhachHang(
                        parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim(),
                        parts[4].trim(), parts[5].trim(), parts[6].trim(), diem);
            }
        }
        System.out.println("=> Đã đọc " + soLuongKhachHang + " khách hàng từ file data/customers.txt!");
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
            System.out.println("2. Xem thông tin khách hàng");
            System.out.println("3. Thêm người (Quản lý / Nhân viên / Khách hàng)");
            System.out.println("4. Sửa thông tin người");
            System.out.println("5. Xóa người");
            System.out.println("6. Tìm kiếm");
            System.out.println("7. Duyệt khuyến mãi (duyetKhuyenMai)");
            System.out.println("8. Xem báo cáo doanh thu (xemBaoCaoDoanhThu)");
            System.out.println("9. Ghi file khách hàng");
            System.out.println("10. Đọc file khách hàng");
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
                        xemThongTinKhachHang();
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
                    case 9:
                        ghiFileKhachHang();
                        break;
                    case 10:
                        docFileKhachHang();
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
            System.out.println("2. Xem thông tin khách hàng");
            System.out.println("3. Thêm người");
            System.out.println("4. Sửa thông tin");
            System.out.println("5. Tìm kiếm");
            System.out.println("6. Ghi file khách hàng");
            System.out.println("7. Đọc file khách hàng");
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
                        xemThongTinKhachHang();
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
                    case 6:
                        ghiFileKhachHang();
                        break;
                    case 7:
                        docFileKhachHang();
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
                        if (soLuongKhachHang > 0) {
                            dsKhachHang[0].hienThiThongTin();
                        } else {
                            System.out.println("Chưa có thông tin khách hàng!");
                        }
                        break;
                    case 2:
                        if (soLuongKhachHang > 0) {
                            System.out.print("Nhập số điểm cần cộng/trừ: ");
                            try {
                                int diem = Integer.parseInt(sc.nextLine().trim());
                                dsKhachHang[0].capNhatDiemTichLuy(diem);
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
}
