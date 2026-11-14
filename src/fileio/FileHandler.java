package fileio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileHandler {

    // Đường dẫn thư mục chứa dữ liệu mặc định
    private static final String DATA_DIR = "data";

    /**
     * Đảm bảo thư mục chứa file luôn tồn tại trước khi đọc/ghi
     */
    private static void ensureDirectoryExists() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * Ghi một mảng các dòng dữ liệu vào file text (Ghi đè hoặc ghi nối).
     * 
     * @param fileName Tên file (ví dụ: "customers.txt")
     * @param lines    Mảng các chuỗi cần ghi (mỗi phần tử tương ứng 1 dòng)
     * @param append   true nếu muốn ghi nối tiếp (append), false nếu muốn ghi đè (overwrite)
     */
    public static void writeLinesToFile(String fileName, String[] lines, boolean append) {
        ensureDirectoryExists();
        File file = new File(DATA_DIR, fileName);

        try (PrintWriter writer = new PrintWriter(new FileWriter(file, append))) {
            for (String line : lines) {
                if (line != null) {
                    writer.println(line);
                }
            }
            System.out.println("💾 Đã ghi dữ liệu thành công vào file: " + file.getPath());
        } catch (IOException e) {
            System.out.println("⚠️ Lỗi khi ghi file " + fileName + ": " + e.getMessage());
        }
    }

    /**
     * Đọc toàn bộ nội dung file text và trả về danh sách các dòng dưới dạng mảng String.
     * 
     * @param fileName Tên file cần đọc (ví dụ: "customers.txt")
     * @return Mảng các chuỗi (mỗi phần tử là 1 dòng trong file), hoặc mảng rỗng nếu file không tồn tại.
     */
    public static String[] readLinesFromFile(String fileName) {
        File file = new File(DATA_DIR, fileName);
        if (!file.exists()) {
            // Đã fix: Bổ sung dấu ngoặc kép cho chuỗi thông báo
            System.out.println("⚠️ File " + fileName + " chưa tồn tại trong thư mục " + DATA_DIR + "!");
            return new String[0];
        }

        // Đếm số dòng trước để tạo mảng tĩnh có kích thước chính xác
        int lineCount = countLines(file);
        String[] lines = new String[lineCount];
        int index = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                lines[index++] = currentLine;
            }
            System.out.println("📂 Đã đọc thành công " + lineCount + " dòng từ file: " + file.getPath());
        } catch (IOException e) {
            System.out.println("⚠️ Lỗi khi đọc file " + fileName + ": " + e.getMessage());
        }

        return lines;
    }

    /**
     * Hàm phụ trợ đếm tổng số dòng trong file
     */
    private static int countLines(File file) {
        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.readLine() != null) {
                count++;
            }
        } catch (IOException e) {
            // Bỏ qua nếu lỗi đếm
        }
        return count;
    }
}