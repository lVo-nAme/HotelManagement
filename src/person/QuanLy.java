import java.util.Scanner;

public class QuanLy extends NhanVien {
    // Thuộc tính
    private double phuCap;
    private int soNamKinhNghiem;

    // Constructors
    public QuanLy() {
        super();
        this.chucVu = "Quản lý";
    }

    public QuanLy(String maNguoi, String hoTen, String soDienThoai, String cccd, String email,
                  String chucVu, double luong, String ngayVaoLam,
                  double phuCap, int soNamKinhNghiem) {
        super(maNguoi, hoTen, soDienThoai, cccd, email, chucVu, luong, ngayVaoLam);
        this.phuCap = phuCap;
        this.soNamKinhNghiem = soNamKinhNghiem;
    }

    // Getters và Setters
    public double getPhuCap() {
        return phuCap;
    }

    public void setPhuCap(double phuCap) {
        this.phuCap = phuCap;
    }

    public int getSoNamKinhNghiem() {
        return soNamKinhNghiem;
    }

    public void setSoNamKinhNghiem(int soNamKinhNghiem) {
        this.soNamKinhNghiem = soNamKinhNghiem;
    }

    // Ghi đè phương thức tính lương (Lương cơ bản + Phụ cấp trách nhiệm)
    @Override
    public double tinhLuong() {
        return this.luong + this.phuCap;
    }

    // Phương thức duyệt khuyến mãi theo sơ đồ UML
    public boolean duyetKhuyenMai() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n--- [XÉT DUYỆT KHUYẾN MÃI - QUẢN LÝ: " + this.hoTen + "] ---");
        System.out.print("Nhập tên chương trình khuyến mãi: ");
        String tenKM = sc.nextLine();
        System.out.print("Nhập mức chiết khấu/giảm giá đề xuất (%): ");
        double phanTram = 0;
        try {
            phanTram = Double.parseDouble(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            phanTram = 0;
        }

        System.out.print("Bạn có chấp thuận phê duyệt đợt khuyến mãi này không? (y/n): ");
        String xacNhan = sc.nextLine().trim();
        boolean duyet = xacNhan.equalsIgnoreCase("y");

        if (duyet) {
            System.out.println(">> KẾT QUẢ: Quản lý " + this.hoTen + " ĐÃ DUYỆT chương trình [" + tenKM + "] (Giảm " + phanTram + "%).");
        } else {
            System.out.println(">> KẾT QUẢ: Quản lý " + this.hoTen + " TỪ CHỐI duyệt chương trình [" + tenKM + "].");
        }
        return duyet;
    }

    // Phương thức xem báo cáo doanh thu theo sơ đồ UML
    public void xemBaoCaoDoanhThu() {
        System.out.println("\n========================================================");
        System.out.println("        BÁO CÁO DOANH THU & HIỆU SUẤT HOẠT ĐỘNG         ");
        System.out.println("========================================================");
        System.out.println("Người phụ trách duyệt  : " + this.hoTen + " (" + this.chucVu + ")");
        System.out.println("Thâm niên kinh nghiệm  : " + this.soNamKinhNghiem + " năm");
        System.out.println("Tổng doanh thu khách sạn: 350,000,000 VNĐ");
        System.out.println("Chi phí vận hành       : 120,000,000 VNĐ");
        System.out.println("Lợi nhuận gộp          : 230,000,000 VNĐ");
        System.out.println("Tỷ lệ phòng lấp đầy    : 88.5%");
        System.out.println("Trạng thái             : Đạt chỉ tiêu kinh doanh tháng");
        System.out.println("========================================================");
    }

    // Ghi đè phương thức nhập thông tin
    @Override
    public void nhapThongTin() {
        super.nhapThongTin();
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhập mức phụ cấp quản lý (VNĐ): ");
        while (true) {
            try {
                this.phuCap = Double.parseDouble(sc.nextLine().trim());
                break;
            } catch (NumberFormatException e) {
                System.out.print("Phụ cấp phải là số thực. Vui lòng nhập lại: ");
            }
        }
        System.out.print("Nhập số năm kinh nghiệm: ");
        while (true) {
            try {
                this.soNamKinhNghiem = Integer.parseInt(sc.nextLine().trim());
                break;
            } catch (NumberFormatException e) {
                System.out.print("Số năm kinh nghiệm phải là số nguyên. Vui lòng nhập lại: ");
            }
        }
    }

    // Ghi đè phương thức hiển thị thông tin
    @Override
    public void hienThiThongTin() {
        System.out.println("----------------------------------------");
        System.out.println("           THÔNG TIN QUẢN LÝ            ");
        System.out.println("----------------------------------------");
        System.out.println("Mã quản lý      : " + maNguoi);
        System.out.println("Họ tên          : " + hoTen);
        System.out.println("Số điện thoại   : " + soDienThoai);
        System.out.println("CCCD            : " + cccd);
        System.out.println("Email           : " + email);
        System.out.println("Chức vụ         : " + chucVu);
        System.out.printf("Mức lương cơ bản: %,.0f VNĐ\n", luong);
        System.out.printf("Phụ cấp trách nhiệm: %,.0f VNĐ\n", phuCap);
        System.out.printf("Tổng thực nhận  : %,.0f VNĐ\n", tinhLuong());
        System.out.println("Kinh nghiệm     : " + soNamKinhNghiem + " năm");
        System.out.println("Ngày vào làm    : " + ngayVaoLam);
        System.out.println("----------------------------------------");
    }
}
