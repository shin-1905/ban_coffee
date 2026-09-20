package com.example.btl;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btl.thongke.ThongKeActivity;
import com.example.cafetable.Activity.TableManagementActivity;
import com.example.coffeemanager.ui.MainActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import android.widget.Toast;

import com.example.btl.api.ApiClient;
import com.example.btl.api.ApiService;
import com.example.btl.api.model.Mon;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import nhanvien.NhanVienActivity;

public class trangchu extends AppCompatActivity {

    private MaterialCardView btnNhanVien;
    private MaterialCardView btnDoUong;
    private MaterialCardView btnBan;
    private MaterialCardView btnHoaDon;
    private MaterialCardView btnThongKe;
    private MaterialButton btnDangXuat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trangchu);
        anhXaView();
        thietLapSuKien();
        testApi();
    }

    private void testApi() {
        ApiService apiService = ApiClient
                .getClient()
                .create(ApiService.class);

        apiService.getDanhSachMon().enqueue(
                new Callback<List<Mon>>() {
                    @Override
                    public void onResponse(
                            Call<List<Mon>> call,
                            Response<List<Mon>> response
                    ) {
                        if (response.isSuccessful()
                                && response.body() != null) {

                            int soLuongMon = response.body().size();

                            Toast.makeText(
                                    trangchu.this,
                                    "Nhận được " + soLuongMon + " món từ API",
                                    Toast.LENGTH_LONG
                            ).show();

                        } else {
                            Toast.makeText(
                                    trangchu.this,
                                    "API trả lỗi: " + response.code(),
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<List<Mon>> call,
                            Throwable t
                    ) {
                        Toast.makeText(
                                trangchu.this,
                                "Lỗi kết nối: " + t.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );
    }

    private void anhXaView() {
        btnNhanVien = findViewById(R.id.btnNhanVien);
        btnDoUong = findViewById(R.id.btnDoUong);
        btnBan = findViewById(R.id.btnBan);
        btnHoaDon = findViewById(R.id.btnHoaDon);
        btnThongKe = findViewById(R.id.btnThongKe);
        btnDangXuat = findViewById(R.id.btnDangXuat);
    }

    private void thietLapSuKien() {
        btnNhanVien.setOnClickListener(v -> startActivity(new Intent(this, NhanVienActivity.class)));

        btnDoUong.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("role", "manager");
            startActivity(intent);
        });

        btnBan.setOnClickListener(v -> startActivity(new Intent(this, TableManagementActivity.class)));

        btnHoaDon.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("role", "manager");
            intent.putExtra("start_destination", "history");
            startActivity(intent);
        });

        btnThongKe.setOnClickListener(v -> startActivity(new Intent(this, ThongKeActivity.class)));

        btnDangXuat.setOnClickListener(v -> {
            Intent intent = new Intent(this, login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
