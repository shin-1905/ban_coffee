package com.example.coffeemanager.ui;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.btl.SQlite;
import com.example.btl.databinding.FragmentInvoiceBinding;
import com.example.btl.databinding.ItemInvoiceRowBinding;

import java.text.NumberFormat;
import java.util.Locale;

public class InvoiceFragment extends Fragment {

    private static final String ARG_INVOICE_ID = "invoice_id";

    private final NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("vi", "VN"));

    private FragmentInvoiceBinding binding;
    private SQlite dbHelper;
    private long invoiceId = -1;

    public static InvoiceFragment newInstance(long id) {
        InvoiceFragment fragment = new InvoiceFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_INVOICE_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentInvoiceBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        dbHelper = new SQlite(requireContext());
        if (getArguments() != null) {
            invoiceId = getArguments().getLong(ARG_INVOICE_ID, -1);
        }

        binding.btnDone.setText("Quay lai");
        binding.btnDone.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        if (invoiceId != -1) {
            loadInvoiceFromDb(invoiceId);
        }
    }

    private void loadInvoiceFromDb(long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT MaBan, NgayLap, TongTien FROM HoaDon WHERE MaHD = ?", new String[]{String.valueOf(id)});
        try {
            if (cursor.moveToFirst()) {
                binding.tvInvoiceId.setText("So: " + id);
                binding.tvInvoiceTable.setText("Ban " + cursor.getInt(0));
                binding.tvInvoiceDate.setText("Ngay: " + cursor.getString(1));
                binding.tvInvoicePrintTime.setText("In luc: " + cursor.getString(1));
                binding.tvInvoiceInTime.setText("Gio vao: " + cursor.getString(1));
                binding.tvInvoiceOutTime.setText("Gio ra: " + cursor.getString(1));
                binding.tvInvoiceCashier.setText("Thu ngan: Quan ly");
                binding.tvInvoiceTotal.setText(formatter.format(cursor.getDouble(2)) + " đ");
            }
        } finally {
            cursor.close();
        }

        Cursor details = db.rawQuery(
                "SELECT TenDoUong, SoLuong, DonGia, ThanhTien FROM ChiTietHoaDon WHERE MaHD = ? ORDER BY MaDU ASC",
                new String[]{String.valueOf(id)}
        );
        try {
            while (details.moveToNext()) {
                addItemToLayout(details.getString(0), details.getInt(1), details.getDouble(2), details.getDouble(3));
            }
        } finally {
            details.close();
        }
    }

    private void addItemToLayout(String name, int qty, double price, double subtotal) {
        ItemInvoiceRowBinding itemBinding = ItemInvoiceRowBinding.inflate(getLayoutInflater(), binding.layoutItemsContainer, true);
        itemBinding.tvItemName.setText(name);
        itemBinding.tvItemQuantity.setText(String.valueOf(qty));
        itemBinding.tvItemPrice.setText(formatter.format(price));
        itemBinding.tvItemSubtotal.setText(formatter.format(subtotal));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
