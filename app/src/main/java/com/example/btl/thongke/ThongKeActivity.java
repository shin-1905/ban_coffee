package com.example.btl.thongke;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.btl.R;
import com.example.btl.SQlite;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ThongKeActivity extends AppCompatActivity {

    private Calendar lichDangChon = Calendar.getInstance();
    private final NumberFormat dinhDangTien = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
    private final SimpleDateFormat dinhDangNgayHienThi = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private final SimpleDateFormat dinhDangNgayTruyVan = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    private SQlite db;
    private int namDangChon;

    private MaterialToolbar toolbarThongKe;
    private MaterialButton btnThongKeHomNay;
    private MaterialButton btnChonNgay;
    private MaterialButton btnThongKeTheoNgay;
    private MaterialButton btnChonNam;
    private MaterialButton btnThongKeTheoNam;

    private TextView tvNgayDangChon;
    private TextView tvNamDangChon;
    private TextView tvBoLocDangXem;
    private TextView tvTongDoanhThu;
    private TextView tvSoHoaDon;
    private TextView tvTrungBinhHoaDon;
    private TextView tvTieuDeChiTietThang;
    private TextView tvThongBaoChiTietThang;

    private LinearLayout layoutChiTietTheoThang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke);

        db = new SQlite(this);
        namDangChon = lichDangChon.get(Calendar.YEAR);

        anhXaView();
        thietLapToolbar();
        thietLapSuKien();
        capNhatNgayVaNamDangChon();
        thongKeHomNay();
    }

    private void anhXaView() {
        toolbarThongKe = findViewById(R.id.toolbarThongKe);

        btnThongKeHomNay = findViewById(R.id.btnThongKeHomNay);
        btnChonNgay = findViewById(R.id.btnChonNgay);
        btnThongKeTheoNgay = findViewById(R.id.btnThongKeTheoNgay);
        btnChonNam = findViewById(R.id.btnChonNam);
        btnThongKeTheoNam = findViewById(R.id.btnThongKeTheoNam);

        tvNgayDangChon = findViewById(R.id.tvNgayDangChon);
        tvNamDangChon = findViewById(R.id.tvNamDangChon);
        tvBoLocDangXem = findViewById(R.id.tvBoLocDangXem);
        tvTongDoanhThu = findViewById(R.id.tvTongDoanhThu);
        tvSoHoaDon = findViewById(R.id.tvSoHoaDon);
        tvTrungBinhHoaDon = findViewById(R.id.tvTrungBinhHoaDon);
        tvTieuDeChiTietThang = findViewById(R.id.tvChiTietTheoThang);
        tvThongBaoChiTietThang = findViewById(R.id.tvKhongCoDuLieuThang);

        layoutChiTietTheoThang = findViewById(R.id.layoutChiTietTheoThang);
    }

    private void thietLapToolbar() {
        setSupportActionBar(toolbarThongKe);
        toolbarThongKe.setNavigationOnClickListener(v -> finish());
    }

    private void thietLapSuKien() {
        btnThongKeHomNay.setOnClickListener(v -> thongKeHomNay());
        btnChonNgay.setOnClickListener(v -> moChonNgay());
        btnThongKeTheoNgay.setOnClickListener(v -> thongKeTheoNgayDangChon());
        btnChonNam.setOnClickListener(v -> moChonNam());
        btnThongKeTheoNam.setOnClickListener(v -> thongKeTheoNamDangChon());
    }

    private void thongKeHomNay() {
        lichDangChon = Calendar.getInstance();
        namDangChon = lichDangChon.get(Calendar.YEAR);
        capNhatNgayVaNamDangChon();

        String tieuDe = "Thong ke hom nay (" + layNgayHienThi(lichDangChon) + ")";
        ThongKeTongQuan duLieuThongKe = db.layThongKeTheoNgay(layNgayTruyVan(lichDangChon));

        hienThiThongKeTongQuan(tieuDe, duLieuThongKe);
        hienThiChiTietTheoThang(null);
    }

    private void thongKeTheoNgayDangChon() {
        String tieuDe = "Thong ke ngay " + layNgayHienThi(lichDangChon);
        ThongKeTongQuan duLieuThongKe = db.layThongKeTheoNgay(layNgayTruyVan(lichDangChon));

        hienThiThongKeTongQuan(tieuDe, duLieuThongKe);
        hienThiChiTietTheoThang(null);
    }

    private void thongKeTheoNamDangChon() {
        String tieuDe = "Thong ke nam " + namDangChon;
        ThongKeTongQuan duLieuThongKe = db.layThongKeTheoNam(namDangChon);
        List<DoanhThuTheoThang> danhSachTheoThang = db.layDoanhThuTheoThangTheoNam(namDangChon);

        hienThiThongKeTongQuan(tieuDe, duLieuThongKe);
        hienThiChiTietTheoThang(danhSachTheoThang);
    }

    private void hienThiThongKeTongQuan(String tieuDe, ThongKeTongQuan duLieuThongKe) {
        tvBoLocDangXem.setText(tieuDe);
        tvTongDoanhThu.setText(dinhDangTien.format(duLieuThongKe.getTongDoanhThu()) + " d");
        tvSoHoaDon.setText(String.valueOf(duLieuThongKe.getSoHoaDon()));
        tvTrungBinhHoaDon.setText(dinhDangTien.format(duLieuThongKe.getTrungBinhHoaDon()) + " d");
    }

    private void hienThiChiTietTheoThang(List<DoanhThuTheoThang> danhSachTheoThang) {
        layoutChiTietTheoThang.removeAllViews();

        if (danhSachTheoThang == null) {
            tvTieuDeChiTietThang.setText("Chi tiet theo thang");
            tvThongBaoChiTietThang.setText("Chi tiet theo thang chi hien khi thong ke theo nam.");
            return;
        }

        tvTieuDeChiTietThang.setText("Chi tiet theo thang nam " + namDangChon);

        if (danhSachTheoThang.isEmpty()) {
            tvThongBaoChiTietThang.setText("Khong co du lieu cho nam da chon.");
            return;
        }

        tvThongBaoChiTietThang.setText("");
        for (DoanhThuTheoThang doanhThuTheoThang : danhSachTheoThang) {
            layoutChiTietTheoThang.addView(taoViewDoanhThuThang(doanhThuTheoThang));
        }
    }

    private View taoViewDoanhThuThang(DoanhThuTheoThang doanhThuTheoThang) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_monthly_revenue, layoutChiTietTheoThang, false);

        TextView tvThang = view.findViewById(R.id.tvThang);
        TextView tvSoHoaDonThang = view.findViewById(R.id.tvSoHoaDonThang);
        TextView tvDoanhThuThang = view.findViewById(R.id.tvDoanhThuThang);

        tvThang.setText("Thang " + doanhThuTheoThang.getThang());
        tvSoHoaDonThang.setText(doanhThuTheoThang.getSoHoaDon() + " hoa don");
        tvDoanhThuThang.setText(dinhDangTien.format(doanhThuTheoThang.getDoanhThu()) + " d");

        return view;
    }

    private void moChonNgay() {
        new DatePickerDialog(
                this,
                (view, nam, thang, ngay) -> {
                    lichDangChon.set(Calendar.YEAR, nam);
                    lichDangChon.set(Calendar.MONTH, thang);
                    lichDangChon.set(Calendar.DAY_OF_MONTH, ngay);
                    namDangChon = nam;
                    capNhatNgayVaNamDangChon();
                },
                lichDangChon.get(Calendar.YEAR),
                lichDangChon.get(Calendar.MONTH),
                lichDangChon.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void moChonNam() {
        List<Integer> danhSachNam = db.layDanhSachNamHoaDon();
        int namMin = danhSachNam.isEmpty() ? namDangChon - 5 : danhSachNam.get(danhSachNam.size() - 1);
        int namMax = danhSachNam.isEmpty() ? namDangChon + 1 : danhSachNam.get(0);

        NumberPicker numberPicker = new NumberPicker(this);
        numberPicker.setMinValue(namMin);
        numberPicker.setMaxValue(namMax);
        numberPicker.setValue(namDangChon);
        numberPicker.setWrapSelectorWheel(false);

        new AlertDialog.Builder(this)
                .setTitle("Chon nam thong ke")
                .setView(numberPicker)
                .setNegativeButton("Huy", null)
                .setPositiveButton("Chon", (dialog, which) -> {
                    namDangChon = numberPicker.getValue();
                    capNhatNgayVaNamDangChon();
                })
                .show();
    }

    private void capNhatNgayVaNamDangChon() {
        tvNgayDangChon.setText(layNgayHienThi(lichDangChon));
        tvNamDangChon.setText(String.valueOf(namDangChon));
    }

    private String layNgayTruyVan(Calendar lich) {
        return dinhDangNgayTruyVan.format(lich.getTime());
    }

    private String layNgayHienThi(Calendar lich) {
        return dinhDangNgayHienThi.format(lich.getTime());
    }
}
