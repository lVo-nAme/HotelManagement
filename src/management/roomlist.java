package management;

import room.Phong;
import room.PhongCaoCap;
import room.PhongSuite;
import room.PhongTieuChuan;
import room.TrangThaiPhong;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

class QuanLyPhong {
    private Phong[] danhSach;
    private int soLuong;
    private final int MAX = 100;

    public QuanLyPhong() {
        danhSach = new Phong[MAX];
        soLuong = 0;
    }

    public void themPhong(Phong p) {
        if (soLuong < MAX) {
            danhSach[soLuong] = p;
            soLuong++;
            System.out.println("=> Da them phong thanh cong!");
        } else {
            System.out.println("=> Danh sach day!");
        }
    } 
    // 2. THÊM MỚI: Hàm nhập thông tin từ bàn phím và tự thêm phòng
    public void themPhong(Scanner sc) {
        System.out.println("--- Them moi 1 phong ---");
        System.out.print("Chon loai (1-Tieu chuan, 2-Cao cap, 3-Suite): ");
        int loai = Integer.parseInt(sc.nextLine());
        
        System.out.print("Ma phong: "); String ma = sc.nextLine();
        System.out.print("So phong: "); String so = sc.nextLine();
        System.out.print("Tang: "); int tang = Integer.parseInt(sc.nextLine());
        System.out.print("Gia co ban: "); double gia = Double.parseDouble(sc.nextLine());
        
        System.out.print("Trang thai (1-TRONG, 2-DA_DAT, 3-DANG_SU_DUNG, 4-BAO_TRI): ");
        int tt = Integer.parseInt(sc.nextLine());
        TrangThaiPhong trangThai = TrangThaiPhong.TRONG;
        if (tt == 2) trangThai = TrangThaiPhong.DA_DAT;
        else if (tt == 3) trangThai = TrangThaiPhong.DANG_SU_DUNG;
        else if (tt == 4) trangThai = TrangThaiPhong.BAO_TRI;
        System.out.print("So nguoi toi da: "); int nguoi = Integer.parseInt(sc.nextLine());
        System.out.print("So giuong: "); int giuong = Integer.parseInt(sc.nextLine());
        System.out.print("Dien tich: "); double dt = Double.parseDouble(sc.nextLine());
        Phong p = null;
        if (loai == 1) {
            p = new PhongTieuChuan(ma, so, tang, gia, trangThai, nguoi, giuong, dt);
        } else if (loai == 2) {
            System.out.print("Co ban cong? (true/false): ");
            boolean bc = Boolean.parseBoolean(sc.nextLine());
            p = new PhongCaoCap(ma, so, tang, gia, trangThai, nguoi, giuong, dt, bc);
        } else if (loai == 3) {
            System.out.print("Co phong khach? (true/false): ");
            boolean pk = Boolean.parseBoolean(sc.nextLine());
            System.out.print("Co bon tam? (true/false): ");
            boolean bt = Boolean.parseBoolean(sc.nextLine());
            p = new PhongSuite(ma, so, tang, gia, trangThai, nguoi, giuong, dt, pk, bt);
        } else {
            System.out.println("=> Loai phong khong hop le!");
            return;
        }
        themPhong(p); 
    }
    public void themPhong() {
        themPhong(new Scanner(System.in));
    }

    public void xuatDanhSach() {
        if (soLuong == 0) {
            System.out.println("=> Danh sach rong!");
            return;
        }
        for (int i = 0; i < soLuong; i++) {
            danhSach[i].hienThiThongTin();
        }
    }

    public void suaPhong(String maPhong, Scanner sc) {
        for (int i = 0; i < soLuong; i++) {
            if (danhSach[i].getMaPhong().equalsIgnoreCase(maPhong)) {
                System.out.print("Nhap gia co ban moi: ");
                double giaMoi = Double.parseDouble(sc.nextLine());
                danhSach[i].setGiaCoBan(giaMoi);
                
                System.out.print("Nhap trang thai moi (1-TRONG, 2-DA_DAT, 3-DANG_SU_DUNG, 4-BAO_TRI): ");
                int tt = Integer.parseInt(sc.nextLine());
                if (tt == 1) danhSach[i].setTrangThai(TrangThaiPhong.TRONG);
                else if (tt == 2) danhSach[i].setTrangThai(TrangThaiPhong.DA_DAT);
                else if (tt == 3) danhSach[i].setTrangThai(TrangThaiPhong.DANG_SU_DUNG);
                else danhSach[i].setTrangThai(TrangThaiPhong.BAO_TRI);
                
                System.out.println("=> Da sua thanh cong!");
                return;
            }
        }
        System.out.println("=> Khong tim thay phong de sua!");
    }

