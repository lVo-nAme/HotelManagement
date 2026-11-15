import java.util.Scanner;

public class NhanVien extends Nguoi {
    // Thuộc tính
    protected String chucVu;
    protected double luong;
    protected String ngayVaoLam;

    // Constructors
    public NhanVien() {
        super();
    }

    public NhanVien(String maNguoi, String hoTen, String soDienThoai, String cccd, String email,
                    String chucVu, double luong, String ngayVaoLam) {
        super(maNguoi, hoTen, soDienThoai, cccd, email);
        this.chucVu = chucVu;
        this.luong = luong;
        this.ngayVaoLam = ngayVaoLam;
    }

    // Getters và Setters
    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    public double getLuong() {
        return luong;
    }

    public void setLuong(double luong) {
        this.luong = luong;
    }

    public String getNgayVaoLam() {
        return ngayVaoLam;
    }

    public void setNgayVaoLam(String ngayVaoLam) {
        this.ngayVaoLam = ngayVaoLam;
    }

    // Phương thức tính lương
    public double tinhLuong() {
        return this.luong;
    }

    // Ghi đè phương thức nhập thông tin
    @Override
    public void nhapThongTin() {
        super.nhapThongTin();
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhập chức vụ: ");
        this.chucVu = sc.nextLine();
        System.out.print("Nhập mức lương cơ bản (VNĐ): ");
        while (true) {
            try {
                this.luong = Double.parseDouble(sc.nextLine().trim());
                break;
            } catch (NumberFormatException e) {
                System.out.print("Lương phải là số thực. Vui lòng nhập lại: ");
            }
        }
        System.out.print("Nhập ngày vào làm (dd/MM/yyyy): ");
        this.ngayVaoLam = sc.nextLine();
    }

    // Ghi đè phương thức hiển thị thông tin
    @Override
    public void hienThiThongTin() {
        System.out.println("----------------------------------------");
        System.out.println("           THÔNG TIN NHÂN VIÊN          ");
        System.out.println("----------------------------------------");
        System.out.println("Mã nhân viên    : " + maNguoi);
        System.out.println("Họ tên          : " + hoTen);
        System.out.println("Số điện thoại   : " + soDienThoai);
        System.out.println("CCCD            : " + cccd);
        System.out.println("Email           : " + email);
        System.out.println("Chức vụ         : " + chucVu);
        System.out.printf("Mức lương cơ bản: %,.0f VNĐ\n", luong);
        System.out.println("Ngày vào làm    : " + ngayVaoLam);
        System.out.printf("Tổng thực nhận  : %,.0f VNĐ\n", tinhLuong());
        System.out.println("----------------------------------------");
    }
}
