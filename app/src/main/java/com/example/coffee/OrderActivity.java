package com.example.coffee;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btl.R;
import com.example.btl.SQlite;
import com.example.btl.api.ApiClient;
import com.example.btl.api.ApiService;
import com.example.btl.api.model.ChiTietGioHangRequest;
import com.example.btl.api.model.GioHangRequest;
import com.example.btl.api.model.GioHangResponse;
import com.example.btl.api.model.Mon;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity implements GioHangAdapter.GioHangListener {
    private GridView gvDoUong;
    private ListView lvGioHang;
    private TextView txtTenBan;
    private TextView txtTongTien;
    private TextView txtCartCount;
    private TextView txtApiStateTitle;
    private TextView txtApiStateMessage;
    private EditText edtTimKiem;
    private EditText edtGhiChuDonHang;
    private Button btnXacNhan;
    private Button btnRetryMon;
    private LinearLayout layoutApiState;
    private ProgressBar progressMon;

    private final ArrayList<DoUong> danhSachDoUong = new ArrayList<>();
    private final ArrayList<DoUong> danhSachHienThi = new ArrayList<>();
    private final ArrayList<GioHang> danhSachGioHang = new ArrayList<>();

    private DoUongAdapter doUongAdapter;
    private GioHangAdapter gioHangAdapter;
    private SQlite sqliteHelper;
    private int maGioHangHienTai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coffee_order);

        anhXaView();
        sqliteHelper = new SQlite(this);

        txtTenBan.setText(R.string.pos_cart);
        maGioHangHienTai = sqliteHelper.layHoacTaoGioHangTaiQuay();

        doUongAdapter = new DoUongAdapter(this, R.layout.item_coffee_drink, danhSachHienThi);
        gvDoUong.setAdapter(doUongAdapter);

        gioHangAdapter = new GioHangAdapter(this, R.layout.item_coffee_cart, danhSachGioHang, true, this);
        lvGioHang.setAdapter(gioHangAdapter);

        thietLapSuKien();
        taiDanhSachDoUong();
        taiDuLieuGioHang();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sqliteHelper != null) {
            taiDuLieuGioHang();
        }
    }

    private void anhXaView() {
        txtTenBan = findViewById(R.id.txt_tenban_order);
        gvDoUong = findViewById(R.id.gv_douong);
        lvGioHang = findViewById(R.id.lv_giohang);
        txtTongTien = findViewById(R.id.txt_tongtien);
        txtCartCount = findViewById(R.id.txt_cart_count);
        txtApiStateTitle = findViewById(R.id.txt_api_state_title);
        txtApiStateMessage = findViewById(R.id.txt_api_state_message);
        edtTimKiem = findViewById(R.id.edt_timkiem);
        edtGhiChuDonHang = findViewById(R.id.edt_ghichu_donhang);
        btnXacNhan = findViewById(R.id.btn_xacnhan);
        btnRetryMon = findViewById(R.id.btn_retry_mon);
        layoutApiState = findViewById(R.id.layout_api_state);
        progressMon = findViewById(R.id.progress_mon);
    }

    private void thietLapSuKien() {
        btnRetryMon.setOnClickListener(v -> taiDanhSachDoUong());

        edtTimKiem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                locDoUong(s.toString().trim().toLowerCase(Locale.getDefault()));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        gvDoUong.setOnItemClickListener((parent, view, position, id) -> {
            DoUong doUong = danhSachHienThi.get(position);
            sqliteHelper.luuDoUongTuApi(
                    doUong.getMaDoUong(),
                    doUong.getTenDoUong(),
                    doUong.getDonGia(),
                    doUong.getHinhAnh()
            );
            sqliteHelper.themHoacCapNhatChiTiet(maGioHangHienTai, doUong.getMaDoUong(), doUong.getDonGia());
            Toast.makeText(this, "Da them " + doUong.getTenDoUong(), Toast.LENGTH_SHORT).show();
            taiDuLieuGioHang();
        });

        btnXacNhan.setOnClickListener(v -> {
            if (danhSachGioHang.isEmpty()) {
                Toast.makeText(this, "Gio hang trong", Toast.LENGTH_SHORT).show();
                return;
            }

            luuGhiChuGioHang();
            dongBoGioHangLenApi();
            denTrangThanhToan();
        });
    }

    private void taiDanhSachDoUong() {
        hienThiDangTaiMon();

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getDanhSachMon().enqueue(new Callback<List<Mon>>() {
            @Override
            public void onResponse(Call<List<Mon>> call, Response<List<Mon>> response) {
                if (!response.isSuccessful()) {
                    hienThiLoiTaiMon("API tra loi: " + response.code());
                    return;
                }

                List<Mon> danhSachMon = response.body();
                if (danhSachMon == null) {
                    hienThiLoiTaiMon("API tra ve du lieu rong");
                    return;
                }

                danhSachDoUong.clear();
                for (Mon mon : danhSachMon) {
                    DoUong doUong = taoDoUongTuMon(mon);
                    if (doUong != null) {
                        danhSachDoUong.add(doUong);
                    }
                }

                if (danhSachDoUong.isEmpty()) {
                    hienThiDanhSachRong();
                } else {
                    anTrangThaiTaiMon();
                }
                locDoUong(edtTimKiem.getText().toString().trim().toLowerCase(Locale.getDefault()));
            }

            @Override
            public void onFailure(Call<List<Mon>> call, Throwable t) {
                hienThiLoiTaiMon("Loi ket noi API: " + t.getMessage());
            }
        });
    }

    private void hienThiDangTaiMon() {
        txtTenBan.setText(R.string.pos_cart);
        gvDoUong.setEnabled(false);
        layoutApiState.setVisibility(View.VISIBLE);
        progressMon.setVisibility(View.VISIBLE);
        btnRetryMon.setVisibility(View.GONE);
        txtApiStateTitle.setText(R.string.pos_loading_title);
        txtApiStateMessage.setText(R.string.pos_loading_message);
        danhSachDoUong.clear();
        danhSachHienThi.clear();
        doUongAdapter.notifyDataSetChanged();
    }

    private void hienThiLoiTaiMon(String thongBao) {
        txtTenBan.setText(R.string.pos_cart);
        gvDoUong.setEnabled(false);
        layoutApiState.setVisibility(View.VISIBLE);
        progressMon.setVisibility(View.GONE);
        btnRetryMon.setVisibility(View.VISIBLE);
        txtApiStateTitle.setText(R.string.pos_error_title);
        txtApiStateMessage.setText(thongBao);
        danhSachDoUong.clear();
        danhSachHienThi.clear();
        doUongAdapter.notifyDataSetChanged();
    }

    private void hienThiDanhSachRong() {
        txtTenBan.setText(R.string.pos_cart);
        gvDoUong.setEnabled(true);
        layoutApiState.setVisibility(View.VISIBLE);
        progressMon.setVisibility(View.GONE);
        btnRetryMon.setVisibility(View.VISIBLE);
        txtApiStateTitle.setText(R.string.pos_empty_title);
        txtApiStateMessage.setText(R.string.pos_empty_message);
        danhSachHienThi.clear();
        doUongAdapter.notifyDataSetChanged();
    }

    private void anTrangThaiTaiMon() {
        txtTenBan.setText(R.string.pos_cart);
        gvDoUong.setEnabled(true);
        layoutApiState.setVisibility(View.GONE);
        progressMon.setVisibility(View.GONE);
        btnRetryMon.setVisibility(View.GONE);
    }

    private DoUong taoDoUongTuMon(Mon mon) {
        if (mon == null || mon.getMaMon() <= 0) {
            return null;
        }

        String tenMon = mon.getTenMon() == null ? "" : mon.getTenMon().trim();
        if (tenMon.isEmpty()) {
            tenMon = "Mon #" + mon.getMaMon();
        }

        return new DoUong(
                mon.getMaMon(),
                tenMon,
                mon.getGiaBan(),
                "",
                mon.getHinhAnh()
        );
    }

    public void taiDuLieuGioHang() {
        double tongTien = sqliteHelper.taiGioHang(maGioHangHienTai, danhSachGioHang);

        txtTongTien.setText(String.format(Locale.getDefault(), "%,.0f VND", tongTien));
        capNhatTrangThaiGioHang();
        gioHangAdapter.notifyDataSetChanged();
    }

    private void capNhatTrangThaiGioHang() {
        int tongSoLuong = 0;
        for (GioHang item : danhSachGioHang) {
            tongSoLuong += item.getSoLuong();
        }

        txtCartCount.setText(String.format(Locale.getDefault(), "%d mon", tongSoLuong));
        boolean coMon = !danhSachGioHang.isEmpty();
        btnXacNhan.setEnabled(coMon);
        btnXacNhan.setAlpha(coMon ? 1.0f : 0.55f);
    }

    private void locDoUong(String tuKhoa) {
        danhSachHienThi.clear();
        if (tuKhoa.isEmpty()) {
            danhSachHienThi.addAll(danhSachDoUong);
        } else {
            for (DoUong doUong : danhSachDoUong) {
                if (doUong.getTenDoUong().toLowerCase(Locale.getDefault()).contains(tuKhoa)) {
                    danhSachHienThi.add(doUong);
                }
            }
        }

        doUongAdapter.notifyDataSetChanged();
        if (danhSachDoUong.isEmpty()) {
            return;
        }

        if (danhSachHienThi.isEmpty()) {
            gvDoUong.setEnabled(true);
            layoutApiState.setVisibility(View.VISIBLE);
            progressMon.setVisibility(View.GONE);
            btnRetryMon.setVisibility(View.GONE);
            txtApiStateTitle.setText(R.string.pos_no_search_title);
            txtApiStateMessage.setText(R.string.pos_no_search_message);
        } else {
            anTrangThaiTaiMon();
        }
    }

    private void luuGhiChuGioHang() {
        for (int i = 0; i < lvGioHang.getChildCount(); i++) {
            int viTri = i + lvGioHang.getFirstVisiblePosition();
            if (viTri >= danhSachGioHang.size()) {
                continue;
            }
            EditText edtGhiChu = lvGioHang.getChildAt(i).findViewById(R.id.edt_giohang_ghichu);
            if (edtGhiChu != null) {
                String ghiChu = edtGhiChu.getText().toString().trim();
                GioHang item = danhSachGioHang.get(viTri);
                item.setGhiChu(ghiChu);
            }
        }

        for (GioHang item : danhSachGioHang) {
            sqliteHelper.capNhatGhiChu(maGioHangHienTai, item.getMaDoUong(), item.getGhiChu());
        }
    }

    private String layGhiChuDonHang() {
        if (edtGhiChuDonHang.getText() == null) {
            return "";
        }
        return edtGhiChuDonHang.getText().toString().trim();
    }

    private String taoGhiChuApi() {
        StringBuilder builder = new StringBuilder(layGhiChuDonHang());
        for (GioHang item : danhSachGioHang) {
            String ghiChuMon = item.getGhiChu() == null ? "" : item.getGhiChu().trim();
            if (ghiChuMon.isEmpty()) {
                continue;
            }

            if (builder.length() > 0) {
                builder.append(" | ");
            }
            builder.append(item.getTenDoUong()).append(": ").append(ghiChuMon);
        }
        return builder.toString();
    }

    private void dongBoGioHangLenApi() {
        List<ChiTietGioHangRequest> chiTiet = new ArrayList<>();
        for (GioHang item : danhSachGioHang) {
            if (item.getMaDoUong() > 0 && item.getSoLuong() > 0) {
                chiTiet.add(new ChiTietGioHangRequest(item.getMaDoUong(), item.getSoLuong()));
            }
        }

        if (chiTiet.isEmpty()) {
            return;
        }

        GioHangRequest request = new GioHangRequest();
        request.setMaNhanVien(null);
        request.setGhiChu(taoGhiChuApi());
        request.setChiTiet(chiTiet);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.themGioHang(request).enqueue(new Callback<GioHangResponse>() {
            @Override
            public void onResponse(Call<GioHangResponse> call, Response<GioHangResponse> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(OrderActivity.this,
                            "Chua dong bo duoc gio hang: " + response.code(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GioHangResponse> call, Throwable t) {
                Toast.makeText(OrderActivity.this,
                        "Gio hang dang luu tam tren may",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void denTrangThanhToan() {
        double tongTien = 0;
        for (GioHang item : danhSachGioHang) {
            tongTien += item.getThanhTien();
        }

        Intent intent = new Intent(this, ThanhToan.class);
        intent.putExtra("MaGH", maGioHangHienTai);
        intent.putExtra("TongTien", tongTien);
        startActivity(intent);
    }

    @Override
    public void onTangSoLuong(GioHang item) {
        sqliteHelper.capNhatGhiChu(maGioHangHienTai, item.getMaDoUong(), item.getGhiChu());
        sqliteHelper.themHoacCapNhatChiTiet(maGioHangHienTai, item.getMaDoUong(), item.getDonGia());
        taiDuLieuGioHang();
    }

    @Override
    public void onGiamSoLuong(GioHang item) {
        if (item.getSoLuong() <= 1) {
            Toast.makeText(this, "So luong khong the nho hon 1", Toast.LENGTH_SHORT).show();
            return;
        }
        sqliteHelper.capNhatGhiChu(maGioHangHienTai, item.getMaDoUong(), item.getGhiChu());
        sqliteHelper.capNhatSoLuong(maGioHangHienTai, item.getMaDoUong(), item.getSoLuong() - 1);
        taiDuLieuGioHang();
    }

    @Override
    public void onXoaMon(GioHang item) {
        sqliteHelper.xoaChiTietGioHang(maGioHangHienTai, item.getMaDoUong());
        Toast.makeText(this, "Da xoa " + item.getTenDoUong(), Toast.LENGTH_SHORT).show();
        taiDuLieuGioHang();
    }
}
