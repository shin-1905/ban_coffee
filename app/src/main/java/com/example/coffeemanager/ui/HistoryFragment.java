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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.btl.R;
import com.example.btl.SQlite;
import com.example.btl.databinding.FragmentHistoryBinding;
import com.example.coffeemanager.data.Invoice;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding binding;
    private InvoiceAdapter adapter;
    private SQlite dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        dbHelper = new SQlite(requireContext());
        adapter = new InvoiceAdapter();
        adapter.setOnItemClickListener(invoice -> requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_list_container, InvoiceFragment.newInstance(invoice.getId()))
                .addToBackStack(null)
                .commit());

        binding.recyclerHistory.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerHistory.setAdapter(adapter);
        binding.toolbarHistory.setTitle("Lich su hoa don");
        binding.toolbarHistory.setNavigationIcon(android.R.drawable.ic_menu_revert);
        binding.toolbarHistory.setNavigationOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        loadInvoices();
    }

    private void loadInvoices() {
        List<Invoice> invoiceList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MaHD, MaBan, NgayLap, TongTien, TrangThai FROM HoaDon ORDER BY NgayLap DESC", null);
        try {
            while (cursor.moveToNext()) {
                invoiceList.add(new Invoice(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getDouble(3),
                        cursor.getString(4)
                ));
            }
        } finally {
            cursor.close();
        }
        adapter.setInvoices(invoiceList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
