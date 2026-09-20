package com.example.coffee;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btl.R;
import com.example.btl.SQlite;

import java.text.NumberFormat;
import java.util.Locale;

public class HoaDonActivity extends AppCompatActivity {
    private final NumberFormat dinhDangTien = NumberFormat.getNumberInstance(new Locale("vi", "VN"));

    private TextView tvInvoiceTable;
    private TextView tvInvoiceDate;
    private TextView tvInvoiceId;
    private TextView tvInvoiceCashier;
    private TextView tvInvoicePrintTime;
    private TextView tvInvoiceInTime;
    private TextView tvInvoiceOutTime;
    private TextView tvInvoiceTotal;
    private LinearLayout layoutItemsContainer;
    private Button btnDone;

    private SQlite dbHelper;
    private long invoiceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_invoice);

        dbHelper = new SQlite(this);
        invoiceId = getIntent().getLongExtra("invoice_id", -1);

        anhXaView();
        btnDone.setText("Quay lai");
        btnDone.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        if (invoiceId != -1) {
            taiHoaDon(invoiceId);
        }
    }

    private void anhXaView() {
        tvInvoiceTable = findViewById(R.id.tv_invoice_table);
        tvInvoiceDate = findViewById(R.id.tv_invoice_date);
        tvInvoiceId = findViewById(R.id.tv_invoice_id);
        tvInvoiceCashier = findViewById(R.id.tv_invoice_cashier);
        tvInvoicePrintTime = findViewById(R.id.tv_invoice_print_time);
        tvInvoiceInTime = findViewById(R.id.tv_invoice_in_time);
        tvInvoiceOutTime = findViewById(R.id.tv_invoice_out_time);
        tvInvoiceTotal = findViewById(R.id.tv_invoice_total);
        layoutItemsContainer = findViewById(R.id.layout_items_container);
        btnDone = findViewById(R.id.btn_done);
    }

    private void taiHoaDon(long maHoaDon) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT MaBan, NgayLap, TongTien FROM HoaDon WHERE MaHD = ?",
                new String[]{String.valueOf(maHoaDon)}
        );
        try {
            if (cursor.moveToFirst()) {
                String ngayLap = cursor.getString(1);
                tvInvoiceId.setText("So: " + maHoaDon);
                if (cursor.isNull(0) || cursor.getInt(0) <= 0) {
                    tvInvoiceTable.setText("Don tai quay");
                } else {
                    tvInvoiceTable.setText("Ban " + cursor.getInt(0));
                }
                tvInvoiceDate.setText("Ngay: " + ngayLap);
                tvInvoicePrintTime.setText("In luc: " + ngayLap);
                tvInvoiceInTime.setText("Gio vao: " + ngayLap);
                tvInvoiceOutTime.setText("Gio ra: " + ngayLap);
                tvInvoiceCashier.setText("Thu ngan: Nhan vien");
                tvInvoiceTotal.setText(dinhDangTien.format(cursor.getDouble(2)) + " d");
            }
        } finally {
            cursor.close();
        }

        layoutItemsContainer.removeAllViews();
        Cursor chiTietCursor = db.rawQuery(
                "SELECT TenDoUong, SoLuong, DonGia, ThanhTien FROM ChiTietHoaDon WHERE MaHD = ? ORDER BY MaDU ASC",
                new String[]{String.valueOf(maHoaDon)}
        );
        try {
            while (chiTietCursor.moveToNext()) {
                themDongChiTiet(
                        chiTietCursor.getString(0),
                        chiTietCursor.getInt(1),
                        chiTietCursor.getDouble(2),
                        chiTietCursor.getDouble(3)
                );
            }
        } finally {
            chiTietCursor.close();
        }
    }

    private void themDongChiTiet(String tenMon, int soLuong, double donGia, double thanhTien) {
        LinearLayout dong = (LinearLayout) LayoutInflater.from(this)
                .inflate(R.layout.item_invoice_row, layoutItemsContainer, false);

        TextView tvItemName = dong.findViewById(R.id.tv_item_name);
        TextView tvItemQuantity = dong.findViewById(R.id.tv_item_quantity);
        TextView tvItemPrice = dong.findViewById(R.id.tv_item_price);
        TextView tvItemSubtotal = dong.findViewById(R.id.tv_item_subtotal);

        tvItemName.setText(tenMon);
        tvItemQuantity.setText(String.valueOf(soLuong));
        tvItemPrice.setText(dinhDangTien.format(donGia));
        tvItemSubtotal.setText(dinhDangTien.format(thanhTien));

        layoutItemsContainer.addView(dong);
    }
}
