package com.example.coffee;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.btl.R;

import java.util.ArrayList;
import java.util.Locale;

public class DoUongAdapter extends ArrayAdapter<DoUong> {
    private final Activity context;
    private final int layout;
    private final ArrayList<DoUong> danhSachDoUong;

    public DoUongAdapter(Activity context, int layout, ArrayList<DoUong> danhSachDoUong) {
        super(context, layout, danhSachDoUong);
        this.context = context;
        this.layout = layout;
        this.danhSachDoUong = danhSachDoUong;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(layout, parent, false);

            holder = new ViewHolder();
            holder.imgDoUong = convertView.findViewById(R.id.img_douong);
            holder.txtTenDoUong = convertView.findViewById(R.id.txt_tendouong);
            holder.txtGiaDoUong = convertView.findViewById(R.id.txt_giadouong);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        DoUong doUong = danhSachDoUong.get(position);
        holder.txtTenDoUong.setText(doUong.getTenDoUong());
        holder.txtGiaDoUong.setText(String.format(Locale.getDefault(), "%,.0f VND", doUong.getDonGia()));

        String tenHinh = doUong.getHinhAnh();
        if (tenHinh != null && tenHinh.contains(".")) {
            tenHinh = tenHinh.substring(0, tenHinh.lastIndexOf('.'));
        }

        int resId = 0;
        if (tenHinh != null && !tenHinh.trim().isEmpty()) {
            resId = context.getResources().getIdentifier(tenHinh.trim(), "drawable", context.getPackageName());
        }
        if (resId != 0) {
            holder.imgDoUong.setImageResource(resId);
        } else {
            holder.imgDoUong.setImageResource(R.drawable.cafeden);
        }
        return convertView;
    }

    static class ViewHolder {
        ImageView imgDoUong;
        TextView txtTenDoUong;
        TextView txtGiaDoUong;
    }
}
