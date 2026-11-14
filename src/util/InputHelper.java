package util;

import java.util.Scanner;

public class InputHelper {
    // Khởi tạo một Scanner tĩnh duy nhất dùng chung cho toàn bộ ứng dụng
    private static final Scanner SCANNER = new Scanner(System.in);

    /**
     * Đọc một chuỗi ký tự từ bàn phím, đảm bảo không bị để trống.
     * @param prompt Thông báo hiển thị cho người dùng
     * @return Chuỗi dữ liệu hợp lệ
     */
    public static String getString(String prompt) {
        String result;
        while (true) {
            System.out.print(prompt);
            result = SCANNER.nextLine().trim();
            if (!result.isEmpty()) {
                return result;
            }
            System.out.println("⚠️ Lỗi: Không được để trống. Vui lòng nhập lại!");
        }
    }

    /**
     * Đọc một số nguyên hợp lệ từ bàn phím (bắt lỗi nếu nhập chữ hoặc ký tự đặc biệt).
     * @param prompt Thông báo hiển thị
     * @return Số nguyên hợp lệ
     */
    public static int getInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String line = SCANNER.nextLine().trim();
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Lỗi: Vui lòng nhập vào một số nguyên hợp lệ!");
            }
        }
    }

    /**
     * Đọc một số nguyên nằm trong một khoảng cho phép (Ví dụ: dùng cho Menu lựa chọn).
     * @param prompt Thông báo hiển thị
     * @param min Giá trị tối thiểu (bao gồm)
     * @param max Giá trị tối đa (bao gồm)
     * @return Số nguyên trong khoảng [min, max]
     */
    public static int getIntInRange(String prompt, int min, int max) {
        while (true) {
            int value = getInt(prompt);
            if (value >= min && value <= max) {
                return value;
            }
            System.out.println("⚠️ Lỗi: Giá trị phải nằm trong khoảng từ " + min + " đến " + max + "!");
        }
    }

    /**
     * Đọc một số thực (double) hợp lệ từ bàn phím (dùng cho giá tiền, diện tích, v.v.).
     * @param prompt Thông báo hiển thị
     * @return Số thực hợp lệ
     */
    public static double getDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String line = SCANNER.nextLine().trim();
                return Double.parseDouble(line);
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Lỗi: Vui lòng nhập vào một số thực hợp lệ!");
            }
        }
    }
}