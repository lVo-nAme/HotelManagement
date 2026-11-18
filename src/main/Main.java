package main;

import person.QuanLyNguoi;

public class Main {
    public static void main(String[] args) {
        QuanLyNguoi quanLyNguoi = new QuanLyNguoi();
        // Gọi hàm kiểm tra chức vụ khi chạy Main (yêu cầu chọn Role: Quản lý, Nhân viên, Khách hàng)
        quanLyNguoi.kiemTraChucVu();
    }
}
