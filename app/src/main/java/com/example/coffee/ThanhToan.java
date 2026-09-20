package com.example.coffee;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btl.R;
import com.example.btl.SQlite;

import java.util.ArrayList;
import java.util.Locale;

public class ThanhToan extends AppCompatActivity {
    private ListView lvGioHang;
    private TextView txtTongTien;
    private TextView txtTienTraLai;
    private EditText edtTienKhach;
    private RadioGroup rgPhuongThuc;
    private ImageView imgQr;
    private Button btnThanhToan;

    private final ArrayList<GioHang> danhSachThanhToan = new ArrayList<>();
    private GioHangAdapter gioHangAdapter;
    private SQlite sqliteHelper;
    private int maGioHang;
    private double tongTienPhaiTra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coffee_payment);

        anhXaView();
        sqliteHelper = new SQlite(this);
        maGioHang = getIntent().getIntExtra("MaGH", -1);
        tongTienPhaiTra = getIntent().getDoubleExtra("TongTien", 0);

        txtTongTien.setText(String.format(Locale.getDefault(), "Tong: %,.0f VND", tongTienPhaiTra));
        gioHangAdapter = new GioHangAdapter(this, R.layout.item_coffee_cart, danhSachThanhToan, false, null);
        lvGioHang.setAdapter(gioHangAdapter);

        taiChiTietHoaDon();
        thietLapSuKien();
    }

    private void anhXaView() {
        lvGioHang = findViewById(R.id.lv_giohang_thanhtoan);
        txtTongTien = findViewById(R.id.txt_tongtien_thanhtoan);
        txtTienTraLai = findViewById(R.id.txt_tientralai);
        edtTienKhach = findViewById(R.id.edt_tienkhach);
        rgPhuongThuc = findViewById(R.id.radio_group_thanhtoan);
        imgQr = findViewById(R.id.img_qr);
        btnThanhToan = findViewById(R.id.btn_thanhtoan);
    }

    private void thietLapSuKien() {
        rgPhuongThuc.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_chuyenkhoan) {
                imgQr.setVisibility(View.VISIBLE);
                edtTienKhach.setVisibility(View.GONE);
                txtTienTraLai.setVisibility(View.GONE);
            } else {
                imgQr.setVisibility(View.GONE);
                edtTienKhach.setVisibility(View.VISIBLE);
                txtTienTraLai.setVisibility(View.VISIBLE);
            }
        });

        edtTienKhach.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tinhTienTraLai();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btnThanhToan.setOnClickListener(v -> {
            if (rgPhuongThuc.getCheckedRadioButtonId() == R.id.rb_tienmat && !kiemTraTienKhach()) {
                Toast.makeText(this, "Vui long nhap du tien khach dua", Toast.LENGTH_SHORT).show();
                return;
            }

            long maHoaDon = sqliteHelper.thanhToanGioHang(maGioHang);
            if (maHoaDon <= 0) {
                Toast.makeText(this, "Thanh toan that bai", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "Thanh toan thanh cong", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, HoaDonActivity.class);
            intent.putExtra("invoice_id", maHoaDon);
            startActivity(intent);
            finish();
        });
    }

    private void taiChiTietHoaDon() {
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT c.MaDU, d.TenDoUong, c.SoLuong, c.DonGia, c.ThanhTien, d.HinhAnh, c.GhiChu " +
                        "FROM ChiTietGioHang c JOIN DoUong d ON c.MaDU = d.MaDU WHERE c.MaGH = ? ORDER BY d.TenDoUong ASC",
                new String[]{String.valueOf(maGioHang)}
        );

        danhSachThanhToan.clear();
        try {
            while (cursor.moveToNext()) {
                danhSachThanhToan.add(new GioHang(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getDouble(3),
                        cursor.getDouble(4),
                        cursor.getString(5),
                        cursor.getString(6)
                ));
            }
        } finally {
            cursor.close();
        }
        gioHangAdapter.notifyDataSetChanged();
    }

    private boolean kiemTraTienKhach() {
        String tienKhachText = edtTienKhach.getText().toString().trim();
        if (tienKhachText.isEmpty()) {
            return false;
        }
        try {
            return Double.parseDouble(tienKhachText) >= tongTienPhaiTra;
        } catch (Exception e) {
            return false;
        }
    }

    private void tinhTienTraLai() {
        try {
            double tienKhach = Double.parseDouble(edtTienKhach.getText().toString().trim());
            double tienTraLai = tienKhach - tongTienPhaiTra;
            if (tienTraLai >= 0) {
                txtTienTraLai.setText(String.format(Locale.getDefault(), "Tien tra lai: %,.0f VND", tienTraLai));
            } else {
                txtTienTraLai.setText("Tien tra lai: Khach dua thieu");
            }
        } catch (Exception e) {
            txtTienTraLai.setText("Tien tra lai: 0 VND");
        }
    }
}
