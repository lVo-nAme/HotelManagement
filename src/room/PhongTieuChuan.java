package room;

public class PhongTieuChuan extends Phong {

    public PhongTieuChuan(String maPhong, String soPhong, int tang,
                          double giaCoBan, TrangThaiPhong trangThai,
                          int soNguoiToiDa, int soGiuong, double dienTich) {

        super(maPhong, soPhong, tang, giaCoBan, trangThai,
              soNguoiToiDa, soGiuong, dienTich);
    }

    @Override
    public double tinhTienPhong(int soDem) {
        return getGiaCoBan() * soDem;
    }

    @Override
    public void hienThiThongTin() {
        System.out.println("===== PHONG TIEU CHUAN =====");
        super.hienThiThongTin();
    }
    @Override
    public String toCSV() {
        return "TieuChuan," + getMaPhong() + "," + getSoPhong() + "," + getTang() + "," + 
               getGiaCoBan() + "," + getTrangThai() + "," + getSoNguoiToiDa() + "," + 
               getSoGiuong() + "," + getDienTich();
    }
}