    public void xoaPhong(String maPhong) {
        int viTri = -1;
        for (int i = 0; i < soLuong; i++) {
            if (danhSach[i].getMaPhong().equalsIgnoreCase(maPhong)) {
                viTri = i;
                break;
            }
        }
        if (viTri != -1) {
            for (int i = viTri; i < soLuong - 1; i++) {
                danhSach[i] = danhSach[i + 1];
            }
            soLuong--;
            System.out.println("=> Da xoa phong " + maPhong);
        } else {
            System.out.println("=> Khong tim thay phong!");
        }
    }

    public void timKiem(String khoa) {
        boolean timThay = false;
        System.out.println("--- KET QUA TIM KIEM CHO: " + khoa + " ---");
        for (int i = 0; i < soLuong; i++) {
            if (danhSach[i].getMaPhong().equalsIgnoreCase(khoa) || danhSach[i].getSoPhong().contains(khoa)) {
                danhSach[i].hienThiThongTin();
                timThay = true;
            }
        }
        if (!timThay) System.out.println("=> Khong tim thay!");
    }

    public void thongKe() {
        int trong = 0, daDat = 0, dangDung = 0, baoTri = 0;
        int tieuChuan = 0, caoCap = 0, suite = 0;

        for (int i = 0; i < soLuong; i++) {
            if (danhSach[i].getTrangThai() == TrangThaiPhong.TRONG) trong++;
            else if (danhSach[i].getTrangThai() == TrangThaiPhong.DA_DAT) daDat++;
            else if (danhSach[i].getTrangThai() == TrangThaiPhong.DANG_SU_DUNG) dangDung++;
            else baoTri++;

            if (danhSach[i] instanceof PhongTieuChuan) tieuChuan++;
            else if (danhSach[i] instanceof PhongCaoCap) caoCap++;
            else if (danhSach[i] instanceof PhongSuite) suite++;
        }
        System.out.println("--- THONG KE ---");
        System.out.println("Theo trang thai: Trong(" + trong + ") | Da dat(" + daDat + ") | Dang dung(" + dangDung + ") | Bao tri(" + baoTri + ")");
        System.out.println("Theo loai phong: Tieu chuan(" + tieuChuan + ") | Cao cap(" + caoCap + ") | Suite(" + suite + ")");
    }

    public void tinhTongTienDuKien() {
        double tong = 0;
        for (int i = 0; i < soLuong; i++) {
            tong += danhSach[i].tinhTienPhong(1); // Da hinh
        }
        System.out.println("=> Tong tien du kien thu duoc (1 dem): " + tong + " VND");
    }

    public void ghiFile(String tenFile) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(tenFile))) {
            for (int i = 0; i < soLuong; i++) {
                writer.println(danhSach[i].toCSV());
            }
            System.out.println("=> Da ghi " + soLuong + " phong vao file " + tenFile);
        } catch (Exception e) {
            System.out.println("=> Loi ghi file!");
        }
    }

    public void docFile(String tenFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(tenFile))) {
            String line;
            soLuong = 0; // Reset lai danh sach truoc khi doc
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String loai = parts[0];
                
                String ma = parts[1];
                String so = parts[2];
                int tang = Integer.parseInt(parts[3]);
                double gia = Double.parseDouble(parts[4]);
                TrangThaiPhong tt = TrangThaiPhong.valueOf(parts[5]);
                int nguoi = Integer.parseInt(parts[6]);
                int giuong = Integer.parseInt(parts[7]); // Da sua loi chinh ta
                double dt = Double.parseDouble(parts[8]);

                Phong p = null;
                if (loai.equals("TieuChuan")) {
                    p = new PhongTieuChuan(ma, so, tang, gia, tt, nguoi, giuong, dt);
                } else if (loai.equals("CaoCap")) {
                    p = new PhongCaoCap(ma, so, tang, gia, tt, nguoi, giuong, dt, Boolean.parseBoolean(parts[9]));
                } else if (loai.equals("Suite")) {
                    p = new PhongSuite(ma, so, tang, gia, tt, nguoi, giuong, dt, Boolean.parseBoolean(parts[9]), Boolean.parseBoolean(parts[10]));
                }
                if (p != null) themPhong(p);
            }
            System.out.println("=> Da doc " + soLuong + " phong tu file!");
        } catch (Exception e) {
            System.out.println("=> Loi doc file! (Co the file chua ton tai)");
        }
    }
}

// 3. CLASS MAIN CHINH (Phai co chu public va trung ten file roomlist.java)
public class roomlist {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        QuanLyPhong ql = new QuanLyPhong();

