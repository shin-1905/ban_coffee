package com.example.coffee;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.btl.R;

import java.util.ArrayList;
import java.util.Locale;

public class GioHangAdapter extends ArrayAdapter<GioHang> {
    public interface GioHangListener {
        void onTangSoLuong(GioHang item);
        void onGiamSoLuong(GioHang item);
        void onXoaMon(GioHang item);
    }

    private final Activity context;
    private final int layout;
    private final ArrayList<GioHang> danhSachGioHang;
    private final boolean choPhepSua;
    private final GioHangListener listener;

    public GioHangAdapter(Activity context, int layout, ArrayList<GioHang> danhSachGioHang,
                          boolean choPhepSua, @Nullable GioHangListener listener) {
        super(context, layout, danhSachGioHang);
        this.context = context;
        this.layout = layout;
        this.danhSachGioHang = danhSachGioHang;
        this.choPhepSua = choPhepSua;
        this.listener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(layout, parent, false);
        }

        GioHang item = danhSachGioHang.get(position);
        ImageView imgDoUong = convertView.findViewById(R.id.img_giohang_douong);
        TextView txtTen = convertView.findViewById(R.id.txt_giohang_ten);
        TextView txtGia = convertView.findViewById(R.id.txt_giohang_gia);
        TextView txtThanhTien = convertView.findViewById(R.id.txt_giohang_thanhtien);
        TextView txtSoLuong = convertView.findViewById(R.id.txt_giohang_soluong);
        TextView btnTru = convertView.findViewById(R.id.btn_giohang_tru);
        TextView btnCong = convertView.findViewById(R.id.btn_giohang_cong);
        ImageView btnXoa = convertView.findViewById(R.id.btn_giohang_xoa);
        EditText edtGhiChu = convertView.findViewById(R.id.edt_giohang_ghichu);

        txtTen.setText(item.getTenDoUong());
        txtGia.setText(String.format(Locale.getDefault(), "%,.0f VND", item.getDonGia()));
        txtThanhTien.setText(String.format(Locale.getDefault(), "Thanh tien: %,.0f VND", item.getThanhTien()));
        txtSoLuong.setText(String.valueOf(item.getSoLuong()));
        edtGhiChu.setText(item.getGhiChu() == null ? "" : item.getGhiChu());

        String tenHinh = item.getHinhAnh();
        if (tenHinh != null && tenHinh.contains(".")) {
            tenHinh = tenHinh.substring(0, tenHinh.lastIndexOf('.'));
        }

        int resId = 0;
        if (tenHinh != null && !tenHinh.trim().isEmpty()) {
            resId = context.getResources().getIdentifier(tenHinh.trim(), "drawable", context.getPackageName());
        }
        if (resId != 0) {
            imgDoUong.setImageResource(resId);
        } else {
            imgDoUong.setImageResource(R.drawable.cafeden);
        }

        if (choPhepSua) {
            btnTru.setVisibility(View.VISIBLE);
            btnCong.setVisibility(View.VISIBLE);
            btnXoa.setVisibility(View.VISIBLE);
            edtGhiChu.setFocusableInTouchMode(true);

            btnCong.setOnClickListener(v -> {
                if (listener != null) {
                    item.setGhiChu(edtGhiChu.getText().toString().trim());
                    listener.onTangSoLuong(item);
                }
            });

            btnTru.setOnClickListener(v -> {
                if (listener != null) {
                    item.setGhiChu(edtGhiChu.getText().toString().trim());
                    listener.onGiamSoLuong(item);
                }
            });

            btnXoa.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onXoaMon(item);
                }
            });
        } else {
            btnTru.setVisibility(View.GONE);
            btnCong.setVisibility(View.GONE);
            btnXoa.setVisibility(View.GONE);
            edtGhiChu.setFocusable(false);
            edtGhiChu.setClickable(false);
            edtGhiChu.setCursorVisible(false);
        }

        return convertView;
    }
}
