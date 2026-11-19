package management;

import fileio.FileHandler;
import java.util.Scanner;
import person.KhachHang;

public class CustomerList {
    private static final int DUNG_LUONG_MAC_DINH = 100;
    private static final String FILE_NAME = "customers.txt";

    private KhachHang[] danhSach;
    private int soLuong;

    public CustomerList() {
        this(DUNG_LUONG_MAC_DINH);
    }

    public CustomerList(int dungLuong) {
        this.danhSach = new KhachHang[dungLuong > 0 ? dungLuong : DUNG_LUONG_MAC_DINH];
        this.soLuong = 0;
        khoiTaoDuLieuMau();
    }

    private void khoiTaoDuLieuMau() {
        luuCustomerKhiDatPhong(new KhachHang("KH01", "Nguyễn Hoàng Nam", "0911223344", "079199001234", "namnh@gmail.com", "Quận 1, TP.HCM", "Việt Nam", 150));
        luuCustomerKhiDatPhong(new KhachHang("KH02", "John Smith", "0922334455", "P01234567", "johnsmith@gmail.com", "California", "Hoa Kỳ", 300));
    }

    /**
     * Hàm lưu khách hàng (customer) vào customerList khi đặt phòng.
     * - Kiểm tra nếu customer đã có trong danh sách (theo Mã hoặc CCCD) thì không lưu trùng.
     * - Nếu là khách hàng mới thì tự động thêm vào customerList.
     *
     * @param customer Khách hàng thực hiện đặt phòng
     * @return true nếu lưu thành công hoặc khách hàng đã tồn tại hợp lệ, false nếu dữ liệu không hợp lệ
     */
    public boolean luuCustomerKhiDatPhong(KhachHang customer) {
        if (customer == null) {
            System.out.println("⚠️ Lỗi: Thông tin khách hàng đặt phòng không hợp lệ!");
            return false;
        }

        // Kiểm tra xem khách hàng đã tồn tại trong customerList chưa
        for (int i = 0; i < soLuong; i++) {
            if (danhSach[i] != null) {
                boolean trungMa = danhSach[i].getMaNguoi() != null
                        && danhSach[i].getMaNguoi().equalsIgnoreCase(customer.getMaNguoi());
                boolean trungCccd = danhSach[i].getCccd() != null
                        && danhSach[i].getCccd().equalsIgnoreCase(customer.getCccd());

                if (trungMa || trungCccd) {
                    System.out.println("=> Khách hàng [" + customer.getHoTen() + "] (Mã: " + danhSach[i].getMaNguoi()
                            + ") đã tồn tại trong CustomerList.");
                    return true;
                }
            }
        }

        // Mở rộng mảng nếu danh sách đầy
        if (soLuong >= danhSach.length) {
            KhachHang[] mangMoi = new KhachHang[danhSach.length * 2];
            System.arraycopy(danhSach, 0, mangMoi, 0, soLuong);
            danhSach = mangMoi;
        }

        // Lưu khách hàng mới vào customerList
        danhSach[soLuong++] = customer;
        System.out.println("=> Đã lưu khách hàng mới [" + customer.getHoTen() + "] vào CustomerList thành công khi đặt phòng!");
        return true;
    }

    /**
     * Hàm xóa khách hàng khỏi customerList theo mã khách hàng (hoặc CCCD).
     *
     * @param maKhachHang Mã khách hàng hoặc CCCD cần xóa
     * @return true nếu xóa thành công, false nếu không tìm thấy
     */
    public boolean xoaCustomer(String maKhachHang) {
        if (maKhachHang == null || maKhachHang.trim().isEmpty()) {
            System.out.println("⚠️ Lỗi: Mã khách hàng cần xóa không được để trống!");
            return false;
        }

        int viTri = -1;
        for (int i = 0; i < soLuong; i++) {
            if (danhSach[i] != null) {
                boolean trungMa = danhSach[i].getMaNguoi() != null
                        && danhSach[i].getMaNguoi().equalsIgnoreCase(maKhachHang.trim());
                boolean trungCccd = danhSach[i].getCccd() != null
                        && danhSach[i].getCccd().equalsIgnoreCase(maKhachHang.trim());

                if (trungMa || trungCccd) {
                    viTri = i;
                    break;
                }
            }
        }

        if (viTri == -1) {
            System.out.println("⚠️ Không tìm thấy khách hàng có mã/CCCD [" + maKhachHang + "] để xóa!");
            return false;
        }

        String tenXoa = danhSach[viTri].getHoTen();
        // Dồn mảng
        for (int i = viTri; i < soLuong - 1; i++) {
            danhSach[i] = danhSach[i + 1];
        }
        danhSach[--soLuong] = null;
        System.out.println("=> Đã xóa khách hàng [" + tenXoa + "] (Mã: " + maKhachHang + ") khỏi CustomerList thành công!");
        return true;
    }