        // a) Nhap n phan tu moi dau tien
        System.out.print("Nhap so luong phong ban dau (n): ");
        int n = 0;
        try {
            n = Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            System.out.println("Loi nhap, mac dinh n = 0.");
        }

        for (int i = 0; i < n; i++) {
            System.out.println("--- Nhap phong thu " + (i + 1) + " ---");
                ql.themPhong(sc);
        }

        // MENU CHINH
        int chon = -1;
        do {
            System.out.println("\n========== MENU QUAN LY PHONG ==========");
            System.out.println("1. Xuat danh sach phong");
            System.out.println("2. Them moi 1 phong");
            System.out.println("3. Sua phong theo ma");
            System.out.println("4. Xoa phong theo ma");
            System.out.println("5. Tim kiem phong (theo ma hoac so phong)");
            System.out.println("6. Thong ke so lieu");
            System.out.println("7. Tinh tong tien du kien");
            System.out.println("8. Ghi file");
            System.out.println("9. Doc file");
            System.out.println("0. Thoat");
            System.out.print("Chon chuc nang: ");
            
            try {
                chon = Integer.parseInt(sc.nextLine());
                switch (chon) {
                    case 1: 
                        ql.xuatDanhSach(); 
                        break;
                    case 2: 
                        // DA HOAN THIEN CHUC NANG THEM 1 PHONG
                        System.out.println("--- Them moi 1 phong ---");
                        System.out.print("Chon loai (1-Tieu chuan, 2-Cao cap, 3-Suite): ");
                        int loai = Integer.parseInt(sc.nextLine());
                        
                        System.out.print("Ma phong: "); String ma = sc.nextLine();
                        System.out.print("So phong: "); String so = sc.nextLine();
                        System.out.print("Tang: "); int tang = Integer.parseInt(sc.nextLine());
                        System.out.print("Gia co ban: "); double gia = Double.parseDouble(sc.nextLine());
                        
                        System.out.print("Trang thai (1-TRONG, 2-DA_DAT, 3-DANG_SU_DUNG, 4-BAO_TRI): ");
                        int tt = Integer.parseInt(sc.nextLine());
                        TrangThaiPhong trangThai = TrangThaiPhong.TRONG;
                        if (tt == 2) trangThai = TrangThaiPhong.DA_DAT;
                        else if (tt == 3) trangThai = TrangThaiPhong.DANG_SU_DUNG;
                        else if (tt == 4) trangThai = TrangThaiPhong.BAO_TRI;

                        System.out.print("So nguoi toi da: "); int nguoi = Integer.parseInt(sc.nextLine());
                        System.out.print("So giuong: "); int giuong = Integer.parseInt(sc.nextLine());
                        System.out.print("Dien tich: "); double dt = Double.parseDouble(sc.nextLine());

                        if (loai == 1) {
                            ql.themPhong(new PhongTieuChuan(ma, so, tang, gia, trangThai, nguoi, giuong, dt));
                        } else if (loai == 2) {
                            System.out.print("Co ban cong? (true/false): ");
                            boolean bc = Boolean.parseBoolean(sc.nextLine());
                            ql.themPhong(new PhongCaoCap(ma, so, tang, gia, trangThai, nguoi, giuong, dt, bc));
                        } else if (loai == 3) {
                            System.out.print("Co phong khach? (true/false): ");
                            boolean pk = Boolean.parseBoolean(sc.nextLine());
                            System.out.print("Co bon tam? (true/false): ");
                            boolean bt = Boolean.parseBoolean(sc.nextLine());
                            ql.themPhong(new PhongSuite(ma, so, tang, gia, trangThai, nguoi, giuong, dt, pk, bt));
                        }
                        break;
                    case 3: 
                        System.out.print("Nhap ma phong can sua: ");
                        ql.suaPhong(sc.nextLine(),sc); 
                        break;
                    case 4: 
                        System.out.print("Nhap ma phong can xoa: ");
                        ql.xoaPhong(sc.nextLine()); 
                        break;
                    case 5: 
                        System.out.print("Nhap tu khoa (Ma hoac So phong): ");
                        ql.timKiem(sc.nextLine()); 
                        break;
                    case 6: 
                        ql.thongKe(); 
                        break;
                    case 7: 
                        ql.tinhTongTienDuKien(); 
                        break;
                    case 8: 
                        ql.ghiFile("data_phong.txt"); 
                        break;
                    case 9: 
                        ql.docFile("data_phong.txt"); 
                        break;
                    case 0: 
                        System.out.println("Tam biet!"); 
                        break;
                    default: 
                        System.out.println("Chon sai!");
                }
            } catch (Exception e) {
                System.out.println("Loi nhap lieu! Vui long nhap dung dinh dang.");
            }
        } while (chon != 0);
        sc.close();
    }
}
