package management;

import booking.Service;
import fileio.FileHandler;
import util.Format;
import util.InputHelper;

/**
 * Lop mang doi tuong Service[] (khong dung ArrayList, Vector, LinkedList).
 */
public class ServiceList {
    private static final String FILE = "services.txt";
    private static final int MAX = 100;

    private Service[] services = new Service[MAX];
    private int count = 0;

    public void load() {
        count = 0;
        String[] lines = FileHandler.readLinesFromFile(FILE);
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (line == null || line.trim().isEmpty() || line.startsWith("#")) {
                continue;
            }
            Service s = Service.fromFileString(line);
            if (s != null && count < MAX) {
                services[count++] = s;
            }
        }
    }

    public void save() {
        String[] lines = new String[count];
        for (int i = 0; i < count; i++) {
            lines[i] = services[i].toFileString();
        }
        FileHandler.writeLinesToFile(FILE, lines, false);
    }

    public Service[] getAll() {
        Service[] result = new Service[count];
        for (int i = 0; i < count; i++) {
            result[i] = services[i];
        }
        return result;
    }

    public int getCount() {
        return count;
    }

    public String nextId() {
        String[] ids = new String[count];
        for (int i = 0; i < count; i++) {
            ids[i] = services[i].getServiceId();
        }
        return Format.nextId("DV", ids, count);
    }

    public Service findById(String id) {
        if (id == null) return null;
        for (int i = 0; i < count; i++) {
            if (services[i].getServiceId().equalsIgnoreCase(id.trim())) {
                return services[i];
            }
        }
        return null;
    }

    public boolean add(Service s) {
        if (s == null || count >= MAX) {
            return false;
        }
        services[count++] = s;
        return true;
    }

    public boolean remove(String id) {
        if (id == null) return false;
        for (int i = 0; i < count; i++) {
            if (services[i].getServiceId().equalsIgnoreCase(id.trim())) {
                for (int j = i; j < count - 1; j++) {
                    services[j] = services[j + 1];
                }
                services[--count] = null;
                return true;
            }
        }
        return false;
    }

    /**
     * Nhap thong tin 1 dich vu tu ban phim. Ma dich vu duoc sinh tu dong (nextId)
     * nen khong co rui ro trung ma khi nhap hang loat.
     */
    private Service nhapMotDichVu() {
        String name = InputHelper.getString("Ten dich vu: ");
        double price = InputHelper.getPositiveDouble("Don gia (VND): ");
        String unit = InputHelper.getString("Don vi tinh (chai, kg, suat...): ");
        return new Service(nextId(), name, price, unit);
    }

    public void nhapDanhSachBanDau() {
        System.out.println("\n--- NHAP DANH SACH DICH VU BAN DAU (n PHAN TU) ---");
        if (count > 0) {
            System.out.println("=> Danh sach hien da co " + count + " dich vu. Du lieu cu se duoc giu nguyen.");
            if (!InputHelper.getYesNo("Ban co muon tiep tuc nhap them danh sach ban dau khong?")) {
                System.out.println("=> Da huy thao tac nhap danh sach ban dau.");
                return;
            }
        }
        if (count >= MAX) {
            System.out.println("=> Danh sach dich vu da day (toi da " + MAX + "), khong the nhap them!");
            return;
        }
        int n;
        while (true) {
            n = InputHelper.getPositiveInt("Nhap so luong dich vu can nhap (n): ");
            if (n <= MAX - count) {
                break;
            }
            System.out.println("=> Loi: Danh sach chi con cho toi da " + (MAX - count) + " dich vu, vui long nhap lai!");
        }
        int daThem = 0;
        for (int i = 1; i <= n; i++) {
            System.out.println("\n-- Nhap dich vu thu " + i + "/" + n + " --");
            if (add(nhapMotDichVu())) {
                daThem++;
            }
        }
        save();
        System.out.println("=> Da nhap thanh cong " + daThem + "/" + n + " dich vu. Tong so dich vu hien tai: " + count);
    }

    public void themNhieu() {
        System.out.println("\n--- THEM MOI K DICH VU ---");
        if (count >= MAX) {
            System.out.println("=> Danh sach dich vu da day (toi da " + MAX + "), khong the them!");
            return;
        }
        int k;
        while (true) {
            k = InputHelper.getPositiveInt("Nhap so luong dich vu can them (k): ");
            if (k <= MAX - count) {
                break;
            }
            System.out.println("=> Loi: Danh sach chi con cho toi da " + (MAX - count) + " dich vu, vui long nhap lai!");
        }
        int daThem = 0;
        for (int i = 1; i <= k; i++) {
            System.out.println("\n-- Them dich vu thu " + i + "/" + k + " --");
            if (add(nhapMotDichVu())) {
                daThem++;
            }
        }
        save();
        System.out.println("=> Da them thanh cong " + daThem + "/" + k + " dich vu moi. Tong so dich vu hien tai: " + count);
    }
}
