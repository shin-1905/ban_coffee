package nhanvien;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btl.R;

import java.util.ArrayList;
import java.util.Calendar;

public class NhanVienAdapter extends BaseAdapter {
    private static final String GIOI_TINH_NAM = "Nam";
    private static final String GIOI_TINH_NU = "Nu";

    private final Context context;
    private final ArrayList<NhanVien> list;
    private final NhanVienQuery query;

    public NhanVienAdapter(Context context, ArrayList<NhanVien> list) {
        this.context = context;
        this.list = list;
        this.query = new NhanVienQuery(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        NhanVien nhanVien;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        nhanVien = list.get(position);
        holder.txtThongTin.setText((position + 1) + ": " + nhanVien.getMaNV() + " - " + nhanVien.getHoTen());
        view.setOnClickListener(v -> showPopup(v, nhanVien, position));
        return view;
    }

    private void showPopup(View anchorView, NhanVien nhanVien, int position) {
        PopupMenu popup;

        popup = new PopupMenu(context, anchorView);
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.chitiet) {
                showDetailDialog(nhanVien);
                return true;
            }
            if (item.getItemId() == R.id.sua) {
                showEditDialog(nhanVien, position);
                return true;
            }
            if (item.getItemId() == R.id.xoa) {
                query.delete(nhanVien.getMaNV());
                list.remove(position);
                notifyDataSetChanged();
                return true;
            }
            return false;
        });
        popup.show();
    }

    private void showDetailDialog(NhanVien nhanVien) {
        View detailView;
        TextView txtMa, txtTen, txtGT, txtNgaySinh, txtSDT, txtDiaChi;
        Button btnDong;
        AlertDialog dialog;

        detailView = LayoutInflater.from(context).inflate(R.layout.thongtinchitiet, null);
        txtMa = detailView.findViewById(R.id.txtMa);
        txtTen = detailView.findViewById(R.id.txtTen);
        txtGT = detailView.findViewById(R.id.txtGT);
        txtNgaySinh = detailView.findViewById(R.id.txtNgaySinh);
        txtSDT = detailView.findViewById(R.id.txtSDT);
        txtDiaChi = detailView.findViewById(R.id.txtDiaChi);
        btnDong = detailView.findViewById(R.id.btnDong);

        txtMa.setText(nhanVien.getMaNV());
        txtTen.setText(nhanVien.getHoTen());
        txtGT.setText(nhanVien.getGioiTinh());
        txtNgaySinh.setText(nhanVien.getNgaySinh());
        txtSDT.setText(nhanVien.getSoDienThoai());
        txtDiaChi.setText(nhanVien.getDiaChi());

        dialog = taoDialog(detailView);
        dialog.show();
        btnDong.setOnClickListener(v -> dialog.dismiss());
    }

    private void showEditDialog(NhanVien nhanVien, int position) {
        View editView;
        EditText edtTen, edtNgaySinh, edtSDT, edtDiaChi;
        RadioButton rdoNam, rdoNu;
        Button btnCapNhat;
        AlertDialog dialog;

        editView = LayoutInflater.from(context).inflate(R.layout.suanhanvien, null);
        edtTen = editView.findViewById(R.id.edtTen);
        edtNgaySinh = editView.findViewById(R.id.edtNgaySinh);
        edtSDT = editView.findViewById(R.id.edtSDT);
        edtDiaChi = editView.findViewById(R.id.edtDiaChi);
        rdoNam = editView.findViewById(R.id.rdoNam);
        rdoNu = editView.findViewById(R.id.rdoNu);
        btnCapNhat = editView.findViewById(R.id.btnCapNhat);

        edtTen.setText(nhanVien.getHoTen());
        edtNgaySinh.setText(nhanVien.getNgaySinh());
        edtSDT.setText(nhanVien.getSoDienThoai());
        edtDiaChi.setText(nhanVien.getDiaChi());
        edtSDT.setInputType(InputType.TYPE_CLASS_PHONE);

        capNhatGioiTinh(rdoNam, rdoNu, nhanVien.getGioiTinh());
        thietLapChonNgay(edtNgaySinh);

        dialog = taoDialog(editView);
        dialog.show();
        btnCapNhat.setOnClickListener(v -> xuLyCapNhatNhanVien(dialog, nhanVien, position, edtTen, edtNgaySinh, edtSDT, edtDiaChi, rdoNam, rdoNu));
    }

    private void xuLyCapNhatNhanVien(AlertDialog dialog, NhanVien nhanVien, int position,
                                     EditText edtTen, EditText edtNgaySinh, EditText edtSDT, EditText edtDiaChi,
                                     RadioButton rdoNam, RadioButton rdoNu) {
        String ten, ngaySinh, soDienThoai, diaChi, gioiTinh;

        ten = edtTen.getText().toString().trim();
        ngaySinh = edtNgaySinh.getText().toString().trim();
        soDienThoai = edtSDT.getText().toString().trim();
        diaChi = edtDiaChi.getText().toString().trim();
        gioiTinh = layGioiTinh(rdoNam, rdoNu);

        if (!duLieuHopLe(ten, gioiTinh, ngaySinh, soDienThoai, diaChi, edtSDT)) {
            return;
        }

        query.update(nhanVien.getMaNV(), ten, gioiTinh, ngaySinh, soDienThoai, diaChi);
        list.set(position, new NhanVien(nhanVien.getMaNV(), ten, gioiTinh, ngaySinh, soDienThoai, diaChi));
        notifyDataSetChanged();
        dialog.dismiss();
    }

    private AlertDialog taoDialog(View view) {
        return new AlertDialog.Builder(context).setView(view).create();
    }

    private void capNhatGioiTinh(RadioButton rdoNam, RadioButton rdoNu, String gioiTinh) {
        if (GIOI_TINH_NAM.equals(gioiTinh)) {
            rdoNam.setChecked(true);
            return;
        }
        if (GIOI_TINH_NU.equals(gioiTinh)) {
            rdoNu.setChecked(true);
        }
    }

    private void thietLapChonNgay(EditText edtNgaySinh) {
        edtNgaySinh.setFocusable(false);
        edtNgaySinh.setClickable(true);
        edtNgaySinh.setOnClickListener(v -> {
            Calendar lich;

            lich = Calendar.getInstance();
            new DatePickerDialog(
                    context,
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

    private boolean duLieuHopLe(String ten, String gioiTinh,
                                String ngaySinh, String soDienThoai, String diaChi, EditText edtSDT) {
        if (ten.isEmpty() || gioiTinh.isEmpty() || ngaySinh.isEmpty()
                || soDienThoai.isEmpty() || diaChi.isEmpty()) {
            Toast.makeText(context, "Vui long nhap day du thong tin", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!soDienThoai.matches("\\d{10}")) {
            edtSDT.setError("SDT phai du 10 so");
            return false;
        }
        return true;
    }
    private static class ViewHolder {
        private final TextView txtThongTin;

        private ViewHolder(View view) {
            txtThongTin = view.findViewById(android.R.id.text1);
        }
    }
}
