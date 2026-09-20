package nhanvien;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btl.R;

import java.util.ArrayList;
import java.util.Calendar;

public class NhanVienActivity extends AppCompatActivity {
    private static final String GIOI_TINH_NAM = "Nam";
    private static final String GIOI_TINH_NU = "Nu";

    private ListView lvNhanVien;
    private Button btnThem, btnTim;
    private EditText edtSearch;

    private ArrayList<NhanVien> list;
    private NhanVienAdapter adapter;
    private NhanVienQuery query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quanlynhanvien);

        anhXaView();
        khoiTaoDuLieu();
        thietLapSuKien();
    }

    private void anhXaView() {
        lvNhanVien = findViewById(R.id.lvNhanVien);
        btnThem = findViewById(R.id.btnThem);
        btnTim = findViewById(R.id.btnTim);
        edtSearch = findViewById(R.id.edtSearch);
    }

    private void khoiTaoDuLieu() {
        query = new NhanVienQuery(this);
        capNhatDanhSach(query.getAll());
    }

    private void thietLapSuKien() {
        btnThem.setOnClickListener(v -> hienThiDialogThem());
        btnTim.setOnClickListener(v -> timNhanVien());
    }

    private void timNhanVien() {
        String tuKhoa;

        tuKhoa = edtSearch.getText().toString().trim();
        if (tuKhoa.isEmpty()) {
            capNhatDanhSach(query.getAll());
            return;
        }

        capNhatDanhSach(query.search(tuKhoa));
        if (list.isEmpty()) {
            Toast.makeText(this, "Khong tim thay nhan vien nao", Toast.LENGTH_SHORT).show();
        }
    }

    private void capNhatDanhSach(ArrayList<NhanVien> danhSachNhanVien) {
        list = danhSachNhanVien;
        adapter = new NhanVienAdapter(this, list);
        lvNhanVien.setAdapter(adapter);
    }

    private void hienThiDialogThem() {
        View view;
        EditText edtMa, edtTen, edtNgaySinh, edtSDT, edtDiaChi;
        RadioButton rdoNam, rdoNu;
        Button btnLuu;
        AlertDialog dialog;

        view = getLayoutInflater().inflate(R.layout.themnhanvien, null);
        edtMa = view.findViewById(R.id.edtMa);
        edtTen = view.findViewById(R.id.edtTen);
        edtNgaySinh = view.findViewById(R.id.edtNgaySinh);
        edtSDT = view.findViewById(R.id.edtSDT);
        edtDiaChi = view.findViewById(R.id.edtDiaChi);
        rdoNam = view.findViewById(R.id.rdoNam);
        rdoNu = view.findViewById(R.id.rdoNu);
        btnLuu = view.findViewById(R.id.btnLuu);

        edtSDT.setInputType(InputType.TYPE_CLASS_PHONE);
        thietLapChonNgay(edtNgaySinh);

        dialog = new AlertDialog.Builder(this).setView(view).create();
        dialog.show();

        btnLuu.setOnClickListener(v -> xuLyThemNhanVien(dialog, edtMa, edtTen, edtNgaySinh, edtSDT, edtDiaChi, rdoNam, rdoNu));
    }

    private void xuLyThemNhanVien(AlertDialog dialog, EditText edtMa, EditText edtTen, EditText edtNgaySinh,
                                  EditText edtSDT, EditText edtDiaChi, RadioButton rdoNam, RadioButton rdoNu) {
        String ma, ten, ngaySinh, soDienThoai, diaChi, gioiTinh;

        ma = edtMa.getText().toString().trim();
        ten = edtTen.getText().toString().trim();
        ngaySinh = edtNgaySinh.getText().toString().trim();
        soDienThoai = edtSDT.getText().toString().trim();
        diaChi = edtDiaChi.getText().toString().trim();
        gioiTinh = layGioiTinh(rdoNam, rdoNu);

        if (!duLieuHopLe(ma, ten, gioiTinh, ngaySinh, soDienThoai, diaChi, edtSDT)) {
            return;
        }

        query.insert(ma, ten, gioiTinh, ngaySinh, soDienThoai, diaChi);
        capNhatDanhSach(query.getAll());
        dialog.dismiss();
        Toast.makeText(this, "Them thanh cong", Toast.LENGTH_SHORT).show();
    }

    private void thietLapChonNgay(EditText edtNgaySinh) {
        edtNgaySinh.setFocusable(false);
        edtNgaySinh.setOnClickListener(v -> {
            Calendar lich;

            lich = Calendar.getInstance();
            new DatePickerDialog(
                    this,
                    (view, nam, thang, ngay) ->
                            edtNgaySinh.setText(String.format("%02d/%02d/%04d", ngay, thang + 1, nam)),
                    lich.get(Calendar.YEAR),
                    lich.get(Calendar.MONTH),
                    lich.get(Calendar.DAY_OF_MONTH)
            ).show();
        });
    }

    private String layGioiTinh(RadioButton rdoNam, RadioButton rdoNu) {
        if (rdoNam.isChecked()) {
            return GIOI_TINH_NAM;
        }
        if (rdoNu.isChecked()) {
            return GIOI_TINH_NU;
        }
        return "";
    }

    private boolean duLieuHopLe(String ma, String ten, String gioiTinh,
                                String ngaySinh, String soDienThoai, String diaChi, EditText edtSDT) {
        if (ma.isEmpty() || ten.isEmpty() || gioiTinh.isEmpty()
                || ngaySinh.isEmpty() || soDienThoai.isEmpty() || diaChi.isEmpty()) {
            Toast.makeText(this, "Vui long nhap day du thong tin", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!soDienThoai.matches("\\d{10}")) {
            edtSDT.setError("SDT phai du 10 so");
            return false;
        }
        return true;
    }
}
