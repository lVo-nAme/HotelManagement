import java.util.Scanner;

public abstract class Nguoi {
    // Thuộc tính
    protected String maNguoi;
    protected String hoTen;
    protected String soDienThoai;
    protected String cccd;
    protected String email;

    // Hàm khởi tạo không tham số
    public Nguoi() {
    }

    // Hàm khởi tạo đầy đủ tham số
    public Nguoi(String maNguoi, String hoTen, String soDienThoai, String cccd, String email) {
        this.maNguoi = maNguoi;
        this.hoTen = hoTen;
        this.soDienThoai = soDienThoai;
        this.cccd = cccd;
        this.email = email;
    }

    // Getters và Setters
    public String getMaNguoi() {
        return maNguoi;
    }

    public void setMaNguoi(String maNguoi) {
        this.maNguoi = maNguoi;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Phương thức nhập thông tin cơ bản
    public void nhapThongTin() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhập mã định danh: ");
        this.maNguoi = sc.nextLine();
        System.out.print("Nhập họ tên: ");
        this.hoTen = sc.nextLine();
        System.out.print("Nhập số điện thoại: ");
        this.soDienThoai = sc.nextLine();
        System.out.print("Nhập CCCD: ");
        this.cccd = sc.nextLine();
        System.out.print("Nhập email: ");
        this.email = sc.nextLine();
    }

    // Phương thức trừu tượng hiển thị thông tin
    public abstract void hienThiThongTin();
}
