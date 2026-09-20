package nhanvien;

import android.content.Context;
import android.database.Cursor;

import com.example.btl.SQlite;

import java.util.ArrayList;

public class NhanVienQuery {
    private final SQlite db;

    public NhanVienQuery(Context context) {
        db = new SQlite(context);
    }

    public ArrayList<NhanVien> getAll() {
        String sql = "SELECT * FROM NhanVien";
        return docDanhSach(sql, null);
    }

    public ArrayList<NhanVien> search(String keyword) {
        String sql, tuKhoa;

        sql = "SELECT * FROM NhanVien WHERE HoTen LIKE ? OR MaNV LIKE ?";
        tuKhoa = "%" + keyword + "%";
        return docDanhSach(sql, new String[]{tuKhoa, tuKhoa});
    }

    public void insert(String ma, String ten, String gt,
                       String ns, String sdt, String dc) {
        db.getWritableDatabase().execSQL(
                "INSERT INTO TaiKhoan(TenDangNhap,MatKhau,VaiTro) VALUES(?,?,2)",
                new Object[]{ma, ns}
        );

        db.getWritableDatabase().execSQL(
                "INSERT INTO NhanVien(MaNV,HoTen,GioiTinh,NgaySinh,SoDienThoai,DiaChi) VALUES(?,?,?,?,?,?)",
                new Object[]{ma, ten, gt, ns, sdt, dc}
        );
    }

    public void update(String ma, String ten, String gt,
                       String ns, String sdt, String dc) {
        db.getWritableDatabase().execSQL(
                "UPDATE NhanVien SET HoTen=?,GioiTinh=?,NgaySinh=?,SoDienThoai=?,DiaChi=? WHERE MaNV=?",
                new Object[]{ten, gt, ns, sdt, dc, ma}
        );
    }

    public void delete(String ma) {
        db.getWritableDatabase().execSQL(
                "DELETE FROM NhanVien WHERE MaNV=?",
                new Object[]{ma}
        );

        db.getWritableDatabase().execSQL(
                "DELETE FROM TaiKhoan WHERE TenDangNhap=?",
                new Object[]{ma}
        );
    }

    private ArrayList<NhanVien> docDanhSach(String sql, String[] thamSo) {
        ArrayList<NhanVien> danhSach;
        Cursor cursor;

        danhSach = new ArrayList<>();
        cursor = db.getReadableDatabase().rawQuery(sql, thamSo);
        try {
            while (cursor.moveToNext()) {
                danhSach.add(taoNhanVien(cursor));
            }
            return danhSach;
        } finally {
            cursor.close();
        }
    }

    private NhanVien taoNhanVien(Cursor cursor) {
        String maNV, hoTen, gioiTinh, ngaySinh, soDienThoai, diaChi;

        maNV = cursor.getString(0);
        hoTen = cursor.getString(1);
        gioiTinh = cursor.getString(2);
        ngaySinh = cursor.getString(3);
        soDienThoai = cursor.getString(4);
        diaChi = cursor.getString(5);

        return new NhanVien(maNV, hoTen, gioiTinh, ngaySinh, soDienThoai, diaChi);
    }
}