    /**
     * Tìm khách hàng theo mã khách hàng hoặc CCCD
     */
    public KhachHang timCustomer(String maKhachHang) {
        if (maKhachHang == null) return null;
        for (int i = 0; i < soLuong; i++) {
            if (danhSach[i] != null) {
                if (danhSach[i].getMaNguoi().equalsIgnoreCase(maKhachHang.trim())
                        || danhSach[i].getCccd().equalsIgnoreCase(maKhachHang.trim())) {
                    return danhSach[i];
                }
            }
        }
        return null;
    }

    /**
     * Tìm kiếm khách hàng theo từ khóa (Mã hoặc Họ tên)
     */
    public void timKiem(String tuKhoa) {
        if (tuKhoa == null || tuKhoa.trim().isEmpty()) {
            System.out.println("Từ khóa tìm kiếm không được để trống!");
            return;
        }
        String key = tuKhoa.trim().toLowerCase();
        boolean timThay = false;
        System.out.println("\n--- KẾT QUẢ TÌM KIẾM KHÁCH HÀNG ---");
        for (int i = 0; i < soLuong; i++) {
            KhachHang kh = danhSach[i];
            if (kh != null && (kh.getMaNguoi().toLowerCase().contains(key) || kh.getHoTen().toLowerCase().contains(key))) {
                kh.hienThiThongTin();
                timThay = true;
            }
        }
        if (!timThay) {
            System.out.println("=> Không tìm thấy khách hàng nào phù hợp với từ khóa: " + tuKhoa);
        }
    }

    /**
     * Sửa thông tin khách hàng theo mã
     */
    public void suaCustomer(String maKhachHang, Scanner sc) {
        KhachHang kh = timCustomer(maKhachHang);
        if (kh == null) {
            System.out.println("=> Không tìm thấy khách hàng có mã [" + maKhachHang + "]!");
            return;
        }

        System.out.println("Tìm thấy khách hàng: " + kh.getHoTen());
        System.out.print("Nhập họ tên mới: ");
        kh.setHoTen(sc.nextLine());
        System.out.print("Nhập số điện thoại mới: ");
        kh.setSoDienThoai(sc.nextLine());
        System.out.print("Nhập CCCD mới: ");
        kh.setCccd(sc.nextLine());
        System.out.print("Nhập email mới: ");
        kh.setEmail(sc.nextLine());
        System.out.print("Nhập địa chỉ mới: ");
        kh.setDiaChi(sc.nextLine());
        System.out.print("Nhập quốc tịch mới: ");
        kh.setQuocTich(sc.nextLine());
        System.out.println("=> Cập nhật thông tin khách hàng thành công!");
    }

    /**
     * Thêm mới một khách hàng từ bàn phím
     */
    public void themMoiKhachHang(Scanner sc) {
        KhachHang kh = new KhachHang();
        kh.nhapThongTin();
        luuCustomerKhiDatPhong(kh);
    }

