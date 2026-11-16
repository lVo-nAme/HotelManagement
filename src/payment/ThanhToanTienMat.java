public class ThanhToanTienMat implements IThanhToan {
    private double tienKhachDua;

    public ThanhToanTienMat(double tienKhachDua) {
        this.tienKhachDua = tienKhachDua;
    }

    @Override
    public boolean thanhToan(double soTien) {
        if (soTien <= 0 || tienKhachDua < soTien) {
            return false;
        }
        return true;
    }

    @Override
    public String layPhuongThuc() {
        return "Thanh toan tien mat";
    }

    @Override
    public void hienThiThongTin() {
        System.out.println("Phuong thuc: " + layPhuongThuc());
        System.out.println("Tien khach dua: " + tienKhachDua);
    }
}
