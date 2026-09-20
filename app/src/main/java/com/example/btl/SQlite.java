package com.example.btl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.btl.thongke.DoanhThuTheoThang;
import com.example.btl.thongke.ThongKeTongQuan;
import com.example.cafetable.model.CafeTable;
import com.example.coffee.GioHang;

import java.util.ArrayList;
import java.util.List;

public class SQlite extends SQLiteOpenHelper {
    public static final String dbName = "csdl-coffee.sqlite";
    public static final int version = 8;

    public SQlite(@Nullable Context context) {
        super(context, dbName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys = ON");
        db.execSQL("CREATE TABLE TaiKhoan (" +
                "    MaTK INTEGER PRIMARY KEY ," +
                "    TenDangNhap TEXT UNIQUE NOT NULL," +
                "    MatKhau TEXT NOT NULL," +
                "    VaiTro INTEGER NOT NULL" +
                ")");
        db.execSQL("CREATE TABLE NhanVien (" +
                "    MaNV TEXT PRIMARY KEY ," +
                "    HoTen TEXT NOT NULL," +
                "    GioiTinh TEXT," +
                "    NgaySinh TEXT," +
                "    SoDienThoai TEXT," +
                "    DiaChi TEXT," +
                "    MaTK INTEGER," +
                "    FOREIGN KEY (MaTK) REFERENCES TaiKhoan(MaTK)" +
                ");");
        db.execSQL("CREATE TABLE DoUong (" +
                "    MaDU INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    TenDoUong TEXT NOT NULL," +
                "    DonGia REAL NOT NULL," +
                "    MoTa TEXT," +
                "    HinhAnh TEXT," +
                "    DanhMuc TEXT DEFAULT ''," +
                "    ConBan INTEGER DEFAULT 1" +
                ");");
        db.execSQL("CREATE TABLE Ban (" +
                "    MaBan INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    TenBan TEXT NOT NULL," +
                "    SoCho INTEGER DEFAULT 4," +
                "    TrangThai TEXT DEFAULT 'Trong'," +
                "    GhiChu TEXT DEFAULT ''" +
                ");");
        db.execSQL("CREATE TABLE GioHang (" +
                "    MaGH INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    MaBan INTEGER," +
                "    NgayTao TEXT DEFAULT CURRENT_TIMESTAMP," +
                "    TrangThai INTEGER DEFAULT 1," +
                "    FOREIGN KEY (MaBan) REFERENCES Ban(MaBan)" +
                ");");
        db.execSQL("CREATE TABLE ChiTietGioHang (" +
                "    MaGH INTEGER," +
                "    MaDU INTEGER," +
                "    SoLuong INTEGER NOT NULL," +
                "    DonGia REAL NOT NULL," +
                "    ThanhTien REAL ," +
                "    GhiChu TEXT DEFAULT ''," +
                "    PRIMARY KEY (MaGH, MaDU)," +
                "    FOREIGN KEY (MaGH) REFERENCES GioHang(MaGH)," +
                "    FOREIGN KEY (MaDU) REFERENCES DoUong(MaDU)" +
                ");");
        db.execSQL("CREATE TABLE HoaDon (" +
                "    MaHD INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    MaBan INTEGER," +
                "    NgayLap TEXT DEFAULT CURRENT_TIMESTAMP," +
                "    TongTien REAL," +
                "    TrangThai TEXT DEFAULT 'Da thanh toan'," +
                "    FOREIGN KEY (MaBan) REFERENCES Ban(MaBan)" +
                ");");
        db.execSQL("CREATE TABLE ChiTietHoaDon (" +
                "    MaHD INTEGER," +
                "    MaDU INTEGER," +
                "    TenDoUong TEXT," +
                "    SoLuong INTEGER," +
                "    DonGia REAL," +
                "    ThanhTien REAL ," +
                "    PRIMARY KEY (MaHD, MaDU)," +
                "    FOREIGN KEY (MaHD) REFERENCES HoaDon(MaHD)," +
                "    FOREIGN KEY (MaDU) REFERENCES DoUong(MaDU)" +
                ");");

        seedBaseData(db);
        db.execSQL("INSERT INTO DoUong(TenDoUong, DonGia, MoTa, HinhAnh, DanhMuc, ConBan) VALUES ('Ca phe den', 20000, 'Ca phe nguyen chat pha phin', 'cafeden', 'Ca phe', 1)");
        db.execSQL("INSERT INTO DoUong(TenDoUong, DonGia, MoTa, HinhAnh, DanhMuc, ConBan) VALUES ('Ca phe sua', 25000, 'Ca phe sua da truyen thong', 'cafesua', 'Ca phe', 1)");
        db.execSQL("INSERT INTO DoUong(TenDoUong, DonGia, MoTa, HinhAnh, DanhMuc, ConBan) VALUES ('Bac xiu', 25000, 'Nhieu sua, it ca phe', 'bacxiu', 'Ca phe', 1)");
        db.execSQL("INSERT INTO DoUong(TenDoUong, DonGia, MoTa, HinhAnh, DanhMuc, ConBan) VALUES ('Ca phe trung', 35000, 'Thom, ngay', 'cafetrung', 'Ca phe', 1)");
        db.execSQL("INSERT INTO DoUong(TenDoUong, DonGia, MoTa, HinhAnh, DanhMuc, ConBan) VALUES ('Matcha', 30000, 'Matcha nguyen chat', 'matcha', 'Tra', 1)");
        db.execSQL("INSERT INTO DoUong(TenDoUong, DonGia, MoTa, HinhAnh, DanhMuc, ConBan) VALUES ('Sinh to bo', 40000, 'Bo sap beo ngay', 'sinhtobo', 'Sinh to', 1)");
        db.execSQL("INSERT INTO DoUong(TenDoUong, DonGia, MoTa, HinhAnh, DanhMuc, ConBan) VALUES ('Americano', 30000, 'Ca phe kieu My', 'americado', 'Ca phe', 1)");
        db.execSQL("INSERT INTO DoUong(TenDoUong, DonGia, MoTa, HinhAnh, DanhMuc, ConBan) VALUES ('Banh my que', 15000, 'Banh my que nong gion', 'banhmyque', 'Banh ngot', 1)");
        db.execSQL("INSERT INTO DoUong(TenDoUong, DonGia, MoTa, HinhAnh, DanhMuc, ConBan) VALUES ('Banh pho mai tra xanh', 35000, 'Banh ngot vi tra xanh', 'banhphomaitraxanh', 'Banh ngot', 1)");
        db.execSQL("INSERT INTO DoUong(TenDoUong, DonGia, MoTa, HinhAnh, DanhMuc, ConBan) VALUES ('Banh tiramisu', 35000, 'Banh tiramisu mem min', 'banhtiramisu', 'Banh ngot', 1)");
        db.execSQL("INSERT INTO DoUong(TenDoUong, DonGia, MoTa, HinhAnh, DanhMuc, ConBan) VALUES ('Banh croissant', 25000, 'Banh croissant bo thom', 'bannhcroissant', 'Banh ngot', 1)");
        db.execSQL("INSERT INTO DoUong(TenDoUong, DonGia, MoTa, HinhAnh, DanhMuc, ConBan) VALUES ('Ca phe phin den da', 28000, 'Ca phe phin dam vi', 'caphephindenda', 'Ca phe', 1)");
        db.execSQL("INSERT INTO DoUong(TenDoUong, DonGia, MoTa, HinhAnh, DanhMuc, ConBan) VALUES ('Ca phe phin sua da', 30000, 'Ca phe phin sua da', 'caphephinsuada', 'Ca phe', 1)");
        db.execSQL("INSERT INTO DoUong(TenDoUong, DonGia, MoTa, HinhAnh, DanhMuc, ConBan) VALUES ('Cappuccino', 40000, 'Ca phe sua bot min', 'cappuccino', 'Ca phe', 1)");
        db.execSQL("INSERT INTO DoUong(TenDoUong, DonGia, MoTa, HinhAnh, DanhMuc, ConBan) VALUES ('Huong duong', 20000, 'Nuoc hat huong duong', 'huongduong', 'Hat', 1)");
        db.execSQL("INSERT INTO DoUong(TenDoUong, DonGia, MoTa, HinhAnh, DanhMuc, ConBan) VALUES ('Latte', 38000, 'Latte mem vi sua', 'lattle', 'Ca phe', 1)");
        db.execSQL("INSERT INTO DoUong(TenDoUong, DonGia, MoTa, HinhAnh, DanhMuc, ConBan) VALUES ('Matcha latte', 38000, 'Matcha latte beo nhe', 'matchalattle', 'Tra', 1)");
        db.execSQL("INSERT INTO DoUong(TenDoUong, DonGia, MoTa, HinhAnh, DanhMuc, ConBan) VALUES ('Matcha oreo kem tuoi', 42000, 'Matcha kem tuoi oreo', 'matchaoreokemtuoi', 'Da xay', 1)");
        db.execSQL("INSERT INTO DoUong(TenDoUong, DonGia, MoTa, HinhAnh, DanhMuc, ConBan) VALUES ('Mocha', 40000, 'Mocha ngot nhe', 'mocha', 'Ca phe', 1)");
        seedStatisticDataIfNeeded(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        ensureDrinkSchema(db);
        ensureBanSchema(db);
        ensureCartSchema(db);
        ensureInvoiceDetailSchema(db);
        if (getRowCount(db, "DoUong") == 0) {
            db.execSQL("INSERT INTO DoUong(TenDoUong, DonGia, MoTa, HinhAnh, DanhMuc, ConBan) VALUES ('Ca phe den', 20000, 'Ca phe nguyen chat pha phin', 'cafeden', 'Ca phe', 1)");
            db.execSQL("INSERT INTO DoUong(TenDoUong, DonGia, MoTa, HinhAnh, DanhMuc, ConBan) VALUES ('Ca phe sua', 25000, 'Ca phe sua da truyen thong', 'cafesua', 'Ca phe', 1)");
            db.execSQL("INSERT INTO DoUong(TenDoUong, DonGia, MoTa, HinhAnh, DanhMuc, ConBan) VALUES ('Bac xiu', 25000, 'Nhieu sua, it ca phe', 'bacxiu', 'Ca phe', 1)");
            db.execSQL("INSERT INTO DoUong(TenDoUong, DonGia, MoTa, HinhAnh, DanhMuc, ConBan) VALUES ('Ca phe trung', 35000, 'Thom, ngay', 'cafetrung', 'Ca phe', 1)");
            db.execSQL("INSERT INTO DoUong(TenDoUong, DonGia, MoTa, HinhAnh, DanhMuc, ConBan) VALUES ('Matcha', 30000, 'Matcha nguyen chat', 'matcha', 'Tra', 1)");
            db.execSQL("INSERT INTO DoUong(TenDoUong, DonGia, MoTa, HinhAnh, DanhMuc, ConBan) VALUES ('Sinh to bo', 40000, 'Bo sap beo ngay', 'sinhtobo', 'Sinh to', 1)");
            db.execSQL("INSERT INTO DoUong(TenDoUong, DonGia, MoTa, HinhAnh, DanhMuc, ConBan) VALUES ('Americano', 30000, 'Ca phe kieu My', 'americado', 'Ca phe', 1)");
            db.execSQL("INSERT INTO DoUong(TenDoUong, DonGia, MoTa, HinhAnh, DanhMuc, ConBan) VALUES ('Banh my que', 15000, 'Banh my que nong gion', 'banhmyque', 'Banh ngot', 1)");
            db.execSQL("INSERT INTO DoUong(TenDoUong, DonGia, MoTa, HinhAnh, DanhMuc, ConBan) VALUES ('Banh pho mai tra xanh', 35000, 'Banh ngot vi tra xanh', 'banhphomaitraxanh', 'Banh ngot', 1)");
            db.execSQL("INSERT INTO DoUong(TenDoUong, DonGia, MoTa, HinhAnh, DanhMuc, ConBan) VALUES ('Banh tiramisu', 35000, 'Banh tiramisu mem min', 'banhtiramisu', 'Banh ngot', 1)");
            db.execSQL("INSERT INTO DoUong(TenDoUong, DonGia, MoTa, HinhAnh, DanhMuc, ConBan) VALUES ('Banh croissant', 25000, 'Banh croissant bo thom', 'bannhcroissant', 'Banh ngot', 1)");
            db.execSQL("INSERT INTO DoUong(TenDoUong, DonGia, MoTa, HinhAnh, DanhMuc, ConBan) VALUES ('Ca phe phin den da', 28000, 'Ca phe phin dam vi', 'caphephindenda', 'Ca phe', 1)");
            db.execSQL("INSERT INTO DoUong(TenDoUong, DonGia, MoTa, HinhAnh, DanhMuc, ConBan) VALUES ('Ca phe phin sua da', 30000, 'Ca phe phin sua da', 'caphephinsuada', 'Ca phe', 1)");
            db.execSQL("INSERT INTO DoUong(TenDoUong, DonGia, MoTa, HinhAnh, DanhMuc, ConBan) VALUES ('Cappuccino', 40000, 'Ca phe sua bot min', 'cappuccino', 'Ca phe', 1)");
            db.execSQL("INSERT INTO DoUong(TenDoUong, DonGia, MoTa, HinhAnh, DanhMuc, ConBan) VALUES ('Huong duong', 20000, 'Nuoc hat huong duong', 'huongduong', 'Hat', 1)");
            db.execSQL("INSERT INTO DoUong(TenDoUong, DonGia, MoTa, HinhAnh, DanhMuc, ConBan) VALUES ('Latte', 38000, 'Latte mem vi sua', 'lattle', 'Ca phe', 1)");
            db.execSQL("INSERT INTO DoUong(TenDoUong, DonGia, MoTa, HinhAnh, DanhMuc, ConBan) VALUES ('Matcha latte', 38000, 'Matcha latte beo nhe', 'matchalattle', 'Tra', 1)");
            db.execSQL("INSERT INTO DoUong(TenDoUong, DonGia, MoTa, HinhAnh, DanhMuc, ConBan) VALUES ('Matcha oreo kem tuoi', 42000, 'Matcha kem tuoi oreo', 'matchaoreokemtuoi', 'Da xay', 1)");
            db.execSQL("INSERT INTO DoUong(TenDoUong, DonGia, MoTa, HinhAnh, DanhMuc, ConBan) VALUES ('Mocha', 40000, 'Mocha ngot nhe', 'mocha', 'Ca phe', 1)");
        }
        seedStatisticDataIfNeeded(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 4) {
            seedStatisticDataIfNeeded(db);
        }
        if (oldVersion < 5) {
            ensureInvoiceDetailSchema(db);
            seedInvoiceDetailsIfNeeded(db);
        }
        if (oldVersion < 6) {
            ensureBanSchema(db);
        }
        if (oldVersion < 7) {
            ensureDrinkSchema(db);
        }
        if (oldVersion < 8) {
            ensureCartSchema(db);
        }
    }

    private void seedBaseData(SQLiteDatabase db) {
        db.execSQL("INSERT INTO TaiKhoan(TenDangNhap,MatKhau,VaiTro) VALUES ('admin','123',1)");
        db.execSQL("INSERT INTO TaiKhoan(TenDangNhap,MatKhau,VaiTro) VALUES ('nv1','123',2)");

        db.execSQL("INSERT INTO NhanVien(MaNV,HoTen,GioiTinh,NgaySinh,SoDienThoai,DiaChi) VALUES ('NV01','Nguyen Van An','Nam','10/01/2000','0911111111','Ha Noi')");
        db.execSQL("INSERT INTO NhanVien(MaNV,HoTen,GioiTinh,NgaySinh,SoDienThoai,DiaChi) VALUES ('NV02','Tran Thi Binh','Nu','15/02/2001','0922222222','Hai Phong')");
        db.execSQL("INSERT INTO NhanVien(MaNV,HoTen,GioiTinh,NgaySinh,SoDienThoai,DiaChi) VALUES ('NV03','Le Van Cuong','Nam','20/03/1999','0933333333','Nam Dinh')");
        db.execSQL("INSERT INTO NhanVien(MaNV,HoTen,GioiTinh,NgaySinh,SoDienThoai,DiaChi) VALUES ('NV04','Pham Thi Dung','Nu','05/04/2000','0944444444','Thanh Hoa')");
        db.execSQL("INSERT INTO NhanVien(MaNV,HoTen,GioiTinh,NgaySinh,SoDienThoai,DiaChi) VALUES ('NV05','Hoang Van Em','Nam','12/05/1998','0955555555','Nghe An')");
        db.execSQL("INSERT INTO NhanVien(MaNV,HoTen,GioiTinh,NgaySinh,SoDienThoai,DiaChi) VALUES ('NV06','Vu Thi Hoa','Nu','25/06/2001','0966666666','Ha Nam')");
        db.execSQL("INSERT INTO NhanVien(MaNV,HoTen,GioiTinh,NgaySinh,SoDienThoai,DiaChi) VALUES ('NV07','Do Van Khanh','Nam','18/07/1999','0977777777','Bac Ninh')");
        db.execSQL("INSERT INTO NhanVien(MaNV,HoTen,GioiTinh,NgaySinh,SoDienThoai,DiaChi) VALUES ('NV08','Bui Thi Lan','Nu','30/08/2002','0988888888','Hung Yen')");
        db.execSQL("INSERT INTO NhanVien(MaNV,HoTen,GioiTinh,NgaySinh,SoDienThoai,DiaChi) VALUES ('NV09','Ngo Van Minh','Nam','09/09/2000','0999999999','Quang Ninh')");
        db.execSQL("INSERT INTO NhanVien(MaNV,HoTen,GioiTinh,NgaySinh,SoDienThoai,DiaChi) VALUES ('NV10','Mai Thi Ngoc','Nu','22/10/2001','0900000000','Ha Noi')");

        db.execSQL("INSERT INTO TaiKhoan(TenDangNhap,MatKhau,VaiTro) VALUES ('NV01','10/01/2000',2)");
        db.execSQL("INSERT INTO TaiKhoan(TenDangNhap,MatKhau,VaiTro) VALUES ('NV02','15/02/2001',2)");
        db.execSQL("INSERT INTO TaiKhoan(TenDangNhap,MatKhau,VaiTro) VALUES ('NV03','20/03/1999',2)");
        db.execSQL("INSERT INTO TaiKhoan(TenDangNhap,MatKhau,VaiTro) VALUES ('NV04','05/04/2000',2)");
        db.execSQL("INSERT INTO TaiKhoan(TenDangNhap,MatKhau,VaiTro) VALUES ('NV05','12/05/1998',2)");
        db.execSQL("INSERT INTO TaiKhoan(TenDangNhap,MatKhau,VaiTro) VALUES ('NV06','25/06/2001',2)");
        db.execSQL("INSERT INTO TaiKhoan(TenDangNhap,MatKhau,VaiTro) VALUES ('NV07','18/07/1999',2)");
        db.execSQL("INSERT INTO TaiKhoan(TenDangNhap,MatKhau,VaiTro) VALUES ('NV08','30/08/2002',2)");
        db.execSQL("INSERT INTO TaiKhoan(TenDangNhap,MatKhau,VaiTro) VALUES ('NV09','09/09/2000',2)");
        db.execSQL("INSERT INTO TaiKhoan(TenDangNhap,MatKhau,VaiTro) VALUES ('NV10','22/10/2001',2)");
    }

    private void seedStatisticDataIfNeeded(SQLiteDatabase db) {
        if (getRowCount(db, "Ban") == 0) {
            for (int i = 1; i <= 6; i++) {
                ContentValues values = new ContentValues();
                values.put("TenBan", "Ban " + i);
                values.put("SoCho", 4);
                values.put("TrangThai", "Trong");
                values.put("GhiChu", "");
                db.insert("Ban", null, values);
            }
        }

        if (getRowCount(db, "HoaDon") > 0) {
            seedInvoiceDetailsIfNeeded(db);
            return;
        }

        insertInvoice(db, 1, "2024-01-15 08:15:00", 120000);
        insertInvoice(db, 2, "2024-03-10 09:20:00", 185000);
        insertInvoice(db, 1, "2024-06-21 13:10:00", 265000);
        insertInvoice(db, 3, "2024-09-09 18:40:00", 310000);
        insertInvoice(db, 4, "2024-12-31 20:15:00", 455000);
        insertInvoice(db, 1, "2025-01-03 08:10:00", 135000);
        insertInvoice(db, 2, "2025-02-14 10:00:00", 220000);
        insertInvoice(db, 5, "2025-04-18 14:20:00", 275000);
        insertInvoice(db, 2, "2025-07-11 17:45:00", 365000);
        insertInvoice(db, 3, "2025-10-08 19:30:00", 410000);
        insertInvoice(db, 1, "2026-01-09 08:30:00", 150000);
        insertInvoice(db, 4, "2026-02-21 11:15:00", 260000);
        insertInvoice(db, 2, "2026-03-16 16:25:00", 320000);
        insertInvoice(db, 3, "2026-04-16 09:00:00", 180000);
        insertInvoice(db, 4, "2026-04-16 12:15:00", 245000);
        insertInvoice(db, 5, "2026-04-16 18:40:00", 390000);
        insertInvoice(db, 2, "2026-05-12 15:30:00", 275000);
        insertInvoice(db, 6, "2026-08-25 19:20:00", 460000);
        insertInvoice(db, 1, "2026-10-02 20:10:00", 520000);
        insertInvoice(db, 3, "2026-12-20 21:00:00", 610000);
        seedInvoiceDetailsIfNeeded(db);
    }

    private int getRowCount(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + tableName, null);
        try {
            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
            return 0;
        } finally {
            cursor.close();
        }
    }

    private void insertInvoice(SQLiteDatabase db, int tableId, String ngayLap, double tongTien) {
        ContentValues values = new ContentValues();
        values.put("MaBan", tableId);
        values.put("NgayLap", ngayLap);
        values.put("TongTien", tongTien);
        values.put("TrangThai", "Da thanh toan");
        db.insert("HoaDon", null, values);
    }

    private void ensureBanSchema(SQLiteDatabase db) {
        try {
            db.execSQL("ALTER TABLE Ban ADD COLUMN SoCho INTEGER DEFAULT 4");
        } catch (Exception ignored) {
        }

        try {
            db.execSQL("ALTER TABLE Ban ADD COLUMN GhiChu TEXT DEFAULT ''");
        } catch (Exception ignored) {
        }

        db.execSQL("UPDATE Ban SET SoCho = COALESCE(SoCho, 4)");
        db.execSQL("UPDATE Ban SET GhiChu = COALESCE(GhiChu, '')");

        try {
            db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS idx_ban_tenban_unique ON Ban(TenBan COLLATE NOCASE)");
        } catch (Exception ignored) {
        }
    }

    private void ensureDrinkSchema(SQLiteDatabase db) {
        try {
            db.execSQL("ALTER TABLE DoUong ADD COLUMN DanhMuc TEXT DEFAULT ''");
        } catch (Exception ignored) {
        }

        try {
            db.execSQL("ALTER TABLE DoUong ADD COLUMN ConBan INTEGER DEFAULT 1");
        } catch (Exception ignored) {
        }

        db.execSQL("UPDATE DoUong SET DanhMuc = COALESCE(DanhMuc, '')");
        db.execSQL("UPDATE DoUong SET ConBan = COALESCE(ConBan, 1)");
    }

    private void ensureCartSchema(SQLiteDatabase db) {
        try {
            db.execSQL("ALTER TABLE GioHang ADD COLUMN TrangThai INTEGER DEFAULT 1");
        } catch (Exception ignored) {
        }

        try {
            db.execSQL("ALTER TABLE ChiTietGioHang ADD COLUMN GhiChu TEXT DEFAULT ''");
        } catch (Exception ignored) {
        }

        db.execSQL("UPDATE GioHang SET TrangThai = COALESCE(TrangThai, 1)");
        db.execSQL("UPDATE ChiTietGioHang SET GhiChu = COALESCE(GhiChu, '')");
    }

    private void ensureInvoiceDetailSchema(SQLiteDatabase db) {
        try {
            db.execSQL("ALTER TABLE ChiTietHoaDon ADD COLUMN TenDoUong TEXT");
        } catch (Exception ignored) {
        }
    }

    private void seedInvoiceDetailsIfNeeded(SQLiteDatabase db) {
        if (getRowCount(db, "ChiTietHoaDon") > 0) {
            return;
        }

        Cursor cursor = db.rawQuery("SELECT MaHD, TongTien FROM HoaDon ORDER BY MaHD ASC", null);
        try {
            while (cursor.moveToNext()) {
                int invoiceId = cursor.getInt(0);
                double total = cursor.getDouble(1);
                insertInvoiceDetails(db, invoiceId, total);
            }
        } finally {
            cursor.close();
        }
    }

    private void insertInvoiceDetails(SQLiteDatabase db, int invoiceId, double total) {
        String[] itemNames = {"Ca phe den", "Bac xiu", "Tra dao"};
        double[] ratios = {0.5, 0.3, 0.2};
        int[] quantities = {2, 1, 1};

        for (int i = 0; i < itemNames.length; i++) {
            double lineTotal = Math.round(total * ratios[i]);
            double unitPrice = Math.round(lineTotal / quantities[i]);

            ContentValues values = new ContentValues();
            values.put("MaHD", invoiceId);
            values.put("MaDU", i + 1);
            values.put("TenDoUong", itemNames[i]);
            values.put("SoLuong", quantities[i]);
            values.put("DonGia", unitPrice);
            values.put("ThanhTien", unitPrice * quantities[i]);
            db.insert("ChiTietHoaDon", null, values);
        }
    }

    public int checkLoginRole(String user, String pass) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT VaiTro FROM TaiKhoan WHERE TenDangNhap=? AND MatKhau=?",
                new String[]{user, pass}
        );
        int role = 0;
        if (cursor.moveToFirst()) {
            role = cursor.getInt(0);
        }
        cursor.close();
        return role;
    }

    public ThongKeTongQuan layThongKeTheoNgay(String ngay) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*), COALESCE(SUM(TongTien), 0), COALESCE(AVG(TongTien), 0) " +
                        "FROM HoaDon WHERE date(NgayLap) = date(?)",
                new String[]{ngay}
        );
        try {
            if (cursor.moveToFirst()) {
                return new ThongKeTongQuan(cursor.getInt(0), cursor.getDouble(1), cursor.getDouble(2));
            }
            return new ThongKeTongQuan(0, 0, 0);
        } finally {
            cursor.close();
        }
    }

    public ThongKeTongQuan layThongKeTheoNam(int nam) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*), COALESCE(SUM(TongTien), 0), COALESCE(AVG(TongTien), 0) " +
                        "FROM HoaDon WHERE strftime('%Y', NgayLap) = ?",
                new String[]{String.valueOf(nam)}
        );
        try {
            if (cursor.moveToFirst()) {
                return new ThongKeTongQuan(cursor.getInt(0), cursor.getDouble(1), cursor.getDouble(2));
            }
            return new ThongKeTongQuan(0, 0, 0);
        } finally {
            cursor.close();
        }
    }

    public List<DoanhThuTheoThang> layDoanhThuTheoThangTheoNam(int nam) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT CAST(strftime('%m', NgayLap) AS INTEGER), COUNT(*), COALESCE(SUM(TongTien), 0) " +
                        "FROM HoaDon WHERE strftime('%Y', NgayLap) = ? " +
                        "GROUP BY strftime('%m', NgayLap) ORDER BY strftime('%m', NgayLap) ASC",
                new String[]{String.valueOf(nam)}
        );

        List<DoanhThuTheoThang> danhSachDoanhThu = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                danhSachDoanhThu.add(new DoanhThuTheoThang(cursor.getInt(0), cursor.getInt(1), cursor.getDouble(2)));
            }
            return danhSachDoanhThu;
        } finally {
            cursor.close();
        }
    }

    public List<Integer> layDanhSachNamHoaDon() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT DISTINCT CAST(strftime('%Y', NgayLap) AS INTEGER) FROM HoaDon ORDER BY 1 DESC",
                null
        );

        List<Integer> danhSachNam = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                danhSachNam.add(cursor.getInt(0));
            }
            return danhSachNam;
        } finally {
            cursor.close();
        }
    }

    public List<CafeTable> getAllCafeTables() {
        List<CafeTable> tables = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT MaBan, TenBan, COALESCE(SoCho, 4), TrangThai, COALESCE(GhiChu, '') FROM Ban ORDER BY MaBan ASC",
                null
        );
        try {
            while (cursor.moveToNext()) {
                CafeTable cafeTable = new CafeTable();
                cafeTable.setId(cursor.getInt(0));
                cafeTable.setName(cursor.getString(1));
                cafeTable.setSeats(cursor.getInt(2));
                cafeTable.setStatus(cursor.getString(3));
                cafeTable.setNote(cursor.getString(4));
                tables.add(cafeTable);
            }
            return tables;
        } finally {
            cursor.close();
        }
    }

    public long insertCafeTable(CafeTable cafeTable) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TenBan", cafeTable.getName());
        values.put("SoCho", cafeTable.getSeats());
        values.put("TrangThai", cafeTable.getStatus());
        values.put("GhiChu", cafeTable.getNote());
        return db.insert("Ban", null, values);
    }

    public int updateCafeTable(CafeTable cafeTable) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TenBan", cafeTable.getName());
        values.put("SoCho", cafeTable.getSeats());
        values.put("TrangThai", cafeTable.getStatus());
        values.put("GhiChu", cafeTable.getNote());
        return db.update("Ban", values, "MaBan=?", new String[]{String.valueOf(cafeTable.getId())});
    }

    public int deleteCafeTable(int id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete("Ban", "MaBan=?", new String[]{String.valueOf(id)});
    }

    public boolean isCafeTableNameExists(String tableName, int excludeId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor;
        if (excludeId < 0) {
            cursor = db.rawQuery(
                    "SELECT MaBan FROM Ban WHERE LOWER(TenBan) = LOWER(?) LIMIT 1",
                    new String[]{tableName.trim()}
            );
        } else {
            cursor = db.rawQuery(
                    "SELECT MaBan FROM Ban WHERE LOWER(TenBan) = LOWER(?) AND MaBan <> ? LIMIT 1",
                    new String[]{tableName.trim(), String.valueOf(excludeId)}
            );
        }

        try {
            return cursor.moveToFirst();
        } finally {
            cursor.close();
        }
    }

    public int layHoacTaoMaGioHang(int maBan) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT MaGH FROM GioHang WHERE MaBan = ? AND COALESCE(TrangThai, 1) = 1 ORDER BY MaGH DESC LIMIT 1",
                new String[]{String.valueOf(maBan)}
        );

        try {
            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
        } finally {
            cursor.close();
        }

        ContentValues values = new ContentValues();
        values.put("MaBan", maBan);
        values.put("TrangThai", 1);
        return (int) db.insert("GioHang", null, values);
    }

    public int layHoacTaoGioHangTaiQuay() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT MaGH FROM GioHang WHERE MaBan IS NULL AND COALESCE(TrangThai, 1) = 1 ORDER BY MaGH DESC LIMIT 1",
                null
        );

        try {
            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
        } finally {
            cursor.close();
        }

        ContentValues values = new ContentValues();
        values.putNull("MaBan");
        values.put("TrangThai", 1);
        return (int) db.insert("GioHang", null, values);
    }

    public void luuDoUongTuApi(int maDoUong, String tenDoUong, double donGia, String hinhAnh) {
        if (maDoUong <= 0) {
            return;
        }

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MaDU", maDoUong);
        values.put("TenDoUong", tenDoUong == null || tenDoUong.trim().isEmpty()
                ? "Mon #" + maDoUong
                : tenDoUong.trim());
        values.put("DonGia", donGia);
        values.put("MoTa", "");
        if (hinhAnh == null || hinhAnh.trim().isEmpty()) {
            values.putNull("HinhAnh");
        } else {
            values.put("HinhAnh", hinhAnh.trim());
        }
        values.put("DanhMuc", "");
        values.put("ConBan", 1);

        int soDong = db.update(
                "DoUong",
                values,
                "MaDU = ?",
                new String[]{String.valueOf(maDoUong)}
        );
        if (soDong == 0) {
            db.insertWithOnConflict("DoUong", null, values, SQLiteDatabase.CONFLICT_REPLACE);
        }
    }

    public double taiGioHang(int maGH, List<GioHang> danhSachGioHang) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT c.MaDU, d.TenDoUong, c.SoLuong, c.DonGia, c.ThanhTien, d.HinhAnh, c.GhiChu " +
                        "FROM ChiTietGioHang c JOIN DoUong d ON c.MaDU = d.MaDU WHERE c.MaGH = ? ORDER BY d.TenDoUong ASC",
                new String[]{String.valueOf(maGH)}
        );

        double tongTien = 0;
        danhSachGioHang.clear();
        try {
            while (cursor.moveToNext()) {
                GioHang item = new GioHang(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getDouble(3),
                        cursor.getDouble(4),
                        cursor.getString(5),
                        cursor.getString(6)
                );
                danhSachGioHang.add(item);
                tongTien += item.getThanhTien();
            }
        } finally {
            cursor.close();
        }

        return tongTien;
    }

    public void themHoacCapNhatChiTiet(int maGH, int maDU, double donGia) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT SoLuong FROM ChiTietGioHang WHERE MaGH = ? AND MaDU = ?",
                new String[]{String.valueOf(maGH), String.valueOf(maDU)}
        );

        try {
            if (cursor.moveToFirst()) {
                db.execSQL(
                        "UPDATE ChiTietGioHang SET SoLuong = SoLuong + 1, ThanhTien = (SoLuong + 1) * ? WHERE MaGH = ? AND MaDU = ?",
                        new Object[]{donGia, maGH, maDU}
                );
            } else {
                db.execSQL(
                        "INSERT INTO ChiTietGioHang(MaGH, MaDU, SoLuong, DonGia, ThanhTien, GhiChu) VALUES (?, ?, 1, ?, ?, '')",
                        new Object[]{maGH, maDU, donGia, donGia}
                );
            }
        } finally {
            cursor.close();
        }
    }

    public void capNhatSoLuong(int maGH, int maDU, int soLuong) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(
                "UPDATE ChiTietGioHang SET SoLuong = ?, ThanhTien = ? * DonGia WHERE MaGH = ? AND MaDU = ?",
                new Object[]{soLuong, soLuong, maGH, maDU}
        );
    }

    public void xoaChiTietGioHang(int maGH, int maDU) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(
                "DELETE FROM ChiTietGioHang WHERE MaGH = ? AND MaDU = ?",
                new Object[]{maGH, maDU}
        );
    }

    public void capNhatGhiChu(int maGH, int maDU, String ghiChu) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(
                "UPDATE ChiTietGioHang SET GhiChu = ? WHERE MaGH = ? AND MaDU = ?",
                new Object[]{ghiChu, maGH, maDU}
        );
    }

    public void capNhatTrangThaiGioHang(int maGH, int trangThai) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(
                "UPDATE GioHang SET TrangThai = ? WHERE MaGH = ?",
                new Object[]{trangThai, maGH}
        );
    }

    public long thanhToanGioHang(int maGH) {
        SQLiteDatabase db = getWritableDatabase();
        long maHoaDon = -1;
        db.beginTransaction();
        try {
            int maBan = -1;
            Cursor gioHangCursor = db.rawQuery(
                    "SELECT MaBan FROM GioHang WHERE MaGH = ? LIMIT 1",
                    new String[]{String.valueOf(maGH)}
            );
            try {
                if (gioHangCursor.moveToFirst()) {
                    maBan = gioHangCursor.isNull(0) ? -1 : gioHangCursor.getInt(0);
                }
            } finally {
                gioHangCursor.close();
            }

            double tongTien = 0;
            Cursor tongTienCursor = db.rawQuery(
                    "SELECT COALESCE(SUM(ThanhTien), 0) FROM ChiTietGioHang WHERE MaGH = ?",
                    new String[]{String.valueOf(maGH)}
            );
            try {
                if (tongTienCursor.moveToFirst()) {
                    tongTien = tongTienCursor.getDouble(0);
                }
            } finally {
                tongTienCursor.close();
            }

            ContentValues hoaDonValues = new ContentValues();
            if (maBan > 0) {
                hoaDonValues.put("MaBan", maBan);
            } else {
                hoaDonValues.putNull("MaBan");
            }
            hoaDonValues.put("TongTien", tongTien);
            hoaDonValues.put("TrangThai", "Da thanh toan");
            maHoaDon = db.insert("HoaDon", null, hoaDonValues);

            if (maHoaDon > 0) {
                db.execSQL(
                        "INSERT INTO ChiTietHoaDon(MaHD, MaDU, TenDoUong, SoLuong, DonGia, ThanhTien) " +
                                "SELECT ?, c.MaDU, d.TenDoUong, c.SoLuong, c.DonGia, c.ThanhTien " +
                                "FROM ChiTietGioHang c JOIN DoUong d ON c.MaDU = d.MaDU WHERE c.MaGH = ?",
                        new Object[]{maHoaDon, maGH}
                );
            }

            db.execSQL("UPDATE GioHang SET TrangThai = 2 WHERE MaGH = ?", new Object[]{maGH});
            if (maBan > 0) {
                db.execSQL("UPDATE Ban SET TrangThai = 'Trong' WHERE MaBan = ?", new Object[]{maBan});
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return maHoaDon;
    }
}