    /**
     * Xuất danh sách khách hàng hiện có trong CustomerList
     */
    public void xuatDanhSach() {
        System.out.println("\n================================ DANH SÁCH KHÁCH HÀNG (" + soLuong + ") ================================");
        if (soLuong == 0) {
            System.out.println("Danh sách khách hàng đang trống!");
            return;
        }
        System.out.printf("%-4s | %-8s | %-20s | %-12s | %-14s | %-16s | %-10s | %-8s\n",
                "STT", "Mã KH", "Họ Tên", "SĐT", "CCCD", "Địa Chỉ", "Quốc Tịch", "Điểm");
        System.out.println("---------------------------------------------------------------------------------------------------------");
        for (int i = 0; i < soLuong; i++) {
            KhachHang kh = danhSach[i];
            System.out.printf("%-4d | %-8s | %-20s | %-12s | %-14s | %-16s | %-10s | %-8d\n",
                    i + 1, kh.getMaNguoi(), kh.getHoTen(), kh.getSoDienThoai(), kh.getCccd(), kh.getDiaChi(), kh.getQuocTich(), kh.getDiemTichLuy());
        }
        System.out.println("=========================================================================================================");
    }

    /**
     * Ghi file danh sách khách hàng (data/customers.txt)
     */
    public void ghiFile() {
        String[] lines = new String[soLuong];
        for (int i = 0; i < soLuong; i++) {
            KhachHang kh = danhSach[i];
            lines[i] = kh.getMaNguoi() + ";" + kh.getHoTen() + ";" + kh.getSoDienThoai() + ";"
                    + kh.getCccd() + ";" + kh.getEmail() + ";" + kh.getDiaChi() + ";"
                    + kh.getQuocTich() + ";" + kh.getDiemTichLuy();
        }
        FileHandler.writeLinesToFile(FILE_NAME, lines, false);
    }

    /**
     * Đọc file danh sách khách hàng (data/customers.txt)
     */
    public void docFile() {
        String[] lines = FileHandler.readLinesFromFile(FILE_NAME);
        if (lines == null || lines.length == 0) {
            System.out.println("=> File " + FILE_NAME + " trống hoặc chưa có dữ liệu!");
            return;
        }
        soLuong = 0;
        for (String line : lines) {
            if (line == null || line.trim().isEmpty()) continue;
            String[] parts = line.split(";", -1);
            if (parts.length >= 8 && soLuong < danhSach.length) {
                int diem = 0;
                try {
                    diem = Integer.parseInt(parts[7].trim());
                } catch (NumberFormatException ignored) {}
                danhSach[soLuong++] = new KhachHang(
                        parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim(),
                        parts[4].trim(), parts[5].trim(), parts[6].trim(), diem);
            }
        }
        System.out.println("=> Đã đọc " + soLuong + " khách hàng từ file " + FILE_NAME + "!");
    }

    /**
     * Menu Quản lý Khách hàng (dành cho Quản lý truy cập vào tra cứu)
     */
    public void menuCustomerList(Scanner sc) {
        int chon = -1;
        do {
            System.out.println("\n======== PHÂN HỆ QUẢN LÝ KHÁCH HÀNG ========");
            System.out.println("1. Xuất danh sách khách hàng");
            System.out.println("2. Thêm mới khách hàng");
            System.out.println("3. Sửa thông tin khách hàng theo mã");
            System.out.println("4. Xóa khách hàng theo mã");
            System.out.println("5. Tìm kiếm khách hàng");
            System.out.println("6. Ghi file khách hàng");
            System.out.println("7. Đọc file khách hàng");
            System.out.println("0. Quay lại");
            System.out.print("Chọn chức năng khách hàng: ");

            try {
                chon = Integer.parseInt(sc.nextLine().trim());
                switch (chon) {
                    case 1:
                        xuatDanhSach();
                        break;
                    case 2:
                        themMoiKhachHang(sc);
                        break;
                    case 3:
                        System.out.print("Nhập mã khách hàng cần sửa: ");
                        suaCustomer(sc.nextLine().trim(), sc);
                        break;
                    case 4:
                        System.out.print("Nhập mã khách hàng cần xóa: ");
                        xoaCustomer(sc.nextLine().trim());
                        break;
                    case 5:
                        System.out.print("Nhập mã hoặc họ tên khách hàng cần tìm: ");
                        timKiem(sc.nextLine().trim());
                        break;
                    case 6:
                        ghiFile();
                        break;
                    case 7:
                        docFile();
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

    // Getters
    public KhachHang[] getDanhSach() {
        return danhSach;
    }

    public int getSoLuong() {
        return soLuong;
    }
}
