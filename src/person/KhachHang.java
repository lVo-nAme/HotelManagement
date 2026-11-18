package person;

import java.util.Scanner;

public class KhachHang extends Nguoi {
    // Thuộc tính
    private String diaChi;
    private String quocTich;
    private int diemTichLuy;

    // Constructors
    public KhachHang() {
        super();
        this.diemTichLuy = 0;
    }

    public KhachHang(String maNguoi, String hoTen, String soDienThoai, String cccd, String email,
                     String diaChi, String quocTich, int diemTichLuy) {
        super(maNguoi, hoTen, soDienThoai, cccd, email);
        this.diaChi = diaChi;
        this.quocTich = quocTich;
        this.diemTichLuy = diemTichLuy;
    }

    // Getters và Setters
    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getQuocTich() {
        return quocTich;
    }

    public void setQuocTich(String quocTich) {
        this.quocTich = quocTich;
    }

    public int getDiemTichLuy() {
        return diemTichLuy;
    }

    public void setDiemTichLuy(int diemTichLuy) {
        this.diemTichLuy = diemTichLuy;
    }

    // Phương thức cập nhật điểm tích lũy theo sơ đồ UML
    public void capNhatDiemTichLuy(int diem) {
        this.diemTichLuy += diem;
        System.out.println("-> Cập nhật thành công! Khách hàng [" + hoTen + "] có điểm tích lũy mới: " + this.diemTichLuy);
    }

    // Ghi đè phương thức nhập thông tin
    @Override
    public void nhapThongTin() {
        super.nhapThongTin();
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhập địa chỉ: ");
        this.diaChi = sc.nextLine();
        System.out.print("Nhập quốc tịch: ");
        this.quocTich = sc.nextLine();
        System.out.print("Nhập điểm tích lũy: ");
        while (true) {
            try {
                this.diemTichLuy = Integer.parseInt(sc.nextLine().trim());
                break;
            } catch (NumberFormatException e) {
                System.out.print("Điểm tích lũy phải là số nguyên. Vui lòng nhập lại: ");
            }
        }
    }

    // Ghi đè phương thức hiển thị thông tin
    @Override
    public void hienThiThongTin() {
        System.out.println("----------------------------------------");
        System.out.println("           THÔNG TIN KHÁCH HÀNG         ");
        System.out.println("----------------------------------------");
        System.out.println("Mã khách hàng   : " + maNguoi);
        System.out.println("Họ tên          : " + hoTen);
        System.out.println("Số điện thoại   : " + soDienThoai);
        System.out.println("CCCD            : " + cccd);
        System.out.println("Email           : " + email);
        System.out.println("Địa chỉ         : " + diaChi);
        System.out.println("Quốc tịch       : " + quocTich);
        System.out.println("Điểm tích lũy   : " + diemTichLuy);
        System.out.println("----------------------------------------");
    }
}
