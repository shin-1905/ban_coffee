package com.example.cafetable.Activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.btl.SQlite;
import com.example.btl.databinding.ActivityTableManagementBinding;
import com.example.btl.databinding.DialogTableFormBinding;
import com.example.cafetable.adapter.TableAdapter;
import com.example.cafetable.model.CafeTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TableManagementActivity extends AppCompatActivity implements TableAdapter.OnTableActionListener {
    private static final String STATUS_ALL = "Tat ca";
    private static final String STATUS_AVAILABLE = "Trong";
    private static final String STATUS_SERVING = "Dang phuc vu";
    private static final String STATUS_RESERVED = "Da dat truoc";

    private ActivityTableManagementBinding binding;
    private SQlite dbHelper;
    private TableAdapter tableAdapter;
    private final List<CafeTable> allTables = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTableManagementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new SQlite(this);
        tableAdapter = new TableAdapter(this);

        setupToolbar();
        setupRecyclerView();
        setupFilters();
        loadTables();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v -> finish());
        binding.fabAddTable.setOnClickListener(v -> showTableDialog(null));
    }

    private void setupRecyclerView() {
        binding.rvTables.setLayoutManager(new GridLayoutManager(this, 2));
        binding.rvTables.setAdapter(tableAdapter);
    }

    private void setupFilters() {
        String[] filterItems = {STATUS_ALL, STATUS_AVAILABLE, STATUS_SERVING, STATUS_RESERVED};
        ArrayAdapter<String> filterAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, filterItems);

        binding.autoFilterStatus.setAdapter(filterAdapter);
        binding.autoFilterStatus.setText(STATUS_ALL, false);
        binding.autoFilterStatus.setOnItemClickListener((parent, view, position, id) -> applyFilters());
        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyFilters();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void loadTables() {
        allTables.clear();
        allTables.addAll(dbHelper.getAllCafeTables());
        applyFilters();
    }

    private void applyFilters() {
        String keyword = binding.edtSearch.getText() == null
                ? ""
                : binding.edtSearch.getText().toString().trim().toLowerCase(Locale.ROOT);
        String selectedStatus = binding.autoFilterStatus.getText() == null
                ? STATUS_ALL
                : binding.autoFilterStatus.getText().toString().trim();

        List<CafeTable> filteredTables = new ArrayList<>();
        for (CafeTable cafeTable : allTables) {
            boolean matchName = cafeTable.getName().toLowerCase(Locale.ROOT).contains(keyword);
            boolean matchStatus = STATUS_ALL.equals(selectedStatus)
                    || cafeTable.getStatus().equalsIgnoreCase(selectedStatus);

            if (matchName && matchStatus) {
                filteredTables.add(cafeTable);
            }
        }

        tableAdapter.submitList(filteredTables);
        binding.tvEmpty.setVisibility(filteredTables.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void showTableDialog(CafeTable existingTable) {
        DialogTableFormBinding dialogBinding = DialogTableFormBinding.inflate(LayoutInflater.from(this));

        String[] statusItems = {STATUS_AVAILABLE, STATUS_SERVING, STATUS_RESERVED};
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, statusItems);
        dialogBinding.autoStatus.setAdapter(statusAdapter);

        boolean isEdit = existingTable != null;
        if (isEdit) {
            dialogBinding.edtTableName.setText(existingTable.getName());
            dialogBinding.edtSeats.setText(String.valueOf(existingTable.getSeats()));
            dialogBinding.autoStatus.setText(existingTable.getStatus(), false);
            dialogBinding.edtNote.setText(existingTable.getNote());
        } else {
            dialogBinding.autoStatus.setText(STATUS_AVAILABLE, false);
        }

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(isEdit ? "Cap nhat ban" : "Them ban moi")
                .setView(dialogBinding.getRoot())
                .setNegativeButton("Huy", null)
                .setPositiveButton(isEdit ? "Luu" : "Them", null)
                .create();

        dialog.setOnShowListener(d -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String name = dialogBinding.edtTableName.getText() != null
                    ? dialogBinding.edtTableName.getText().toString().trim() : "";
            String seatsText = dialogBinding.edtSeats.getText() != null
                    ? dialogBinding.edtSeats.getText().toString().trim() : "";
            String status = dialogBinding.autoStatus.getText() != null
                    ? dialogBinding.autoStatus.getText().toString().trim() : "";
            String note = dialogBinding.edtNote.getText() != null
                    ? dialogBinding.edtNote.getText().toString().trim() : "";

            if (!validateInput(existingTable, name, seatsText, status, dialogBinding)) {
                return;
            }

            int seats = Integer.parseInt(seatsText);
            if (isEdit) {
                existingTable.setName(name);
                existingTable.setSeats(seats);
                existingTable.setStatus(status);
                existingTable.setNote(note);
                dbHelper.updateCafeTable(existingTable);
                Toast.makeText(this, "Da cap nhat ban", Toast.LENGTH_SHORT).show();
            } else {
                dbHelper.insertCafeTable(new CafeTable(name, seats, status, note));
                Toast.makeText(this, "Da them ban moi", Toast.LENGTH_SHORT).show();
            }

            loadTables();
            dialog.dismiss();
        }));

        dialog.show();
    }

    private boolean validateInput(CafeTable existingTable, String name, String seatsText, String status,
                                  DialogTableFormBinding dialogBinding) {
        dialogBinding.layoutTableName.setError(null);
        dialogBinding.layoutSeats.setError(null);
        dialogBinding.layoutStatus.setError(null);

        if (TextUtils.isEmpty(name)) {
            dialogBinding.layoutTableName.setError("Ten ban khong duoc de trong");
            return false;
        }

        if (TextUtils.isEmpty(seatsText)) {
            dialogBinding.layoutSeats.setError("So cho khong duoc de trong");
            return false;
        }

        int seats;
        try {
            seats = Integer.parseInt(seatsText);
        } catch (NumberFormatException e) {
            dialogBinding.layoutSeats.setError("So cho phai la so");
            return false;
        }

        if (seats <= 0) {
            dialogBinding.layoutSeats.setError("So cho phai lon hon 0");
            return false;
        }

        if (TextUtils.isEmpty(status)) {
            dialogBinding.layoutStatus.setError("Vui long chon trang thai");
            return false;
        }

        int excludeId = existingTable != null ? existingTable.getId() : -1;
        if (dbHelper.isCafeTableNameExists(name, excludeId)) {
            dialogBinding.layoutTableName.setError("Ten ban da ton tai");
            return false;
        }

        return true;
    }

    @Override
    public void onTableClick(CafeTable cafeTable) {
        String message = "Ten ban: " + cafeTable.getName()
                + "\nSo cho: " + cafeTable.getSeats()
                + "\nTrang thai: " + cafeTable.getStatus()
                + "\nGhi chu: " + (TextUtils.isEmpty(cafeTable.getNote()) ? "Khong co" : cafeTable.getNote());

        new AlertDialog.Builder(this)
                .setTitle("Chi tiet ban")
                .setMessage(message)
                .setPositiveButton("Sua", (dialog, which) -> showTableDialog(cafeTable))
                .setNeutralButton("Doi trang thai", (dialog, which) -> toggleTableStatus(cafeTable))
                .setNegativeButton("Dong", null)
                .show();
    }

    @Override
    public void onTableLongClick(CafeTable cafeTable, View anchor) {
        PopupMenu popupMenu = new PopupMenu(this, anchor);
        popupMenu.getMenu().add(0, 1, 0, "Sua");
        popupMenu.getMenu().add(0, 2, 1, "Xoa");
        popupMenu.getMenu().add(0, 3, 2, "Doi trang thai");

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == 1) {
                showTableDialog(cafeTable);
                return true;
            }
            if (item.getItemId() == 2) {
                confirmDelete(cafeTable);
                return true;
            }
            if (item.getItemId() == 3) {
                toggleTableStatus(cafeTable);
                return true;
            }
            return false;
        });

        popupMenu.show();
    }

    private void confirmDelete(CafeTable cafeTable) {
        new AlertDialog.Builder(this)
                .setTitle("Xoa ban")
                .setMessage("Ban co chac muon xoa " + cafeTable.getName() + "?")
                .setNegativeButton("Huy", null)
                .setPositiveButton("Xoa", (dialog, which) -> {
                    dbHelper.deleteCafeTable(cafeTable.getId());
                    loadTables();
                    Toast.makeText(this, "Da xoa ban", Toast.LENGTH_SHORT).show();
                })
                .show();
    }

    private void toggleTableStatus(CafeTable cafeTable) {
        String nextStatus;
        if (STATUS_AVAILABLE.equalsIgnoreCase(cafeTable.getStatus())) {
            nextStatus = STATUS_SERVING;
        } else if (STATUS_SERVING.equalsIgnoreCase(cafeTable.getStatus())) {
            nextStatus = STATUS_RESERVED;
        } else {
            nextStatus = STATUS_AVAILABLE;
        }

        cafeTable.setStatus(nextStatus);
        dbHelper.updateCafeTable(cafeTable);
        loadTables();
        Toast.makeText(this, "Da chuyen sang: " + nextStatus, Toast.LENGTH_SHORT).show();
    }
}
