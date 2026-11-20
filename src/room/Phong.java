package room;

public abstract class Phong {

    // 1. Khai bao thuoc tinh
    private String maPhong;
    private String soPhong;
    private int tang;
    private double giaCoBan;
    private TrangThaiPhong trangThai;
    private int soNguoiToiDa;
    private int soGiuong;
    private double dienTich;

    // Thuoc tinh static
    private static int tongSoPhong = 0;

    // 2. Constructor
    public Phong(String maPhong, String soPhong, int tang,
                double giaCoBan, TrangThaiPhong trangThai,
                int soNguoiToiDa, int soGiuong, double dienTich) {

        this.maPhong = maPhong;
        this.soPhong = soPhong;
        this.tang = tang;
        this.giaCoBan = giaCoBan;
        this.trangThai = trangThai;
        this.soNguoiToiDa = soNguoiToiDa;
        this.soGiuong = soGiuong;
        this.dienTich = dienTich;

        tongSoPhong++;
    }

    // 3. Getter va Setter

    public String getMaPhong() {
        return maPhong;
    }

    public void setMaPhong(String maPhong) {
        this.maPhong = maPhong;
    }

    public String getSoPhong() {
        return soPhong;
    }

    public void setSoPhong(String soPhong) {
        this.soPhong = soPhong;
    }

    public int getTang() {
        return tang;
    }

    public void setTang(int tang) {
        this.tang = tang;
    }

    public double getGiaCoBan() {
        return giaCoBan;
    }

    public void setGiaCoBan(double giaCoBan) {
        this.giaCoBan = giaCoBan;
    }

    public TrangThaiPhong getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(TrangThaiPhong trangThai) {
        this.trangThai = trangThai;
    }

    public int getSoNguoiToiDa() {
        return soNguoiToiDa;
    }

    public void setSoNguoiToiDa(int soNguoiToiDa) {
        this.soNguoiToiDa = soNguoiToiDa;
    }

    public int getSoGiuong() {
        return soGiuong;
    }

    public void setSoGiuong(int soGiuong) {
        this.soGiuong = soGiuong;
    }

    public double getDienTich() {
        return dienTich;
    }

    public void setDienTich(double dienTich) {
        this.dienTich = dienTich;
    }

    public static int getTongSoPhong() {
        return tongSoPhong;
    }

    // 4. Phuong thuc abstract
    public abstract double tinhTienPhong(int soDem);
    
    // Hàm hỗ trợ ghi File 
    public abstract String toCSV();
    
    // 5. Kiem tra trang thai phong
    public boolean kiemTraTrong() {
        return trangThai == TrangThaiPhong.TRONG;
    }

    // 6. Hien thi thong tin phong
    public void hienThiThongTin() {
        System.out.println("Ma phong: " + maPhong);
        System.out.println("So phong: " + soPhong);
        System.out.println("Tang: " + tang);
        System.out.println("Gia co ban: " + giaCoBan);
        System.out.println("Trang thai: " + trangThai);
        System.out.println("Suc chua: " + soNguoiToiDa + " nguoi");
        System.out.println("So giuong: " + soGiuong);
        System.out.println("Dien tich: " + dienTich + " m2");
    }
}