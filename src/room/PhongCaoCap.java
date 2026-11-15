package room;

public class PhongCaoCap extends Phong {

    private boolean coBanCong;

    public PhongCaoCap(String maPhong, String soPhong, int tang,
                       double giaCoBan, TrangThaiPhong trangThai,
                       int soNguoiToiDa, int soGiuong, double dienTich,
                       boolean coBanCong) {

        super(maPhong, soPhong, tang, giaCoBan, trangThai,
              soNguoiToiDa, soGiuong, dienTich);

        this.coBanCong = coBanCong;
    }

    public boolean isCoBanCong() {
        return coBanCong;
    }

    public void setCoBanCong(boolean coBanCong) {
        this.coBanCong = coBanCong;
    }

    @Override
    public double tinhTienPhong(int soDem) {
        return (getGiaCoBan() * soDem) *1.1;
    }

    @Override
    public void hienThiThongTin() {
        System.out.println("===== PHONG CAO CAP =====");
        super.hienThiThongTin();

        System.out.println("Co ban cong: "
                + (coBanCong ? "Co" : "Khong"));
    }
    
    @Override
    public String toCSV() {
        return "CaoCap," + getMaPhong() + "," + getSoPhong() + "," + getTang() + "," + 
               getGiaCoBan() + "," + getTrangThai() + "," + getSoNguoiToiDa() + "," + 
               getSoGiuong() + "," + getDienTich() + "," + coBanCong;
    }
}