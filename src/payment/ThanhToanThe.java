public class ThanhToanThe implements IThanhToan {
    private String loaiThe;
    private String soTheRutGon;
    private String maGiaoDich;

    public ThanhToanThe(String loaiThe, String soTheRutGon,
                        String maGiaoDich) {
        this.loaiThe = loaiThe;
        this.soTheRutGon = soTheRutGon;
        this.maGiaoDich = maGiaoDich;
    }

    @Override
    public boolean thanhToan(double soTien) {
        if (soTien <= 0 || loaiThe == null || loaiThe.trim().isEmpty()
                || soTheRutGon == null || soTheRutGon.trim().isEmpty()
                || maGiaoDich == null || maGiaoDich.trim().isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public String layPhuongThuc() {
        return "Thanh toan bang the";
    }

    @Override
    public void hienThiThongTin() {
        System.out.println("Phuong thuc: " + layPhuongThuc());
        System.out.println("Loai the: " + loaiThe);
        System.out.println("So the: " + soTheRutGon);
        System.out.println("Ma giao dich: " + maGiaoDich);
    }
}
